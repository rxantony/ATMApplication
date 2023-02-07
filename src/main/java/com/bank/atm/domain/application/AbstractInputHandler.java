package com.bank.atm.domain.application;

import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import com.bank.atm.domain.common.Guard;

/**
    AbstractInputHandler
*/
public abstract class AbstractInputHandler {
    protected AbstractInputHandler() {
    }

    public void handle(String input) {
        Guard.validateArgNotNull(input, "input");
        String[] args = input.replaceAll("\\s+", " ").split(" ");
        String command = args[0].toLowerCase();
        String[] params = Stream.of(args).skip(1).toArray(size -> new String[size]);
        try {
            handle(command, params);
        } catch (IndexOutOfBoundsException | NumberFormatException | DateTimeParseException ex) {
            showCommandInfo(command);
        } catch (IllegalArgumentException ex) {
            showError(ex);
        }
    }

    protected abstract void showError(Exception ex);

    protected abstract void showCommandInfo(String command);

    protected abstract void handle(String command, String... args);
}
