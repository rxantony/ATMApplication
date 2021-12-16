package com.dkatalist.atm.domain.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dkatalist.atm.domain.application.facade.SessionExpiredException;

import org.junit.jupiter.api.Test;

class SessionExpiredExceptionTest {

    @Test
    void create() {
        var ex = new SessionExpiredException();
        assertEquals("session is expired", ex.getMessage());
    }
}
