package com.dkatalist.atm.domain.common.cqrs.handler;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class HandlerManagerDefault implements HandlerManager {
    private Map<Class<?>, Handler<?,?>> handlerStores = new HashMap<>();
    private Map<Class<?>, HandlerWithException<?,?, ?>> handlerWithExpStores = new HashMap<>();


    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResult>, TResult> TResult execute(TRequest request) {
        var handler = (Handler<TRequest, TResult>)handlerStores.get(request.getClass());
        if(handler == null)
            throw new HandlerIsNotFoundException();
        return handler.execute(request);
    }

    @Override
    public <TRequest extends Request<TResult>, TResult> HandlerManager registerHandler(
            Handler<TRequest, TResult> handler) {
        var cls = handler.getClass();
        var type =  getRequestClassFrom(cls);
        handlerStores.put(type, handler);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends RequestWithException<TResult, TException>, TResult, TException extends Exception> TResult execute(TRequest request) throws TException {
        var handler = (HandlerWithException<TRequest, TResult, TException>)handlerWithExpStores.get(request.getClass());
        if(handler == null)
            throw new HandlerIsNotFoundException();
        return handler.execute(request);
    }

    @Override 
    public <TRequest extends RequestWithException<TResult,TException>,TResult, TException extends Exception> HandlerManagerDefault registerHandler(HandlerWithException<TRequest, TResult, TException> handler){
        var cls = handler.getClass();
        var type =  getRequestClassFrom(cls);
        /*
        Class<TRequest> type
        var interfaces =  cls.getGenericInterfaces();
        if(interfaces.length > 0){
            type = (Class<TRequest>) ((ParameterizedType)cls.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        } else {
            type = (Class<TRequest>)((ParameterizedType) handler.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }*/
        handlerWithExpStores.put(type, handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    private static <TRequest, THandler> Class<TRequest> getRequestClassFrom(Class<THandler> handlerCls){
        var interfaces =  handlerCls.getGenericInterfaces();
        if(interfaces.length > 0){
            return (Class<TRequest>) ((ParameterizedType)handlerCls.getGenericInterfaces()[0]).getActualTypeArguments()[0];
        }
        return (Class<TRequest>)((ParameterizedType)handlerCls.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
    