package com.dkatalist.atm.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.dkatalist.atm.domain.application.SessionExpiredException;

import org.junit.jupiter.api.Test;

class SessionExpiredExceptionTest {

    @Test
    void create() {
        SessionExpiredException ex = new SessionExpiredException();
        assertEquals("session is expired", ex.getMessage());
    }
}
