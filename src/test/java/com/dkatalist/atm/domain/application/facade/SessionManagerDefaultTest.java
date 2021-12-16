package com.dkatalist.atm.domain.application.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.application.CreateSessionArg;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.service.AccountService;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class SessionManagerDefaultTest{
    static final String bob = "bob";

    @Test
    @SuppressWarnings("unchecked")
    void loginGetSessionAndGetInputHandler(){
        var service = mock(AccountService.class);
        when(service.getAccount(bob)).thenReturn(Optional.of(new Account(bob, 100)));

        var session = mock(Session.class);
        var inputHandler = mock(AbstractInputHandler.class);
        ObjectFactory<CreateSessionArg, Session> sessionFactory = arg -> session;

        ObjectFactory<SessionManager,AbstractInputHandler> inputhandlerFactory = mock(ObjectFactory.class);
        when(inputhandlerFactory.create(any())).thenReturn(inputHandler);

        var sessionMgr = new SessionManagerDefault(service, sessionFactory, inputhandlerFactory);
        var session2 = sessionMgr.getSession();
        sessionMgr.login(bob);
        var session3 = sessionMgr.getSession();
        var inputHandler2 =sessionMgr.getInputHandler();

        assertNull(session2);
        assertNotNull(session3);
        assertNotNull(inputHandler2);
    }

    @Test
    @SuppressWarnings("unchecked")
    void loginCreateNewUserGetSessionGetInputHandlerAndLogout(){
        var service = mock(AccountService.class);
        List<Account> newAccounts = new ArrayList<>();
        when(service.create(any())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                newAccounts.add((Account)args[0]);
                return true;
            }
        });
        when(service.getAccount(bob))
            .thenReturn(Optional.empty())
            .thenReturn(Optional.of(new Account(bob, 100)));

        var session = mock(Session.class);
        var inputHandler = mock(AbstractInputHandler.class);
        ObjectFactory<CreateSessionArg, Session> sessionFactory = new ObjectFactory<CreateSessionArg,Session>() {
            @Override
            public Session create(CreateSessionArg arg) {
                doAnswer(new Answer<Void>() {
                    @Override
                    public Void answer(InvocationOnMock arg0) throws Throwable {
                        arg.eventLogout.accept(bob);
                        return null;
                    }
                }).when(session).logout();
                return session;
            }
        };

        ObjectFactory<SessionManager,AbstractInputHandler> inputhandlerFactory = mock(ObjectFactory.class);
        when(inputhandlerFactory.create(any())).thenReturn(inputHandler);

        var sessionMgr = new SessionManagerDefault(service, sessionFactory, inputhandlerFactory);
        var session2 = sessionMgr.getSession();

        sessionMgr.login(bob);

        var session3 = sessionMgr.getSession();
        var inputHandler2 = sessionMgr.getInputHandler();

        session3.logout();
        var session4 = sessionMgr.getSession();

        assertNull(session2);
        assertNotNull(session3);
        assertNotNull(inputHandler2);
        assertEquals(1, newAccounts.size());
        assertNull(session4);
    }
}
