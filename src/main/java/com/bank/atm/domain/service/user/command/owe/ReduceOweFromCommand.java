package com.bank.atm.domain.service.user.command.owe;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.OweRepository;

public class ReduceOweFromCommand extends AbstractHandler<OweRequest, Integer> {
    private final OweRepository repo;
    private final AbstractHandler<OweRequest, Integer> nextOweCmd;

    public ReduceOweFromCommand(OweRepository repo, AbstractHandler<OweRequest, Integer> nextOweCmd) {
        super(OweRequest.class);
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextOweCmd = nextOweCmd;
    }

    @Override
    public Integer execute(OweRequest request) {
        var ooweFrom = repo.getOweFrom(request.getAccount().getName(), request.getRecipient().getName());
        if (!ooweFrom.isPresent()) {
            if (nextOweCmd != null)
                return nextOweCmd.execute(request);
            return request.getAmount();
        }

        var amount = request.getAmount();
        var oweFrom = ooweFrom.get();
        var oweTo = repo.getOweTo(oweFrom.getAccount2(), oweFrom.getAccount1()).get();
        if (amount < oweFrom.getAmount()) {//
            oweFrom.setAmount(oweFrom.getAmount() - amount);
            oweTo.setAmount(oweTo.getAmount() + amount);
            amount = 0;
        } else {
            amount -= oweFrom.getAmount();
            oweFrom.setAmount(0);
            oweTo.setAmount(0);
        }

        repo.update(oweTo, oweFrom);

        request.getOweList().add(oweTo);
        request.getOweList().add(oweFrom);

        return amount;
    }

}
