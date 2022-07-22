package com.bank.atm.domain.common.handler;

public interface HandlerManager {
    <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request);

    <TRequest extends RequestWithError<TResult, TException>, TResult, TException extends Exception> TResult handle(
            TRequest request) throws TException;

    <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            AbstractHandler<TRequest, TResult> handler);

    <TRequest extends RequestWithError<TResult, TException>, TResult, TException extends Exception> HandlerManager registerHandler(
            AbstractHandlerWithError<TRequest, TResult, TException> handler);
}
