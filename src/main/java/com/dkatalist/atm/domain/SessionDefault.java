package com.dkatalist.atm.domain;

import java.util.Optional;
import java.util.function.Consumer;

public class SessionDefault implements Session {
    private AccountDb db;
    private String accountName;
    private Consumer<String> eventLogout;
    private InputHandler inputHandler;

    public SessionDefault(String accountName, AccountDb db, Consumer<String> eventLogout, ObjectFactory<Session, InputHandler> inputhandlerFactory) {
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNull(db, "db");
        Guard.validateArgNotNull(eventLogout, "eventLogout");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");
        this.accountName = accountName;
        this.db = db;
        this.eventLogout = eventLogout;
        this.inputHandler = inputhandlerFactory.create(this);
    }

    @Override
    public void logout() {
        eventLogout.accept(accountName);
    }
    
    @Override
    public Account getAccount() throws AccountNotExistsException {
        return getAccount(accountName);
    }

    @Override
    public void deposit(int amount) throws AccountNotExistsException {
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");
        Account acc = getAccount();
        acc.setSaving(acc.getSaving() + amount);
        db.update(acc);
    }

    @Override
    public void withdraw(int amount) throws SessionBaseException {
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");
        Account acc = getAccount();
        if (amount > acc.getSaving())
            throw TransactionException.notEnoughAmount(accountName, "withdraw", amount);
        acc.setSaving(acc.getSaving() - amount);
        db.update(acc);
    }

    @Override
    public void transfer(String toAccountName, int amount) throws SessionBaseException {
        Guard.validateArgNotNullOrEmpty(toAccountName, "toAccountName");
        Guard.validateArgMustBeGreaterThan(amount, 0, "amount");
        if(toAccountName.equals(accountName))
            throw TransferException.cannotTransferToSameAccount(accountName, toAccountName, amount);
        Account myAcc = getAccount();
        if (amount > myAcc.getSaving())
            throw TransactionException.notEnoughAmount(accountName, "transfer", amount);
        Account toAcc = getAccount(toAccountName);
        myAcc.setSaving(myAcc.getSaving() - amount);
        toAcc.setSaving(toAcc.getSaving() + amount);
        db.update(myAcc);
        db.update(toAcc);

    }

    private Account getAccount(String accountName) throws AccountNotExistsException {
        Optional<Account> oacc = db.get(accountName);
        if (!oacc.isPresent())
            throw AccountNotExistsException.create(accountName);
        return oacc.get();
    }

    @Override
    public InputHandler getInputHandler() {
        return inputHandler;
    }
}
