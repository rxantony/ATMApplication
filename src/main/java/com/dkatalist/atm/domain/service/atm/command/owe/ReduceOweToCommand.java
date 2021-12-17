package com.dkatalist.atm.domain.service.atm.command.owe;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.OweRepository;

public class ReduceOweToCommand implements Handler<OweRequest, Integer> {

    private final OweRepository repo;
    private final Handler<OweRequest, Integer> nextOweCmd;

    public ReduceOweToCommand(OweRepository repo, Handler<OweRequest, Integer> nextOweCmd) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextOweCmd = nextOweCmd;
    }

    @Override
    public Integer execute(OweRequest request) {
        var ooweTo = repo.getOweTo(request.getAccount().getName(), request.getRecipient().getName());
        if (!ooweTo.isPresent()) {
            if (nextOweCmd != null)
                return nextOweCmd.execute(request);
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
