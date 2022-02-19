package com.bank.atm.domain.common;

public interface ObjectFactory<I, O> {
    O create(I arg);
}
