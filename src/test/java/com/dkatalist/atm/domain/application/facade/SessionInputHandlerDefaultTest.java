package com.dkatalist.atm.domain.application.facade;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import com.dkatalist.atm.domain.ListStringMediaOutput;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ATMService.DepositResult;
import com.dkatalist.atm.domain.service.ATMService.TransactionResult;
import com.dkatalist.atm.domain.service.ATMService.TransferResult;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.ServiceException;

import org.junit.jupiter.api.Test;



class SessionInputHandlerDefaultTest {
    
    final static String bob = "bob";
    final static String alice = "alice";

    @Test
    void depositTransferWithdrawLogout() throws ServiceException{
        var results = new ArrayList<String>();
        results.add(String.format("Your balance is $%d", 100));
        results.add(String.format("Goodbye, %s!", alice));
        results.add(String.format("Your balance is $%d", 80));
        results.add(String.format("Transferred $%d to %s", 50, alice));
        results.add(String.format("Your balance is $%s", 30));
        results.add(String.format("Transferred $%d to %s", 30, alice));
        results.add(String.format("Your balance is $%d", 0));
        results.add(String.format("Owed $%d to %s", 70, alice));
        results.add(String.format("Transferred $%d to %s", 30, alice));
        results.add(String.format("Your balance is $%d", 0));
        results.add(String.format("Owed $%d to %s", 40, alice));
        results.add(String.format("Goodbye, %s!", bob));
        results.add(String.format("Your balance is $%d", 210));
        results.add(String.format("Owed $%d from %s", 10, bob));
        results.add(String.format("Your balance is $%d", 200));
        results.add(String.format("Goodbye, %s!", alice));

        var session = mock(Session.class);
        when(session.getAccountName())
            .thenReturn(alice)
            .thenReturn(alice)
            .thenReturn(bob)
            .thenReturn(bob)
            .thenReturn(bob)
            .thenReturn(bob)
            .thenReturn(bob)
            .thenReturn(alice)
            .thenReturn(alice)
            .thenReturn(alice);

        when(session.deposit(anyInt()))
            .thenReturn(new DepositResult(alice, 100, 100))
            .thenReturn(new DepositResult(bob, 80, 80))
            .thenReturn(new DepositResult(bob, 0, 0, 
                Arrays.asList(new TransferResult(bob, alice, 30, 0, Arrays.asList(new Owe(bob, alice, -40))))));

        when(session.transfer(anyString(), anyInt()))
            .thenReturn(new TransferResult(bob, alice, 50, 30))
            .thenReturn(new TransferResult(bob, alice, 30, 0, 
                Arrays.asList(new Owe(bob, alice, -70), new Owe(alice, bob, 70))))
            .thenReturn(new TransferResult(alice, bob, 0, 210, Arrays.asList(new Owe(alice, bob, 10))));

        when(session.withdraw(anyInt()))
            .thenReturn(new TransactionResult(alice, 10, 200));

        doNothing().when(session).logout();

        var output = new ListStringMediaOutput();

        var inputHandler = new SessionInputHandlerDefault(session, output);
        inputHandler.handle("deposit 100");
        inputHandler.handle("logout");

        inputHandler = new SessionInputHandlerDefault(session, output);
        inputHandler.handle("deposit 80");
        inputHandler.handle(String.format("transfer %s 50", alice));
        inputHandler.handle(String.format("transfer %s 100", alice));
        inputHandler.handle("deposit 30");
        inputHandler.handle("logout");

        inputHandler.handle("transfer bob 30");
        inputHandler.handle("withdraw 10");
        inputHandler.handle("logout");

        assertLinesMatch(results, output.strings);
    }

    @Test
    void depositWithZeroOweValue() throws ServiceException{
        var results = new ArrayList<String>();
        //alice
        results.add("Your balance is $30");
        results.add("Transferred $30 to bob");
        results.add("Your balance is $0");
        results.add("Owed $30 to bob");
        //bob
        results.add("Your balance is $150");
        results.add("Your balance is $150");
        results.add("Owed $10 from bob");
        //alice
        results.add("Your balance is $30");

        var session = mock(Session.class);
        when(session.getAccountName())
            .thenReturn(alice)
            .thenReturn(alice)
            .thenReturn(bob)
            .thenReturn(bob)
            .thenReturn(alice);

        when(session.deposit(anyInt()))
            .thenReturn(new DepositResult(alice, 30, 30))
            .thenReturn(new DepositResult(bob, 20, 150))
            .thenReturn(new DepositResult(alice, 30, 30, 
                Arrays.asList(new TransferResult(alice, bob, 0, 30,
                    Arrays.asList(new Owe(alice, bob, 0), new Owe(bob, alice, 0))))));                

        when(session.transfer(anyString(), anyInt()))
            .thenReturn(new TransferResult(alice, bob, 30, 0, 
                 Arrays.asList(new Owe(alice, bob, -30), new Owe(bob, alice, 30))))
            .thenReturn(new TransferResult(alice, bob, 0, 150, 
                 Arrays.asList(new Owe(alice, bob, 10), new Owe(bob, alice, -10))));

        var output = new ListStringMediaOutput();

        var inputHandler = new SessionInputHandlerDefault(session, output);
        inputHandler.handle("deposit 30");
        inputHandler.handle("transfer bob 60");

        inputHandler.handle("deposit 20");
        inputHandler.handle("transfer alice 20");


        inputHandler.handle("deposit 40");

        assertLinesMatch(results, output.strings);
    }

    @Test
    void showAvailableCommands(){
        var results = new ArrayList<String>();
        results.add("Available commands:");
        results.add("- help");
        results.add("- exit");
        results.add("- logout");
        results.add("- transfer [string accountName], [int amount]");
        results.add("- deposit [int amount]");
        results.add("- withdraw [int amount]");

        var session = mock(Session.class);
        ListStringMediaOutput output = new ListStringMediaOutput();

        var inputHandler = new SessionInputHandlerDefault(session, output);
        inputHandler.handle("goto alice bob");
        inputHandler.handle("GoPay alice 7");
        inputHandler.handle("GOFOOD 7 bob");
        
        assertLinesMatch(results, output.strings.subList(0, 7));
        assertLinesMatch(results, output.strings.subList(7, 14));
        assertLinesMatch(results, output.strings.subList(14, 21));
    }

    @Test
    void showError(){
        var results = new ArrayList<String>();
        /*results.add("transfer [string accountName], [int amount]");
        results.add("For input string: \"alice\"");
        results.add("deposit [int amount]");
        results.add("For input string: \"alice\"");
        results.add("withdraw [int amount]");
        results.add("For input string: \"bob\"");*/

        results.add("transfer [string accountName], [int amount]");
        results.add("transfer [string accountName], [int amount]");
        results.add("deposit [int amount]");
        results.add("deposit [int amount]");
        results.add("withdraw [int amount]");
        results.add("withdraw [int amount]");

        var session = mock(Session.class);
        var output = new ListStringMediaOutput();
        var inputHandler = new SessionInputHandlerDefault(session, output);

        inputHandler.handle("transfer 100");
        inputHandler.handle("transfer 100 alice");
        inputHandler.handle("deposit");
        inputHandler.handle("deposit alice");
        inputHandler.handle("withdraw");
        inputHandler.handle("withdraw bob");

        assertLinesMatch(results, output.strings);
    }

    @Test
    void throwAccountNotExistsException() throws ServiceException{
        var results = new ArrayList<String>();
        results.add(String.format("account with name %s is not exists", bob));
        results.add(String.format("account with name %s is not exists", bob));
        results.add(String.format("account with name %s is not exists", bob));
        results.add(String.format("account with name %s is not exists", alice));
        results.add(String.format("account with name %s is not exists", alice));
        results.add(String.format("account with name %s is not exists", alice));

        var session = mock(Session.class);
        when(session.deposit(anyInt()))
            .thenThrow(AccountNotExistsException.create(bob))
            .thenThrow(AccountNotExistsException.create(alice));
        when(session.transfer(anyString(), anyInt()))    
            .thenThrow(AccountNotExistsException.create(bob))
            .thenThrow(AccountNotExistsException.create(alice));
        when(session.withdraw(anyInt()))      
            .thenThrow(AccountNotExistsException.create(bob))
            .thenThrow(AccountNotExistsException.create(alice));

        var output = new ListStringMediaOutput();
        var inputHandler = new SessionInputHandlerDefault(session, output);

        inputHandler.handle("deposit 100");
        inputHandler.handle("transfer alice 30");
        inputHandler.handle("withdraw 40");

        inputHandler.handle("deposit 700");
        inputHandler.handle("transfer alice 210");
        inputHandler.handle("withdraw 100");

        assertLinesMatch(results, output.strings);
    }
}
