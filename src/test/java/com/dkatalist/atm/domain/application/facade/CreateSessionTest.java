package com.dkatalist.atm.domain.application.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Consumer;

import com.dkatalist.atm.domain.application.CreateSessionArg;

import org.junit.jupiter.api.Test;

class CreateSessionTest {

    private static Consumer<String> eventLogedout = a -> {
    };

    @Test
    void create() {
        var arg = new CreateSessionArg("account 1", eventLogedout);

        assertEquals("account 1", arg.accountName);
        assertEquals(eventLogedout, arg.eventLogout);
    }

    @Test
    void createWithNullAccountNameThrownIllegalArgumenException(){
        assertThrows(IllegalArgumentException.class, () ->new CreateSessionArg(null, eventLogedout));       
    }

    @Test
    void createWithNullEventLoggedoutThrownIllegalArgumenException(){
        assertThrows(IllegalArgumentException.class, () -> new CreateSessionArg("Account1", null));       
    }
}
