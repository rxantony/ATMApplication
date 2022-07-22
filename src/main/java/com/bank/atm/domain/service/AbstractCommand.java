package com.bank.atm.domain.service;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandlerWithError;
import com.bank.atm.domain.common.handler.RequestWithError;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.AccountRepository;
import com.bank.atm.domain.service.account.AccountNotExistsException;

public abstract class AbstractCommand<TRequest extends RequestWithError<TResult, ServiceException>, TResult>
        extends AbstractHandlerWithError<TRequest, TResult, ServiceException> {
    private final AccountRepository accRepo;

    protected AbstractCommand(AccountRepository accRepo, Class<TRequest> requestClass) {
        super(requestClass);
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
