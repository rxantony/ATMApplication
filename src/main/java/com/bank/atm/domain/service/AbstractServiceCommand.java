package com.bank.atm.domain.service;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.HandlerWithException;
import com.bank.atm.domain.common.handler.RequestWithException;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.AccountRepository;
import com.bank.atm.domain.service.account.AccountNotExistsException;

public abstract class AbstractServiceCommand<TRequest extends RequestWithException<TResult, ServiceException>, TResult>
        implements HandlerWithException<TRequest, TResult, ServiceException> {
    private final AccountRepository accRepo;

    protected AbstractServiceCommand(AccountRepository accRepo) {
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
