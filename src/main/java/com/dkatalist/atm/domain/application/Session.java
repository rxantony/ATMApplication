package com.dkatalist.atm.domain.application;

import java.util.List;

import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.atm.command.deposit.DepositResult;
import com.dkatalist.atm.domain.service.atm.command.transfer.TransferResult;
import com.dkatalist.atm.domain.service.atm.command.withdraw.WithdrawResult;

public interface Session {
    void logout();

    String getAccountName();

    Account getAccount() throws ServiceException;

    DepositResult deposit(int amount) throws ServiceException;

    WithdrawResult withdraw(int amount) throws ServiceException;

    TransferResult transfer(String toAccountName, int amount) throws ServiceException;

    List<Owe> getOweList() throws ServiceException;

    AbstractInputHandler getInputHandler();
}
