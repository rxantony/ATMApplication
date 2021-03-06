package com.bank.atm.domain.service.user.command.owe;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.OweRepository;

public class ReduceOweToCommand extends AbstractHandler<OweRequest, Integer> {
    private final OweRepository repo;
    private final AbstractHandler<OweRequest, Integer> nextOweCmd;

    public ReduceOweToCommand(OweRepository repo, AbstractHandler<OweRequest, Integer> nextOweCmd) {
        super(OweRequest.class);
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextOweCmd = nextOweCmd;
    }

    @Override
    public Integer handle(OweRequest request) {
        var ooweTo = repo.getOweTo(request.getAccount().getName(), request.getRecipient().getName());
        if (!ooweTo.isPresent()) {
            if (nextOweCmd != null)
                return nextOweCmd.handle(request);
            return request.getAmount();
        }

        var amount = request.getAmount();
        var oweTo = ooweTo.get();
        var oweFrom = repo.getOweFrom(oweTo.getAccount2(), oweTo.getAccount1()).get();
        if (amount < oweFrom.getAmount()) {//
            oweTo.setAmount(oweTo.getAmount() + amount);
            oweFrom.setAmount(oweFrom.getAmount() - amount);
        } else {
            oweTo.setAmount(0);
            oweFrom.setAmount(0);
        }

        repo.update(oweTo, oweFrom);

        request.getOweList().add(oweTo);
        request.getOweList().add(oweFrom);

        return amount;
    }
}
