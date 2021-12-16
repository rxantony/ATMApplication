package com.dkatalist.atm.domain.application.facade;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Consumer;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ATMService;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.AccountService;
import com.dkatalist.atm.domain.service.ServiceException;

import org.junit.jupiter.api.Test;

class SessionDefaultTest {
    static final String bob = "bob";
    static final String alice = "alice";

    @Test
    @SuppressWarnings("unchecked")
    void getAccountNameGetAccountGetInputhandlerLogoutAndGetAccountNameThanThrowSessionExpiredException()
            throws AccountNotExistsException {
        var atmService = mock(ATMService.class);

        var accountService = mock(AccountService.class);
        when(accountService.getAccount(bob)).thenReturn(Optional.of(new Account(bob, 0)));

        var inputHandler = mock(AbstractInputHandler.class);
        ObjectFactory<Session, AbstractInputHandler> inputhandlerFactory = mock(ObjectFactory.class);
        when(inputhandlerFactory.create(any())).thenReturn(inputHandler);

        var logoutCalled = new ArrayList<Boolean>();
        Consumer<String> eventLogout = name -> {
            logoutCalled.add(true);
        };
        var session = new SessionDefault(bob, atmService, accountService, eventLogout, inputhandlerFactory);

        var accName = session.getAccountName();
        var account = session.getAccount();
        var inputHandler2 = session.getInputHandler();
        session.logout();

        assertNotNull(accName);
        assertFalse(accName.isEmpty());
        assertNotNull(account);
        assertNotNull(inputHandler2);
        assertTrue(logoutCalled.get(0));
        assertThrows(SessionExpiredException.class, () -> session.getAccountName());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAccountNameGetAccountAndAutomaticallyLogoutThanThrowAccountNotExistsException()
            throws AccountNotExistsException {
        var atmService = mock(ATMService.class);
        Optional<Account> emptyAccount = Optional.empty();

        var accountService = mock(AccountService.class);
        when(accountService.getAccount(bob)).thenReturn(emptyAccount);
        ObjectFactory<Session, AbstractInputHandler> inputhandlerFactory = mock(ObjectFactory.class);

        var logoutCalled = new ArrayList<Boolean>();
        Consumer<String> eventLogout = name -> {
            logoutCalled.add(true);
        };

        var session = new SessionDefault(bob, atmService, accountService, eventLogout, inputhandlerFactory);
        var accName = session.getAccountName();

        assertThrows(AccountNotExistsException.class, () -> session.getAccount());
        assertTrue(logoutCalled.get(0));
        assertNotNull(accName);
    }

    @Test
    @SuppressWarnings("unchecked")
    void depositTransferWithdrawalGetOweListAndLogoutBeforeAfterLogout() throws ServiceException {
        var depositResult = new ATMService.DepositResult(bob);
        var transferResult = new ATMService.TransferResult(bob, alice);
        var transactionResult = new ATMService.TransactionResult(bob);

        var atmService = mock(ATMService.class);
        when(atmService.deposit(anyString(), anyInt())).thenReturn(depositResult);
        when(atmService.transfer(anyString(), anyString(), anyInt())).thenReturn(transferResult);
        when(atmService.withdraw(anyString(), anyInt())).thenReturn(transactionResult);
        when(atmService.getOweList(anyString())).thenReturn(new ArrayList<Owe>());

        var accountService = mock(AccountService.class);
        ObjectFactory<Session, AbstractInputHandler> inputhandlerFactory = mock(ObjectFactory.class);
        Consumer<String> eventLogout = name -> {
        };

        var session = new SessionDefault(bob, atmService, accountService, eventLogout, inputhandlerFactory);
        var depositResult2 = session.deposit(100);
        var transferResult2 = session.transfer(alice, 80);
        var transactionResult2 = session.withdraw(700);
        var oweList = session.getOweList();

        session.logout();

        assertNotNull(depositResult2);
        assertNotNull(transferResult2);
        assertNotNull(transactionResult2);
        assertNotNull(oweList);

        assertThrows(SessionExpiredException.class, () -> session.deposit(100));
        assertThrows(SessionExpiredException.class, () -> session.transfer(alice, 80));
        assertThrows(SessionExpiredException.class, () -> session.withdraw(700));
        assertThrows(SessionExpiredException.class, () -> session.logout());
    }
}
