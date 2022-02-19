package com.bank.atm.domain.common;

public interface ObjectFactory<TArg, TOutput> {
    TOutput create(TArg arg);
}
