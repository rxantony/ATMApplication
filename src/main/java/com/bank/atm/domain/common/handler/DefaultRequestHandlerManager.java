package com.bank.atm.domain.common.handler;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultRequestHandlerManager implements RequestHandlerManager {
	private final Validator validator;
	private final Map<Class<?>, AbstractRequestHandler<?, ?>> handlerStores = new HashMap<>();

	@Override
	public <TRequest extends Request<TResult>, TResult> RequestHandlerManager register(
		AbstractRequestHandler<TRequest, TResult> handler) {
		var requestCls = handler.getRequestClass();
		handlerStores.put(requestCls, handler);
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <TRequest extends Request<TResult>, TResult> TResult handle(TRequest request)
			throws Exception {

		if(request == null){
			throw new IllegalArgumentException("request is required");
		}

		var cls = request.getClass();
		var handler = (AbstractRequestHandler<TRequest, TResult>) handlerStores.get(cls);

		if (handler == null){
			throw new RequestHandlerNotFoundException(cls.getCanonicalName());
		}

		try {
			validator.validate(request);	
			return handler.handle(request);
		}
		catch(Exception ex){
			if(handler instanceof RequestHandlerExceptionFactory){
				Throwable iex = ex instanceof RuntimeException ? ex.getCause(): ex;
				throw ((RequestHandlerExceptionFactory<TRequest>)handler).createException(request, iex);
			}
			throw ex;
		}
	}
}
