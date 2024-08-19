package com.bank.atm.domain.common.handler;

import com.bank.atm.domain.common.ATMException;

public class RequestInvalidException extends ATMException {
  public RequestInvalidException(String message){
    super(message);
  }
}
