package com.bank.atm.domain.common.handler;

public interface RequestHandlerManager {
    <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request)
        throws Exception;

    <TRequest extends Request<TResult>, TResult> RequestHandlerManager register(
        AbstractRequestHandler<TRequest, TResult> handler);
}
