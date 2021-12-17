package com.dkatalist.atm.domain.application.facade;

import java.util.Optional;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.application.CreateSessionArg;
import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.service.facade.AccountService;

public class SessionManagerDefault implements SessionManager {
    private AccountService service;
    private Session currentSession;
    private AbstractInputHandler inputHandler;
    private ObjectFactory<CreateSessionArg, Session> sessionFactory;

    public SessionManagerDefault(AccountService service, ObjectFactory<CreateSessionArg, Session> sessionFactory,
            ObjectFactory<SessionManager, AbstractInputHandler> inputhandlerFactory) {
        Guard.validateArgNotNull(service, "service");
        Guard.validateArgNotNull(sessionFactory, "sessionFactory");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");
        this.service = service;
        this.sessionFactory = sessionFactory;
        this.inputHandler = inputhandlerFactory.create(this);
    }

    private void whenSessionLoggedOut(String accountName) {
        currentSession = null;
    }

    public Session getSession() {
        return currentSession;
    }

    public AbstractInputHandler getInputHandler() {
        return inputHandler;
    }

    public void login(String userName) {
        var oacc = service.getAccount(userName);
        if (!oacc.isPresent()) {
            var newAcc = new Account(userName, 0);
            service.create(newAcc);
            oacc = Optional.of(newAcc);
        }
        var arg = new CreateSessionArg(oacc.get().getName(), this::whenSessionLoggedOut);
        currentSession = sessionFactory.create(arg);
    }
}
