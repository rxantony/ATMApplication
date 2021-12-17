package com.dkatalist.atm.domain.service.atm.command.oweCalcullation;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.OweRepository;

public class ReduceOweFromCommand implements Handler<OweCallculationRequest, Integer> {

    private OweRepository repo;
    private Handler<OweCallculationRequest, Integer> nextCallcuationCmd;

    public ReduceOweFromCommand(OweRepository repo, Handler<OweCallculationRequest, Integer> nextCallcuationCmd) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextCallcuationCmd = nextCallcuationCmd;
    }

    @Override
    public Integer execute(OweCallculationRequest request) {
        var ooweFrom = repo.getOweFrom(request.getAccount().getName(), request.getRecipient().getName());
        if (!ooweFrom.isPresent()) {
            if (nextCallcuationCmd != null)
                return nextCallcuationCmd.execute(request);
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
