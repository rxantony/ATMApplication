package com.bank.atm.domain.common;

public interface ObjectFactory<TInput, TOutput> {
    TOutput create(TInput arg);
}
