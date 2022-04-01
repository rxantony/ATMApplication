package com.bank.atm.domain.common.handler;

import com.bank.atm.domain.common.ATMException;

public abstract class AbstractHandlerWithException<TRequest, TResult, TException extends ATMException> {
    private final Class<TRequest> requestClass;
    
    protected AbstractHandlerWithException(Class<TRequest> requestClass){
        this.requestClass = requestClass;
    }

    public Class<TRequest> getRequestClass(){
        return requestClass;
    }
    
    public abstract TResult execute(TRequest request) throws TException;
}
 