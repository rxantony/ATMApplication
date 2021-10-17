package com.dkatalist.atm.domain;

public interface ObjectFactory<I, O> {
    O create(I arg);
}
