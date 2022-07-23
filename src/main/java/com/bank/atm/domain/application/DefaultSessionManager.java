package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.HandlerManager;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountRequest;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountRequest;

public class DefaultSessionManager implements SessionManager {
    private Session currentSession;
    private final HandlerManager handlerMgr;
    private final AbstractInputHandler inputHandler;
    private final SessionFactory sessionFactory;

    public  DefaultSessionManager(HandlerManager handlerMgr, SessionFactory sessionFactory,
            AbstractInputHandlerFactory<SessionManager> inputhandlerFactory) {
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

    @Override
    public Session getSession() {
        return currentSession;
    }

    @Override
    public AbstractInputHandler getInputHandler() {
        return inputHandler;
    }

    @Override
    public Session login(String userName) {
        String accName = null;
        var result = handlerMgr.handle(new GetAccountRequest(userName));
        if (result.isPresent()) {
            accName = result.get().getName();
        }
        else {
            var newAcc = handlerMgr.handle(new CreateAccountRequest(userName, 0));
            accName = newAcc.getName();
        }
        currentSession = sessionFactory.create(accName, this::whenSessionLoggedOut);
        return currentSession;
    }

    @Override
    public void close() throws Exception {
        if(currentSession != null)
            currentSession.close();
    }
}
