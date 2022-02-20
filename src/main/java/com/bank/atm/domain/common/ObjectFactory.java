package com.bank.atm.domain.common;

public interface ObjectFactory<TArg, TResult> {
    TResult create(TArg arg);
}
