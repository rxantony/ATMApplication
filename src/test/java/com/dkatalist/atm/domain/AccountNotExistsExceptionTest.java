package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dkatalist.atm.domain.service.AccountNotExistsException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AccountNotExistsExceptionTest {

    @ParameterizedTest
    @ValueSource(strings = { "account 1" })
    void create(String accountName) {
        AccountNotExistsException ex = AccountNotExistsException.create(accountName);
        assertEquals("account 1", ex.getAccountName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "     " })
    void createWithInvalidArgThrownIllegalArgumentException(String arg) {
        assertThrows(IllegalArgumentException.class, () -> {
            AccountNotExistsException.create(arg);
        });
    }
}
