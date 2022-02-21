package com.bank.atm.domain.common;

public interface ChainObject<TNext> {
    TNext setNext(TNext next);
}
