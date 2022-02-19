package com.bank.atm.domain.common.handler;

public class HandlerIsNotFoundException extends RuntimeException {

    public HandlerIsNotFoundException(String handlerName) {
        super(String.format("handler with request %s is not found", handlerName));
    }

}
