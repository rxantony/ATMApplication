package com.dkatalist.atm.domain.service.atm.command.oweCalcullation;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;

public class RequestOweToCommand implements Handler<OweCallculationRequest, Integer> {

    private final OweRepository repo;
    private final Handler<OweCallculationRequest, Integer> nextCallcuationCmd;

    public RequestOweToCommand(OweRepository repo, Handler<OweCallculationRequest, Integer> nextCallcuationCmd) {
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextCallcuationCmd = nextCallcuationCmd;
    }

    @Override
    public Integer execute(OweCallculationRequest request) {
        if (request.getAmount() <= request.getAccount().getBalance()) {
            if (nextCallcuationCmd != null)
                return nextCallcuationCmd.execute(request);
            return request.getAmount();
        }

        Owe oweTo = null;
        Owe oweFrom = null;
        var oweAmount = request.getAmount() - request.getAccount().getBalance();
        var ooweTo = repo.getOweTo(request.getAccount().getName(), request.getRecipient().getName()); // search our owe
                                                                                                      // to recipient //
                                                                                                      // took place
                                                                                                      // before
        if (ooweTo.isPresent()) {
            oweTo = ooweTo.get();
            oweFrom = repo.getOweFrom(request.getRecipient().getName(), request.getAccount().getName()).get();
            oweTo.setAmount(oweTo.getAmount() - oweAmount);
            oweFrom.setAmount(oweFrom.getAmount() + oweAmount);
            repo.update(oweTo, oweFrom);
        } else {
            oweTo = new Owe(request.getAccount().getName(), request.getRecipient().getName(), -oweAmount);
            oweFrom = new Owe(request.getRecipient().getName(), request.getAccount().getName(), oweAmount);
            repo.add(oweTo, oweFrom);
        }

        request.getOweList().add(oweTo);
        request.getOweList().add(oweFrom);
        return request.getAccount().getBalance();
    }
}
