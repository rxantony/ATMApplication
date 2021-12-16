package com.dkatalist.atm.domain.common.cqrs.handler;

public interface HandlerWithException<TRequest extends RequestWithException<TResult, TException>, TResult, TException extends Exception> {
    TResult execute(TRequest request) throws TException;
}
