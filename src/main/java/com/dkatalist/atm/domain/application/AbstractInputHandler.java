package com.dkatalist.atm.domain.application;

import java.util.stream.Stream;

public abstract class AbstractInputHandler {
    protected AbstractInputHandler(){
    }

    public void handle(String input){
        String[] args = input.replaceAll("\\s+", " ").split(" ");
		String command = args.length == 0 ? "" : args[0].toLowerCase();
        String[] params = Stream.of(args).skip(1).toArray(size -> new String[size]);
        try {
            handle(command, params);
        }
        catch(IllegalArgumentException ex){
            showError(ex);
        }
        catch(IndexOutOfBoundsException ex){
            showCommandInfo(command);
        }
    }

    protected abstract void showError(Exception ex);
    protected abstract void showCommandInfo(String command);
    protected abstract void handle(String command, String... args);
}
