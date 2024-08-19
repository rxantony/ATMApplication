package com.bank.atm.domain.application;

import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import com.bank.atm.domain.common.Guard;

public abstract class AbstractInputHandler {
	protected AbstractInputHandler() {
	}

	public void handle(String input) {
		Guard.validateArgNotNull(input, "input");
		var args = input.replaceAll("\\s+", " ").split(" ");
		var command = args[0].toLowerCase();
		var params = Stream.of(args).skip(1).toArray(size -> new String[size]);
		try {
			if (!handleInternal(command, params)) {
				showCommands();
			}
		} catch (IndexOutOfBoundsException | IllegalArgumentException | DateTimeParseException ex) {
			showCommandInfo(command);
		} catch (Exception ex) {
			showError(ex);
		}
	}

	public abstract void showCommands();

	protected abstract void showError(Exception ex);

	protected abstract void showCommandInfo(String command);

	protected abstract boolean handleInternal(String command, String... args) throws Exception;
}
