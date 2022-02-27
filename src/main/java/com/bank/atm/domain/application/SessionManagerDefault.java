package com.bank.atm.domain.application;

import java.util.Optional;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.ObjectFactory;
import com.bank.atm.domain.common.handler.HandlerManager;
import com.bank.atm.domain.service.account.command.createAccount.CreateAccountRequest;
import com.bank.atm.domain.service.account.query.getAccount.GetAccountRequest;

public class SessionManagerDefault implements SessionManager {
    private Session currentSession;
    private final HandlerManager handlerMgr;
    private final AbstractInputHandler inputHandler;
    private final ObjectFactory<CreateSessionArg, Session> sessionFactory;

    public SessionManagerDefault(HandlerManager handlerMgr, ObjectFactory<CreateSessionArg, Session> sessionFactory,
            ObjectFactory<SessionManager, AbstractInputHandler> inputhandlerFactory) {
        Guard.validateArgNotNull(handlerMgr, "handlerMgr");
        Guard.validateArgNotNull(sessionFactory, "sessionFactory");
        Guard.validateArgNotNull(inputhandlerFactory, "inputhandlerFactory");
        this.handlerMgr = handlerMgr;
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
        var oacc = handlerMgr.handle(new GetAccountRequest(userName));
        if (!oacc.isPresent()) {
            var newAcc = handlerMgr.handle(new CreateAccountRequest(userName, 0));
            oacc = Optional.of(newAcc);
        }
        var arg = new CreateSessionArg(oacc.get().getName(), this::whenSessionLoggedOut);
        currentSession = sessionFactory.create(arg);
    }
}
