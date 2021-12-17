package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TransactionExeptionTest {
    private static Object[] args1() {
        return new Object[][] { { "account 1", "deposit", -1 }, { "account 1", "deposit", 0 },
                { "account 1", "deposit", 1000000 }, };
    };

    @ParameterizedTest
    @MethodSource("args1")
    void notEnoughAmount(String accountName, String transaction, int amount) {
        var ex = TransactionException.notEnoughAmount(accountName, transaction, amount);
        assertEquals(accountName, ex.getAccountName());
        assertEquals(amount, ex.getAmount());
        assertEquals(transaction, ex.getTransaction());
        assertEquals("01", ex.getErrorCode());
    }

    
    private static Object[] args2() {
        return new Object[][] { { null, "deposit", 200 }, { "account 1", null, 1000 }, { "", "deposit", 1000 },
                { "account 1", "", 1000 }, { "", null, 60 }, { null, "", 60 }, { null, null, 7700 }, { "", "", 800 } };
    };
    
    @ParameterizedTest
    @MethodSource("args2")
    void notEnoughAmountWithInvalidArgThrownIllegalArgumentException(String accountName, String transaction, int amount) {
        assertThrows(IllegalArgumentException.class, () -> TransactionException.notEnoughAmount(accountName, transaction, amount));
    }
}
