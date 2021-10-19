package com.dkatalist.atm.domain.common;

public interface ObjectFactory<I, O> {
    O create(I arg);
}
