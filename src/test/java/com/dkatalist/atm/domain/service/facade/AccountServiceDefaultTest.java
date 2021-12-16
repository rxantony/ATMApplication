package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.service.AccountServiceDefault;

import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

class AccountServiceDefaultTest {
    final static String bob = "bob";
    final static String alice = "alice";

    @Test
    void createAccount() {
        var bobAcc = new Account(bob, 1000);
        Optional<Account> emptyAccount = Optional.empty();

        var repo = mock(AccountRepository.class);
        when(repo.get(bob))
            .thenReturn(emptyAccount)
            .thenReturn(Optional.of(bobAcc))
            .thenReturn(Optional.of(bobAcc));

        var service = new AccountServiceDefault(repo);
        var createdResult1 = service.create(bobAcc);
        var createdResult2 = service.create(bobAcc);
        var getBobResult = service.getAccount(bob);

        assertTrue(createdResult1);
        assertFalse(createdResult2);
        assertNotNull(getBobResult);
        assertEquals(bob, getBobResult.get().getName());
        assertEquals(1000, getBobResult.get().getBalance());
    }

    @Test
    void getAccountThanAutomaticallyTriggerCreateAccountWhenInokeCreateReturnFalse() {
        Optional<Account> emptyAccount = Optional.empty();

        var oweList = new ArrayList<Account>();
        var repo = mock(AccountRepository.class);
        when(repo.get(bob))
            .thenReturn(emptyAccount)
            .thenAnswer(new Answer<Optional<Account>>() {
                @Override
                public Optional<Account> answer(InvocationOnMock arg) throws Throwable {
                    return Optional.of(oweList.get(0));
                }
            });
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock arg0) throws Throwable {
                Object[] args = arg0.getArguments();
                oweList.add((Account)args[0]);
                return null;
            }
        }).when(repo).add(any());

        var service = new AccountServiceDefault(repo);
        var getBobResult = service.getAccount(bob);
        var createdResult = service.create(new Account(bob, 100));

        assertNotNull(getBobResult);
        assertFalse(createdResult);
        assertEquals(0, getBobResult.get().getBalance());
    }

    @Test
    void createServieWithNullDb() {
        assertThrows(IllegalArgumentException.class, () -> new AccountServiceDefault(null));
    }

}
