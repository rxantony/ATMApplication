package com.bank.atm.domain.application;

public interface AbstractInputHandlerFactory<TArg> {
    AbstractInputHandler create(TArg arg);
}
