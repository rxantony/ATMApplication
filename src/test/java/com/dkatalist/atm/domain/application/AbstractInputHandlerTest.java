package com.dkatalist.atm.domain.application;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class AbstractInputHandlerTest {
    
    @Test
    void withNullArgsThrowIllegalArgumentException(){
        var inputHandler = new AbstractInputHandler() {
            @Override
            protected void handle(String command, String... args) {
                // just do nothing here, we only need the parent logic for testing
            }
            @Override
            protected void showError(Exception ex) {
            }
            @Override
            protected void showCommandInfo(String command) {
            }
        };
        assertThrows(IllegalArgumentException.class, ()-> inputHandler.handle(null));
    }
}
