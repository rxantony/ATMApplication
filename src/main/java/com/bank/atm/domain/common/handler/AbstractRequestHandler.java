package com.bank.atm.domain.common.handler;

import java.lang.reflect.ParameterizedType;

public abstract class AbstractRequestHandler <TRequest extends Request<TResult>, TResult>{

  @SuppressWarnings("unchecked")
  public final Class<TRequest> getRequestClass() {
    return (Class<TRequest>) ((ParameterizedType) getClass()
        .getGenericSuperclass())
        .getActualTypeArguments()[0];
  }

  public abstract TResult handle(TRequest request) throws Exception;
}
