package com.dkatalist.atm.domain;

public interface Session {
    void logout();
    Account getAccount() throws AccountNotExistsException;
    void deposit(int amount) throws AccountNotExistsException;
    void withdraw(int amount) throws SessionBaseException;
    void transfer(String toAccountName, int amount) throws SessionBaseException; 
    InputHandler getInputHandler();  
}
