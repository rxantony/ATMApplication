package com.bank.atm.domain.common.handler;

public abstract class AbstractHandlerWithError<TRequest, TResult, TException extends Exception> {
    private final Class<TRequest> requestClass;
    
    protected AbstractHandlerWithError(Class<TRequest> requestClass){
        this.requestClass = requestClass;
    }

    public Class<TRequest> getRequestClass(){
        return requestClass;
    }
    
    public abstract TResult handle(TRequest request) throws TException;
}
 