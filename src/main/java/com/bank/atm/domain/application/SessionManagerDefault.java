package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.ObjectFactory;
import com.bank.atm.domain.common.handler.HandlerManager;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountRequest;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountRequest;

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
        String accName = null;
        var result = handlerMgr.handle(new GetAccountRequest(userName));
        if (result.isPresent()) {
            accName = result.get().getName();
        }
        else {
            var newAcc = handlerMgr.handle(new CreateAccountRequest(userName, 0));
            accName = newAcc.getName();
        }
        var arg = new CreateSessionArg(accName, this::whenSessionLoggedOut);
        currentSession = sessionFactory.create(arg);
    }
}
