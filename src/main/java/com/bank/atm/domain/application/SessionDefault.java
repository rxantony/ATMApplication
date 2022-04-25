package com.bank.atm.domain.application;

import java.util.List;
import java.util.function.Consumer;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.ObjectFactory;
import com.bank.atm.domain.common.handler.HandlerManager;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.service.ServiceException;
import com.bank.atm.domain.service.account.AccountNotExistsException;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountRequest;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountResult;
import com.bank.atm.domain.service.user.command.deposit.DepositRequest;
import com.bank.atm.domain.service.user.command.deposit.DepositResult;
import com.bank.atm.domain.service.user.command.transfer.TransferRequest;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawRequest;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawResult;
import com.bank.atm.domain.service.user.query.getowelist.GetOweListRequest;
import com.bank.atm.domain.service.user.query.getowelist.GetOweResult;

public class SessionDefault implements Session {
    private boolean sessionClosed;
    private final String accountName;
    private final HandlerManager manager;
    private final Consumer<String> eventLogout;
    private final AbstractInputHandler inputHandler;

    public SessionDefault(String accountName, HandlerManager manager, Consumer<String> eventLogout,
            ObjectFactory<Session, AbstractInputHandler> inputhandlerFactory) {

        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNull(manager, "manager");
        Guard.validateArgNotNull(eventLogout, "eventLogout");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");

        this.accountName = accountName;
        this.manager = manager;
        this.eventLogout = eventLogout;
        this.inputHandler = inputhandlerFactory.create(this);
    }

    @Override
    public String getAccountName() {
        validateSessionExpired();
        return accountName;
    }

    @Override
    public GetAccountResult    getAccount() throws ServiceException {
        validateSessionExpired();
        var oacc = manager.handle(new GetAccountRequest(accountName));
        if (!oacc.isPresent()) {
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
        return manager.handle(new DepositRequest(accountName, amount));
    }

    @Override
    public TransferResult transfer(String recipient, int amount) throws ServiceException {
        validateSessionExpired();
        return manager.handle(new TransferRequest(accountName, recipient, amount));
    }

    @Override
    public WithdrawResult withdraw(int amount) throws ServiceException {
        validateSessionExpired();
        return manager.handle(new WithdrawRequest(accountName, amount));
    }

    @Override
    public List<GetOweResult> getOweList() throws ServiceException {
        validateSessionExpired();
        return manager.handle(new GetOweListRequest(accountName));
    }

    @Override
    public AbstractInputHandler getInputHandler() {
        validateSessionExpired();
        return inputHandler;
    }

    private void validateSessionExpired() {
        if (sessionClosed)
            throw new SessionExpiredException();
    }
}
