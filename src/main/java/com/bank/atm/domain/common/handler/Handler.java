package com.bank.atm.domain.common.handler;

public interface Handler<TRequest, TResult> {
    TResult execute(TRequest request);
}
