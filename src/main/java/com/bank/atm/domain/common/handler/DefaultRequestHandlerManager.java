package com.bank.atm.domain.common.handler;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Validator;

import com.bank.atm.domain.common.Guard;

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

		validate(request);
		
		var cls = request.getClass();
		var handler = (AbstractRequestHandler<TRequest, TResult>) handlerStores.get(cls);

		if (handler == null){
			throw new RequestHandlerNotFoundException(cls.getCanonicalName());
		}

		try {	
			return handler.handle(request);
		}
		catch(Exception ex){
			if(ex instanceof RuntimeException && ex.getCause() instanceof Exception){
				throw (Exception) ex.getCause();
			}
			throw ex;
		}
	}

	private void validate(Object request){
		Guard.validateArgNotNull(request, "request is required");
		validator.validate(request);
	}
}
