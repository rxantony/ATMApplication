package com.dkatalist.atm.domain;

import java.util.Optional;

public class ATMApplicationDefault implements ATMApplication {
    public int id;
    private AccountDb db;
    private Session currentSession;
    private InputHandler inputHandler;
    private ObjectFactory<CreateSessionArg,Session> sessionFactory;

    public ATMApplicationDefault(int id, AccountDb db, ObjectFactory<CreateSessionArg,Session> sessionFactory, ObjectFactory<ATMApplication, InputHandler> inputhandlerFactory){
        Guard.validateArgNotNull(db, "db");
        Guard.validateArgNotNull(sessionFactory, "sessionFactory");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");
        this.id = id;
        this.db = db;
        this.sessionFactory = sessionFactory;
        this.inputHandler = inputhandlerFactory.create(this);
    }

    public int getId(){
        return id;
    }

    private void whenSessionLoggedOut(String accountName){
        currentSession = null;
    }

    public Session getSession(){
        return currentSession;
    }

    public InputHandler getInputHandler(){
        return inputHandler;
    }

    public void login(String userName){
        Optional<Account> oacc = db.get(userName);
        if(!oacc.isPresent()){
            Account newAcc = new Account(userName, 0);
            db.add(newAcc);
            oacc = Optional.of(newAcc);
        }
        CreateSessionArg arg = new CreateSessionArg(oacc.get().getName(), this::whenSessionLoggedOut);
        currentSession =  sessionFactory.create(arg);
    }
}
