package com.dkatalist.atm.domain;

public interface Session {
    void logout();
    String getAccountName();
    Account getAccount() throws AccountNotExistsException;
    void deposit(int amount) throws AccountNotExistsException;
    void withdraw(int amount) throws ATMBaseException;
    void transfer(String toAccountName, int amount) throws ATMBaseException; 
    AbstractInputHandler getInputHandler();  
}
