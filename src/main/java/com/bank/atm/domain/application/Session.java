package com.bank.atm.domain.application;

import java.util.List;

import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.Owe;
import com.bank.atm.domain.service.ServiceException;
import com.bank.atm.domain.service.user.command.deposit.DepositResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawResult;

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
