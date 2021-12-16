package com.dkatalist.atm.domain.application.facade;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import com.dkatalist.atm.domain.ListStringMediaOutput;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.ServiceException;

import org.junit.jupiter.api.Test;

class SessionManagerInputHandlerDefaultTest {
    String bob = "bob";
    String alice = "alice";

    @Test
    void login() throws ServiceException{
        var results = new ArrayList<String>();
        results.add("Hello, alice!");
        results.add("Your balance is $7000");
        results.add("Owed 10 from bob");
        results.add("Owed 10 to alice");

        var account = new Account(bob, 7000);
        var oweList = Arrays.asList(new Owe(alice, bob, 10), new Owe(bob, alice, -10), new Owe(alice, "someone", 0), new Owe("someone", alice, 0));
        
        var session = mock(Session.class);
        when(session.getAccountName()).thenReturn(bob);
        when(session.getAccount()).thenReturn(account);
        when(session.getOweList()).thenReturn(oweList);

        var sessionMgr = mock(SessionManager.class);
        doNothing().when(sessionMgr).login(anyString());
        when(sessionMgr.getSession()).thenReturn(session);

        var output = new ListStringMediaOutput();
        var inputHandler = new SessionManagerInputHandlerDefault(sessionMgr, output);
        inputHandler.handle("login alice");

        assertLinesMatch(results, output.strings);
    }

    @Test
    void showAvailableCommands(){
        var results = new ArrayList<String>();
        results.add("Available commands:");
        results.add("- help");
        results.add("- exit");
        results.add("- login [string accountName]");

        var output = new ListStringMediaOutput();
        var sessionMgr = mock(SessionManager.class);
        var inputHandler = new SessionManagerInputHandlerDefault(sessionMgr, output);

        inputHandler.handle("goto alice bob");
        inputHandler.handle("GoPay alice 7");
        inputHandler.handle("GOFOOD 7 bob");

        assertLinesMatch(results, output.strings.subList(0, 4));
        assertLinesMatch(results, output.strings.subList(4, 8));
        assertLinesMatch(results, output.strings.subList(8, 12));
    }


    @Test
    void showCommandInfo(){
        var results = new ArrayList<String>();
        results.add("login [string accountName]");

        var output = new ListStringMediaOutput();
        var sessionMgr = mock(SessionManager.class);
        var inputHandler = new SessionManagerInputHandlerDefault(sessionMgr, output);

        inputHandler.handle("login");

        assertLinesMatch(results, output.strings);
    }

    @Test
    void getAccountAndthrowAccountNotExistsException() throws ServiceException{
        var results = new ArrayList<String>();
        results.add("Hello, bob!");
        results.add(String.format("account with name %s is not exists", bob));
        results.add("Hello, alice!");
        results.add(String.format("account with name %s is not exists", alice));

        var session = mock(Session.class);
        when(session.getAccount())
            .thenThrow(AccountNotExistsException.create(bob))
            .thenThrow(AccountNotExistsException.create(alice));

        var sessionMgr = mock(SessionManager.class);
        doNothing().when(sessionMgr).login(anyString());
        when(sessionMgr.getSession()).thenReturn(session);
        
        var output = new ListStringMediaOutput();

        var inputHandler = new SessionManagerInputHandlerDefault(sessionMgr, output);

        inputHandler.handle("login bob");
        inputHandler.handle("login alice");

        assertLinesMatch(results, output.strings);
    }
}
