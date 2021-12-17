package com.dkatalist.atm.domain.common.handler;

import com.dkatalist.atm.domain.common.ATMException;

public interface HandlerWithException<TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> {
    TResult execute(TRequest request) throws TException;
}
