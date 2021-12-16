package com.dkatalist.atm.domain.common.cqrs.handler;

public interface Handler<TRequest extends Request<TResult>, TResult> {
    TResult execute(TRequest request);
}
