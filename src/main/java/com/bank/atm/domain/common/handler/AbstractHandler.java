package com.bank.atm.domain.common.handler;

public abstract class AbstractHandler<TRequest extends Request<TResult>, TResult> {
    private final Class<TRequest> requestClass;
    
    protected AbstractHandler(Class<TRequest> requestClass){
        this.requestClass = requestClass;
    }

    public Class<TRequest> getRequestClass(){
        return requestClass;
    }

    public abstract TResult handle(TRequest request);
}
