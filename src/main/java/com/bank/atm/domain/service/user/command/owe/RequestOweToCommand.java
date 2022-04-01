package com.bank.atm.domain.service.user.command.owe;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.data.OweRepository;

public class RequestOweToCommand extends AbstractHandler<OweRequest, Integer> {
    private final OweRepository repo;
    private final AbstractHandler<OweRequest, Integer> nextOweCmd;

    public RequestOweToCommand(OweRepository repo, AbstractHandler<OweRequest, Integer> nextOweCmd) {
        super(OweRequest.class);
        Guard.validateArgNotNull(repo, "repo");
        this.repo = repo;
        this.nextOweCmd = nextOweCmd;
    }

    @Override
    public Integer execute(OweRequest request) {
        if (request.getAmount() <= request.getAccount().getBalance()) {
            if (nextOweCmd != null)
                return nextOweCmd.execute(request);
            return request.getAmount();
        }

        Owe oweTo = null;
        Owe oweFrom = null;
        var oweAmount = request.getAmount() - request.getAccount().getBalance();
        var ooweTo = repo.getOweTo(request.getAccount().getName(), request.getRecipient().getName());
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
