package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.data.OweRepository;
import com.dkatalist.atm.domain.service.AccountNotExistsException;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.oweCallculation.OweCalculationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class ATMServiceDefaultWithdrawalTest extends AbstractATMServiceDefaultTest {

    @Test
    void withdraw() throws ServiceException {
        var account = Optional.of(new Account(bob, 500));
        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(account);
        
        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);
        var result = service.withdraw(bob, 100);

        assertEquals(bob, result.getAccountName());
        assertEquals(100, result.getAmount());
        assertEquals(400, result.getBalance());
    }

    @Test
    void withdrawalWithNotRegisteredAccount() throws ServiceException {
        Optional<Account> emptyAccount = Optional.empty();
        var accRepo = mock(AccountRepository.class);
        when(accRepo.get(bob)).thenReturn(emptyAccount);

        var oweRepo = mock(OweRepository.class);
        var oweService = mock(OweCalculationService.class);
        var service = new ATMServiceDefault(accRepo, oweRepo, oweService);

        assertThrows(AccountNotExistsException.class, () -> {
            service.withdraw(bob, 100);
        });
    }

    @Test
    void withdrawalExcessAmountBalance() {
        var service = crerateEmptyATMServive();
        assertThrows(TransactionException.class, () -> service.withdraw(bob, 501));
    }

    @ParameterizedTest
    @ValueSource(ints = { 01, -1 })
    void withdawalWithAmountLessThanMinThresholdAmount(int amount) {
        var service = crerateEmptyATMServive();
        assertThrows(TransactionException.class, () -> service.withdraw(bob, 0));
    }

    static Stream<String> blankStrings() {
        return Stream.of("", "   ", null);
    }

    @ParameterizedTest
    @MethodSource("blankStrings")
    void withdawalWithEMptyAcoountName(String accountName) {
        var service = crerateEmptyATMServive();
        assertThrows(IllegalArgumentException.class, () -> service.withdraw(accountName, 70));
    }
}
