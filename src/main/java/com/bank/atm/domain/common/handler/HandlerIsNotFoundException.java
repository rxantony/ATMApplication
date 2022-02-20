package com.bank.atm.domain.common.handler;

public class HandlerIsNotFoundException extends RuntimeException {
    public final String handlerName;

    public HandlerIsNotFoundException(String handlerName) {
        super(String.format("handler with request %s is not found", handlerName));
        this.handlerName = handlerName;
    }

}
