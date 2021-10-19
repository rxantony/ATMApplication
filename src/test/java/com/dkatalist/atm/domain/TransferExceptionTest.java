package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dkatalist.atm.domain.service.TransactionException;
import com.dkatalist.atm.domain.service.TransferException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TransferExceptionTest {
    private static Object[] args1() {
        return new Object[][] { { "account 1", "account 2", 100 }, };
    };

    private static Object[] args2() {
        return new Object[][] { { null, "account 2", 200 }, { "account 1", null, 1000 }, { "", "deposit", 1000 },
                { "account 1", "", 1000 }, { "", null, 60 }, { null, "", 60 }, { null, null, 7700 }, { "", "", 800 } };
    };

    @ParameterizedTest
    @MethodSource("args1")
    void cannotTransferToSameAccount(String source, String recipient, int amount) {
        TransferException ex = TransferException.cannotTransferToSameAccount(source, recipient, amount);
        assertEquals(source, ex.getAccountName());
        assertEquals(recipient, ex.getRecipient());
        assertEquals(amount, ex.getAmount());
        assertEquals("02", ex.getErrorCode());
    }

    @ParameterizedTest
    @MethodSource("args2")
    void notEnoughAmountWithInvalidArgThrownIllegalArgumentException(String accountName, String transaction,
            int amount) {
        assertThrows(IllegalArgumentException.class, () -> {
            TransactionException.notEnoughAmount(accountName, transaction, amount);
        });
    }
}