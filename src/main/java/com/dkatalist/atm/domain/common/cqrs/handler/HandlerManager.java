package com.dkatalist.atm.domain.common.cqrs.handler;

public interface HandlerManager {
    public <TRequest extends Request<TResult>, TResult> TResult execute(TRequest request);

    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends Exception> TResult execute(TRequest request) throws TException;

    public <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(Handler<TRequest, TResult> handler);

    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends Exception> HandlerManager registerHandler(HandlerWithException<TRequest, TResult, TException> handler);
}
