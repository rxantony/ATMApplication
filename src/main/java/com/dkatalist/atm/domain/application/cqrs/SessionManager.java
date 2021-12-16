package com.dkatalist.atm.domain.application.cqrs;

import com.dkatalist.atm.domain.application.AbstractInputHandler;

public interface SessionManager {
    Session getSession();
    void login(String userName);
    AbstractInputHandler getInputHandler();  
}
