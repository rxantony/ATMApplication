package com.bank.atm.domain.common.handler;

public class RequestHandlerNotFoundException extends RuntimeException {
	public final String handlerName;

	public RequestHandlerNotFoundException(String handlerName) {
		super(String.format("request handler with request %s is not found", handlerName));
		this.handlerName = handlerName;
	}

}
