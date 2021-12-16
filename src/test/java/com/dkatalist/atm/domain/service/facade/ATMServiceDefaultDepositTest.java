package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.ATMServiceDefault;
import com.dkatalist.atm.domain.service.OweCalculationService;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.TransactionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class ATMServiceDefaultDepositTest extends AbstractATMServiceDefaultTest {

    @Test
    void normalDeposit() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);

        var oweDb = mock(OweRepository.class);
        when(oweDb.getOweToList(bob))
            .thenReturn(emptyOweList);

        var oweService = mock(OweCalculationService.class);
        var service = new ATMServiceDefault(accRepo, oweDb, oweService);
        var result = service.deposit(bob, 70);
        var getOweListResult = service.getOweList(bob);

        assertEquals(bob, result.getAccountName());
        assertEquals(70, result.getAmount());
        assertEquals(150, result.getBalance());
        assertEquals(result.getBalance(), bobAcc.get().getBalance());
        assertEquals(0, getOweListResult.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void depositThanTransferAndReduceOweToLtOweTo() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 0));
        var aliceAcc = Optional.of(new Account(alice, 210));

        var bobOweTo = Optional.of(new Owe(bob, alice, -70));
        var aliceOweFrom = Optional.of(new Owe(alice, bob, 70));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweToList(bob))
            .thenReturn(Arrays.asList(bobOweTo.get()));

        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(bob, alice, -40, bobOweTo.get().getCreatedAt()));
                ioweList.add(new Owe(alice, bob, 40, aliceOweFrom.get().getCreatedAt()));
                return 30;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.deposit(bob, 30);

        assertEquals(bob, result.getAccountName());
        assertEquals(0, result.getAmount());
        assertEquals(0, result.getBalance());

        assertEquals(bob, result.getTransferList().get(0).getAccountName());
        assertEquals(alice, result.getTransferList().get(0).getRecipient());
        assertEquals(30, result.getTransferList().get(0).getAmount());

        assertEquals(bob, result.getOweList().get(0).getAccount1());
        assertEquals(alice, result.getOweList().get(0).getAccount2());
        assertEquals(-40, result.getOweList().get(0).getAmount());
        assertEquals(bobOweTo.get().getCreatedAt(), result.getOweList().get(0).getCreatedAt());

        assertEquals(alice, result.getOweList().get(1).getAccount1());
        assertEquals(bob, result.getOweList().get(1).getAccount2());
        assertEquals(40, result.getOweList().get(1).getAmount());
        assertEquals(aliceOweFrom.get().getCreatedAt(), result.getOweList().get(1).getCreatedAt());
    }

    @Test
    @SuppressWarnings("unchecked")
    void depositThanTransferAndReduceOweToEqOweTo() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 0));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var bobOweTo = Optional.of(new Owe(bob, alice, -20));
        var aliceOweFrom = Optional.of(new Owe(alice, bob, 20));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var oweRepo = mock(OweRepository.class);
        when(oweRepo.getOweToList(bob))
            .thenReturn(Arrays.asList(bobOweTo.get()));

        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(bob, alice, 0, bobOweTo.get().getCreatedAt()));
                ioweList.add(new Owe(alice, bob, 0, aliceOweFrom.get().getCreatedAt()));
                return 20;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.deposit(bob, 100);

        assertEquals(bob, result.getAccountName());
        assertEquals(80, result.getAmount());
        assertEquals(80, result.getBalance());

        assertEquals(bob, result.getTransferList().get(0).getAccountName());
        assertEquals(alice, result.getTransferList().get(0).getRecipient());
        assertEquals(20, result.getTransferList().get(0).getAmount());

        assertEquals(bob, result.getOweList().get(0).getAccount1());
        assertEquals(alice, result.getOweList().get(0).getAccount2());
        assertEquals(0, result.getOweList().get(0).getAmount());
        assertEquals(bobOweTo.get().getCreatedAt(), result.getOweList().get(0).getCreatedAt());

        assertEquals(alice, result.getOweList().get(1).getAccount1());
        assertEquals(bob, result.getOweList().get(1).getAccount2());
        assertEquals(0, result.getOweList().get(1).getAmount());
        assertEquals(aliceOweFrom.get().getCreatedAt(), result.getOweList().get(1).getCreatedAt());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void depositWithInvalidAmount(int amount) throws ServiceException {
        var service = crerateEmptyATMServive();
        assertThrows(TransactionException.class, () ->service.deposit(bob, amount));
    }

    static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void depositWithInvalidAccountName(String accountName) throws ServiceException {
        var service = crerateEmptyATMServive();
        assertThrows(IllegalArgumentException.class, () -> service.deposit(accountName, 100));
    }
}