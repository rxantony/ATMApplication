package com.bank.atm.domain.common.handler;

public class HandlerNotFoundException extends RuntimeException {
    public final String handlerName;

    public HandlerNotFoundException(String handlerName) {
        super(String.format("handler with request %s is not found", handlerName));
        this.handlerName = handlerName;
    }

}
