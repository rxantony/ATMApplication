package com.bank.atm.domain.common.handler;

public interface Handler<TRequest extends Request<TResult>, TResult> {
    TResult execute(TRequest request);
}
