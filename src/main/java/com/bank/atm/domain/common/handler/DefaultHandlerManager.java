package com.bank.atm.domain.common.handler;

import java.util.HashMap;
import java.util.Map;

public class DefaultHandlerManager implements HandlerManager {
    private final Map<Class<?>, AbstractHandler<?, ?>> handlerStores = new HashMap<>();
    private final Map<Class<?>, AbstractHandlerWithError<?, ?, ?>> handlerWithErrStores = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request) {
        var cls = request.getClass();
        var handler = (AbstractHandler<TRequest, TResult>) handlerStores.get(cls);
        if (handler == null)
            throw new HandlerNotFoundException(cls.getCanonicalName());
        return handler.handle(request);
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
    public <TRequest extends RequestWithError<TResult, TException>, TResult, TException extends Exception> TResult handle(
            TRequest request) throws TException {
        var cls = request.getClass();
        var handler = (AbstractHandlerWithError<TRequest, TResult, TException>) handlerWithErrStores.get(cls);
        if (handler == null)
            throw new HandlerNotFoundException(cls.getCanonicalName());
        return handler.handle(request);
    }

    @Override
    public <TRequest extends RequestWithError<TResult, TException>, TResult, TException extends Exception> DefaultHandlerManager registerHandler(
            AbstractHandlerWithError<TRequest, TResult, TException> handler) {
        var requestCls = handler.getRequestClass();
        handlerWithErrStores.put(requestCls, handler);
        return this;
    }
}
