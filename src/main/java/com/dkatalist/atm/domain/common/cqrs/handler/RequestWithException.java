package com.dkatalist.atm.domain.common.cqrs.handler;

import com.dkatalist.atm.domain.common.ATMException;

public interface RequestWithException <TResult, TException extends ATMException>  {
    
}
