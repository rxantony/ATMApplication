package com.dkatalist.atm.domain.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dkatalist.atm.domain.service.AccountNotExistsException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class AccountNotExistsExceptionTest {
    static final String bob = "bob";
    static final String alice = "alice";

    @ParameterizedTest
    @ValueSource(strings = { "account 1" })
    void createWithAccountNameArg(String accountName) {
        var ex = AccountNotExistsException.create(accountName);
        assertEquals("account 1", ex.getAccountName());
    }

    private static Object[] args1() {
        return new Object[][] { { bob, "message from " + bob  },  { alice,  "message from " + alice },};
    };
    @ParameterizedTest
    @MethodSource("args1")
    void createWithAccountNameAndMessageAgs(String accountName, String message) {
        var ex = AccountNotExistsException.create(accountName, message);
        assertEquals(accountName, ex.getAccountName());
        assertEquals(message, ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "     " })
    void createWithInvalidArgThrownIllegalArgumentException(String arg) {
        assertThrows(IllegalArgumentException.class, () -> AccountNotExistsException.create(arg));
    }

    private static Object[] args2() {
        return new Object[][] { { null, "" }, { "", null }, { null, null }, { "", "" }, { "", " " }, { " ", "" },{ " ", " " } };
    };
    
    @ParameterizedTest
    @MethodSource("args2")
    void createWithInvalidArgsThrownIllegalArgumentException(String arg, String message) {
        assertThrows(IllegalArgumentException.class, () -> AccountNotExistsException.create(arg, message));
    }
}
