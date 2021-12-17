package com.dkatalist.atm.domain.application.facade;

import java.util.List;

import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.facade.ATMService.DepositResult;
import com.dkatalist.atm.domain.service.facade.ATMService.TransactionResult;
import com.dkatalist.atm.domain.service.facade.ATMService.TransferResult;

public interface Session {
    void logout();
    String getAccountName();
    Account getAccount() throws ServiceException;
    DepositResult deposit(int amount) throws ServiceException;
    TransactionResult withdraw(int amount) throws ServiceException;
    TransferResult transfer(String toAccountName, int amount) throws ServiceException; 
    List<Owe> getOweList();
    AbstractInputHandler getInputHandler();  
}
