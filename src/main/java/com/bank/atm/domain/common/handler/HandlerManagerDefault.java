package com.bank.atm.domain.common.handler;

import java.util.HashMap;
import java.util.Map;

import com.bank.atm.domain.common.ATMException;

public class HandlerManagerDefault implements HandlerManager {
    private final Map<Class<?>, AbstractHandler<?, ?>> handlerStores = new HashMap<>();
    private final Map<Class<?>, AbstractHandlerWithException<?, ?, ?>> handlerWithErrStores = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request) {
        var cls = request.getClass();
        var handler = (AbstractHandler<TRequest, TResult>) handlerStores.get(cls);
        if (handler == null)
            throw new HandlerIsNotFoundException(cls.getCanonicalName());
        return handler.execute(request);
    }

    @Override
    public <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            AbstractHandler<TRequest, TResult> handler) {
        var requestCls = handler.getRequestClass();
        handlerStores.put(requestCls, handler);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> TResult handle(
            TRequest request) throws TException {
        var cls = request.getClass();
        var handler = (AbstractHandlerWithException<TRequest, TResult, TException>) handlerWithErrStores.get(cls);
        if (handler == null)
            throw new HandlerIsNotFoundException(cls.getCanonicalName());
        return handler.execute(request);
    }

    @Override
    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> HandlerManagerDefault registerHandler(
            AbstractHandlerWithException<TRequest, TResult, TException> handler) {
        var requestCls = handler.getRequestClass();
        handlerWithErrStores.put(requestCls, handler);
        return this;
    }
}
