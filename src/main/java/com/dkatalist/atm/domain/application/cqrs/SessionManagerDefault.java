package com.dkatalist.atm.domain.application.cqrs;

import java.util.Optional;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.application.CreateSessionArg;
import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.common.cqrs.handler.HandlerManager;
import com.dkatalist.atm.domain.service.cqrs.account.command.createAccount.CreateAccountRequest;
import com.dkatalist.atm.domain.service.cqrs.account.query.getAccount.GetAccountRequest;

public class SessionManagerDefault implements SessionManager {
    private HandlerManager handlerMgr;
    private Session currentSession;
    private AbstractInputHandler inputHandler;
    private ObjectFactory<CreateSessionArg, Session> sessionFactory;

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
        var oacc = handlerMgr.execute(new GetAccountRequest(userName));
        if (!oacc.isPresent()) {
            var newAcc = handlerMgr.execute(new CreateAccountRequest(userName, 0));
            oacc = Optional.of(newAcc);
        }
        var arg = new CreateSessionArg(oacc.get().getName(), this::whenSessionLoggedOut);
        currentSession = sessionFactory.create(arg);
    }
}
