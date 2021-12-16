package com.dkatalist.atm.domain.common.cqrs.handler;

public class Nothing {
    private Nothing() {}
    
    public final static Nothing instance = new Nothing();
}
