package com.dkatalist.atm.domain;

public interface InputHandler {
    void handle(String input) throws AccountNotExistsException;
}
