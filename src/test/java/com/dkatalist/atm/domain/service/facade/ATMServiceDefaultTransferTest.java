package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

class ATMServiceDefaultTransferTest extends AbstractATMServiceDefaultTest {

    @Test
    void transferLessThanBalance() throws ServiceException {
        transferWithNoOweTo(70, 70, 10, 170);
    }

    @Test
    void transferEqualsToBalance() throws ServiceException {
        transferWithNoOweTo(80, 80, 0, 180);
    }
    
    private void transferWithNoOweTo(int amount, int resultAmount, int resultBalance, int aliceBalance) throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenReturn(amount);

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.transfer(bob, alice, amount);

        assertEquals(bob, result.getAccountName());
        assertEquals(alice, result.getRecipient());

        assertEquals(resultAmount, result.getAmount());
        assertEquals(resultBalance, result.getBalance());

        assertEquals(result.getBalance(), bobAcc.get().getBalance());
        assertEquals(aliceBalance, aliceAcc.get().getBalance());
    }

    @Test
    @SuppressWarnings("unchecked")
    void transferThanReduceLtOweFrom() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var bobOweFrom = Optional.of(new Owe(bob, alice, 80));
        var aliceOweTo = Optional.of(new Owe(alice, bob, -80));

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(alice, bob, -30, bobOweFrom.get().getCreatedAt()));
                ioweList.add(new Owe(bob, alice, 30, aliceOweTo.get().getCreatedAt()));
                return 0;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.transfer(bob, alice, 50);

        assertEquals(bob, result.getAccountName());
        assertEquals(0, result.getAmount());
        assertEquals(80, result.getBalance());

        assertEquals(alice, result.getOweList().get(0).getAccount1());
        assertEquals(bob, result.getOweList().get(0).getAccount2());
        assertEquals(-30, result.getOweList().get(0).getAmount());
        assertEquals(aliceOweTo.get().getCreatedAt(), result.getOweList().get(0).getCreatedAt());

        assertEquals(bob, result.getOweList().get(1).getAccount1());
        assertEquals(alice, result.getOweList().get(1).getAccount2());
        assertEquals(30, result.getOweList().get(1).getAmount());
        assertEquals(aliceOweTo.get().getCreatedAt(), result.getOweList().get(1).getCreatedAt());
    }

    @Test
    @SuppressWarnings("unchecked")
    void transferThanReduceGteOweFrom() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var bobOweFrom = Optional.of(new Owe(bob, alice, 20));
        var aliceOweTo = Optional.of(new Owe(alice, bob, -20));

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(alice, bob, 0, bobOweFrom.get().getCreatedAt()));
                ioweList.add(new Owe(bob, alice, 0, aliceOweTo.get().getCreatedAt()));
                return 80;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.transfer(bob, alice, 100);

        assertEquals(bob, result.getAccountName());
        assertEquals(80, result.getAmount());
        assertEquals(0, result.getBalance());

        assertEquals(alice, result.getOweList().get(0).getAccount1());
        assertEquals(bob, result.getOweList().get(0).getAccount2());
        assertEquals(0, result.getOweList().get(0).getAmount());
        assertEquals(aliceOweTo.get().getCreatedAt(), result.getOweList().get(0).getCreatedAt());

        assertEquals(bob, result.getOweList().get(1).getAccount1());
        assertEquals(alice, result.getOweList().get(1).getAccount2());
        assertEquals(0, result.getOweList().get(1).getAmount());
        assertEquals(aliceOweTo.get().getCreatedAt(), result.getOweList().get(1).getCreatedAt());
    }

    @Test
    @SuppressWarnings("unchecked")
    void transferExcessBalanceWithouOweThanRequestOweTo() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(bob, alice, -20));
                ioweList.add(new Owe(alice, bob, 20));
                return 80;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.transfer(bob, alice, 100);

        assertEquals(bob, result.getAccountName());
        assertEquals(80, result.getAmount());
        assertEquals(0, result.getBalance());

        assertEquals(bob, result.getOweList().get(0).getAccount1());
        assertEquals(alice, result.getOweList().get(0).getAccount2());
        assertEquals(-20, result.getOweList().get(0).getAmount());

        assertEquals(alice, result.getOweList().get(1).getAccount1());
        assertEquals(bob, result.getOweList().get(1).getAccount2());
        assertEquals(20, result.getOweList().get(1).getAmount());
    }

    @Test
    @SuppressWarnings("unchecked")
    void transferExcessBalanceWithCurrentOweThanRequestOweTo() throws ServiceException {
        var bobAcc = Optional.of(new Account(bob, 80));
        var aliceAcc = Optional.of(new Account(alice, 100));

        var bobOweFrom = Optional.of(new Owe(bob, alice, -20));
        var aliceOweTo = Optional.of(new Owe(alice, bob, 20));

        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(bobAcc);
        when(accRepo.get(alice)).thenReturn(aliceAcc);

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        when(oweService.calculate(any(), any(), anyInt(), anyList())).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock arg) throws Throwable {
                Object[] args = arg.getArguments();
                var ioweList = (List<Owe>) args[3];
                ioweList.add(new Owe(bob, alice, -40, bobOweFrom.get().getCreatedAt()));
                ioweList.add(new Owe(alice, bob, 40, aliceOweTo.get().getCreatedAt()));
                return 80;
            }
        });

        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.transfer(bob, alice, 100);

        assertEquals(bob, result.getAccountName());
        assertEquals(80, result.getAmount());
        assertEquals(0, result.getBalance());

        assertEquals(bob, result.getOweList().get(0).getAccount1());
        assertEquals(alice, result.getOweList().get(0).getAccount2());
        assertEquals(-40, result.getOweList().get(0).getAmount());
        assertEquals(bobOweFrom.get().getCreatedAt(), result.getOweList().get(0).getCreatedAt());

        assertEquals(alice, result.getOweList().get(1).getAccount1());
        assertEquals(bob, result.getOweList().get(1).getAccount2());
        assertEquals(40, result.getOweList().get(1).getAmount());
        assertEquals(aliceOweTo.get().getCreatedAt(), result.getOweList().get(1).getCreatedAt());
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, -1 })
    void TransferWithAmountLessThanThreasholdAmount(int amount) {
        var service = crerateEmptyATMServive();
        assertThrows(TransactionException.class, () -> service.transfer(bob, alice, amount));
    }

    static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void transferlWithEMptyAcoountName(String accountName) {
        var service = crerateEmptyATMServive();
        assertThrows(IllegalArgumentException.class, () -> service.deposit(accountName, 100));
    }

    @Test
    void TransferIntoSameAccount() {
        var service = crerateEmptyATMServive();
        assertThrows(TransactionException.class, () -> service.transfer(bob, bob, 700));
        assertThrows(TransactionException.class, () -> service.transfer(alice, alice, 700));
    }
}
