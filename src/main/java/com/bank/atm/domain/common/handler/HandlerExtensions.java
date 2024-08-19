package com.bank.atm.domain.common.handler;

public class HandlerExtensions {
  public static <TRequest extends Request<TResult>, TResult> TResult execute(AbstractRequestHandler<TRequest, TResult> handler, TRequest request){
    try{
      return handler.handle(request);
    }
    catch(Exception ex){
      throw new RuntimeException(ex);
    }
  }

	public static <TRequest extends Request<TResult>, TResult> TResult execute(RequestHandlerManager manager, TRequest request) {
		try{
			return manager.handle(request);
		}
		catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
