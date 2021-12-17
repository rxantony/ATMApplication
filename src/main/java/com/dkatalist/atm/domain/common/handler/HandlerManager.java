package com.dkatalist.atm.domain.common.handler;

import com.dkatalist.atm.domain.common.ATMException;

public interface HandlerManager {
    public <TRequest extends Request<TResult>, TResult> TResult execute(TRequest request);

    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> TResult execute(
            TRequest request) throws TException;

    public <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            Handler<TRequest, TResult> handler);

    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> HandlerManager registerHandler(
            HandlerWithException<TRequest, TResult, TException> handler);
}
