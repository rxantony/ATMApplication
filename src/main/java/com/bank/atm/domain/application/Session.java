package com.bank.atm.domain.application;

import java.util.List;

import com.bank.atm.domain.service.ServiceException;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountResult;
import com.bank.atm.domain.service.user.command.deposit.DepositResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawResult;
import com.bank.atm.domain.service.user.query.getowelist.GetOweResult;

public interface Session extends AutoCloseable{
    void logout();

    String getAccountName();

    GetAccountResult getAccount() throws ServiceException;

    DepositResult deposit(int amount) throws ServiceException;

    WithdrawResult withdraw(int amount) throws ServiceException;

    TransferResult transfer(String toAccountName, int amount) throws ServiceException;

    List<GetOweResult> getOweList() throws ServiceException;

    AbstractInputHandler getInputHandler();
}
