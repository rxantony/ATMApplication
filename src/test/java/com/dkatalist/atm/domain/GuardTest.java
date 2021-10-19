package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.dkatalist.atm.domain.common.Guard;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class GuardTest {
    private static Object[] args1() {
        return new Object[][] { { new Object(), "name1" }, { new Object(), null } };
    };

    @ParameterizedTest
    @MethodSource("args1")
    void validateArgNotThrownException(Object arg, String argName) {
        Guard.validateArgNotNull(arg, argName);
    }

    private static Object[] args2() {
        return new Object[][] { { null, "name1" }, { null, null } };
    };

    @ParameterizedTest
    @MethodSource("args2")
    void validateArgThrownIllegalArgumentException(Object arg, String argName) {
        assertThrows(IllegalArgumentException.class, () -> {
            Guard.validateArgNotNull(arg, argName);
        });
    }

    private static Object[] args3() {
        return new Object[][] { {"account 1", "accountName" }, { "account 2", "" }, { "account 2", "  " }, { "account 3", null } };
    };

    @ParameterizedTest
    @MethodSource("args3")
    void validateArgNotNullOrEmptyNotThrownException(String arg, String argName) {
        Guard.validateArgNotNullOrEmpty(arg, argName);
    }

    private static Object[] args4() {
        return new Object[][] { {"", "" }, { "", "  " }, {null, "" }, { null, "  " },  { null, null }};
    };

    @ParameterizedTest
    @MethodSource("args4")
    void validateArgNotNullOrEmptyThrownIllegalArgumentException(String arg, String argName) {
        assertThrows(IllegalArgumentException.class, () -> {
            Guard.validateArgNotNullOrEmpty(arg, argName);
        });
    }

    private static Object[] args5() {
        return new Object[][] { {1, 0 , "amount"}, {1, 0 , ""}, {1, 0 , null}};
    };

    @ParameterizedTest
    @MethodSource("args5")
    void validateArgMustBeGreaterThanNotThrownException(int arg, int reff, String argName) {
        Guard.validateArgMustBeGreaterThan(arg, reff, argName);
    }

    private static Object[] args6() {
        return new Object[][] { {9, 10 , "amount"}, {0, 10 , ""}, {-1, 10 , null}};
    };

    @ParameterizedTest
    @MethodSource("args6")
    void validateArgMustBeGreaterThanThrownIllegalArgumentException(int arg, int reff, String argName) {
        assertThrows(IllegalArgumentException.class, () -> {
            Guard.validateArgMustBeGreaterThan(arg, reff, argName);
        });
    }
}
