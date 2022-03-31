package com.bank.atm.domain.common.handler;

import com.bank.atm.domain.common.ATMException;

public interface HandlerWithException<TRequest, TResult, TException extends ATMException> {
    TResult execute(TRequest request) throws TException;
}
 