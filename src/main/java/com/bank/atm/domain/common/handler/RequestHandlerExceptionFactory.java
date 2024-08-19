package com.bank.atm.domain.common.handler;

public interface RequestHandlerExceptionFactory <TRequest>{
  Exception createException(TRequest request, Throwable cause);
}
