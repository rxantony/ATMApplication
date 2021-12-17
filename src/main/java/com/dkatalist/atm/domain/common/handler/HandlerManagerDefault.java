package com.dkatalist.atm.domain.common.handler;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.dkatalist.atm.domain.common.ATMException;

public class HandlerManagerDefault implements HandlerManager {
    private final Map<Class<?>, Handler<?, ?>> handlerStores = new HashMap<>();
    private final Map<Class<?>, HandlerWithException<?, ?, ?>> handlerWithErrStores = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResult>, TResult> TResult execute(TRequest request) {
        var cls = request.getClass();
        var handler = (Handler<TRequest, TResult>) handlerStores.get(cls);
        if (handler == null)
            throw new HandlerIsNotFoundException(cls.getCanonicalName());
        return handler.execute(request);
    }

    @Override
    public <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            Handler<TRequest, TResult> handler) {
        var cls = handler.getClass();
        var type = getRequestClassFrom(cls);
        handlerStores.put(type, handler);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> TResult execute(
            TRequest request) throws TException {
        var cls = request.getClass();
        var handler = (HandlerWithException<TRequest, TResult, TException>) handlerWithErrStores.get(cls);
        if (handler == null)
            throw new HandlerIsNotFoundException(cls.getCanonicalName());
        return handler.execute(request);
    }

    @Override
    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends ATMException> HandlerManagerDefault registerHandler(
            HandlerWithException<TRequest, TResult, TException> handler) {
        var cls = handler.getClass();
        var type = getRequestClassFrom(cls);
        handlerWithErrStores.put(type, handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static <TRequest, THandler> Class<TRequest> getRequestClassFrom(Class<THandler> handlerCls) {
        var interfaces = handlerCls.getGenericInterfaces();
        if (interfaces.length > 0) {
            return (Class<TRequest>) ((ParameterizedType) handlerCls.getGenericInterfaces()[0])
                    .getActualTypeArguments()[0];
        }
        return (Class<TRequest>) ((ParameterizedType) handlerCls.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
