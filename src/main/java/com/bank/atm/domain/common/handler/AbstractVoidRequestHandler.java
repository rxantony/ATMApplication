package com.bank.atm.domain.common.handler;

public abstract class AbstractVoidRequestHandler<TRequest extends Request<Void>>  
  extends AbstractRequestHandler<TRequest, Void> {
  
    @Override
    public Void handle(TRequest request) throws Exception {
      handleInternal(request);
      return null;
    }

    protected abstract void handleInternal(TRequest request) throws Exception;
}
