package com.dkatalist.atm.domain.application;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dkatalist.atm.domain.ListStringMediaInput;
import com.dkatalist.atm.domain.application.facade.ATMMachine;
import com.dkatalist.atm.domain.application.facade.Session;
import com.dkatalist.atm.domain.application.facade.SessionManager;

import org.junit.jupiter.api.Test;

class ATMMachineTest {
  
    @Test
    void run(){
        var sessionInputHandler = mock(AbstractInputHandler.class);
        doNothing().when(sessionInputHandler).handle(anyString());

        var session = mock(Session.class);
        when(session.getInputHandler())
            .thenReturn(sessionInputHandler);

        var sessionMgrinputHandler = mock(AbstractInputHandler.class);
        doNothing().when(sessionMgrinputHandler).handle(anyString());

        var sessionMgr = mock(SessionManager.class);
        when(sessionMgr.getSession())
            .thenReturn(null)
            .thenReturn(session);
        when(sessionMgr.getInputHandler()).thenReturn(sessionMgrinputHandler);

        var input = new ListStringMediaInput();
        input.strings.add("login bob");
        input.strings.add("deposit 100");
        input.strings.add("logout");
        input.strings.add("exit");

        var machine = new ATMMachine(sessionMgr, input);
        machine.run();

        verify(sessionMgr).getInputHandler();

        verify(sessionMgr, times(4)).getSession();

        verify(sessionMgrinputHandler).handle(anyString());

        verify(session, times(2)).getInputHandler();

        verify(sessionInputHandler, times(2)).handle(anyString());
    }

    @Test
    void runWithoutSessionCreated(){
        var sessionInputHandler = mock(AbstractInputHandler.class);
        var session = mock(Session.class);
        var sessionMgrinputHandler = mock(AbstractInputHandler.class);
        var sessionMgr = mock(SessionManager.class);
        when(sessionMgr.getSession()).thenReturn(null);

        var input = new ListStringMediaInput();
        input.strings.add("exit");

        var machine = new ATMMachine(sessionMgr, input);
        machine.run();

        verify(sessionMgr).getSession();

        verify(sessionMgr, never()).getInputHandler();

        verify(sessionMgrinputHandler, never()).handle(anyString());

        verify(session, never()).getInputHandler();

        verify(sessionInputHandler, never()).handle(anyString());
    }
}
