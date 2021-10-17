package com.dkatalist.atm.domain;

import java.util.function.Consumer;

public class CreateSessionArg {
    public final String accountName;
    public final Consumer<String> eventLogout;
    
    public CreateSessionArg(String accountName, Consumer<String> eventLogout){
        Guard.validateArgNotNullOrEmpty(accountName, "accountName");
        Guard.validateArgNotNull(eventLogout, "eventLogout");
        this.accountName = accountName;
        this.eventLogout = eventLogout;
    }
}
