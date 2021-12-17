package com.dkatalist.atm.domain.application.facade;

import java.util.List;
import java.util.function.Consumer;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.application.SessionExpiredException;
import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.facade.ATMService;
import com.dkatalist.atm.domain.service.facade.AccountService;
import com.dkatalist.atm.domain.service.facade.ATMService.DepositResult;
import com.dkatalist.atm.domain.service.facade.ATMService.TransactionResult;
import com.dkatalist.atm.domain.service.facade.ATMService.TransferResult;
import com.dkatalist.atm.domain.service.AccountNotExistsException;

public class SessionDefault implements Session {
    private String accountName;
    private ATMService atmService;
    private AccountService accountService;
    private boolean sessionClosed;
    private Consumer<String> eventLogout;
    private AbstractInputHandler inputHandler;

    public SessionDefault(String accountName, ATMService atmService, AccountService accountService,
            Consumer<String> eventLogout, ObjectFactory<Session, AbstractInputHandler> inputhandlerFactory) {

        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNull(atmService, "atmService");
        Guard.validateArgNotNull(accountService, "accountService");
        Guard.validateArgNotNull(eventLogout, "eventLogout");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");

        this.accountName = accountName;
        this.atmService = atmService;
        this.accountService = accountService;
        this.eventLogout = eventLogout;
        this.inputHandler = inputhandlerFactory.create(this);
    }

    @Override
    public String getAccountName() {
        validateSessionExpired();
        return accountName;
    }

    @Override
    public Account getAccount() throws AccountNotExistsException {
        validateSessionExpired();
        var oacc = accountService.getAccount(accountName);
        if(!oacc.isPresent()){
            logout();
            throw AccountNotExistsException.create(accountName);
        }
        return oacc.get();
    }

    @Override
    public void logout() {
        validateSessionExpired();
        sessionClosed = true;
        eventLogout.accept(accountName);
    }

    @Override
    public DepositResult deposit(int amount) throws ServiceException {
        validateSessionExpired();
        return atmService.deposit(accountName, amount);
    }

    @Override
    public TransferResult transfer(String recipient, int amount) throws ServiceException {
        validateSessionExpired();
        return atmService.transfer(accountName, recipient, amount);
    }

    @Override
    public TransactionResult withdraw(int amount) throws ServiceException {
        validateSessionExpired();
        return atmService.withdraw(accountName, amount);
    }

    @Override
    public AbstractInputHandler getInputHandler() {
        validateSessionExpired();
        return inputHandler;
    }

    @Override
    public List<Owe> getOweList() {
        validateSessionExpired();
        return atmService.getOweList(accountName);
    }

    private void validateSessionExpired() {
        if (sessionClosed)
            throw new SessionExpiredException();
    }
}
