package com.dkatalist.atm.domain.service.cqrs;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.cqrs.handler.HandlerWithException;
import com.dkatalist.atm.domain.common.cqrs.handler.RequestWithException;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.ServiceException;

public abstract class AbstractATMCommand<TRequest extends RequestWithException<TResult, ServiceException>, TResult>
        implements HandlerWithException<TRequest, TResult, ServiceException> {

    private final AccountRepository accRepo;

    protected AbstractATMCommand(AccountRepository accRepo) {
        Guard.validateArgNotNull(accRepo, "accRepo");
        this.accRepo = accRepo;
    }

    protected Account getAccount(String accountName) throws AccountNotExistsException {
        var oacc = accRepo.get(accountName);
        if (!oacc.isPresent())
            throw AccountNotExistsException.create(accountName);
        return oacc.get();
    }

    protected void updateAccounts(Account... accounts) {
        accRepo.update(accounts);
    }
}
