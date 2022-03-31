package com.bank.atm.domain.common.handler;

import com.bank.atm.domain.common.ATMException;

public interface HandlerManager {
    <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request);

    <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> TResult handle(
            TRequest request) throws TException;

    <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            Handler<TRequest, TResult> handler);

    <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> HandlerManager registerHandler(
            HandlerWithException<TRequest, TResult, TException> handler);
}
