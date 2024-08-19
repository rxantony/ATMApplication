package com.bank.atm.domain.application;

import java.util.Collection;

import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.service.user.command.deposit.DepositResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawResult;

public interface Session extends AutoCloseable{
    void logout();

    AccountDto getAccount();

    DepositResult deposit(int amount);

    WithdrawResult withdraw(int amount);

    TransferResult transfer(String toAccountName, int amount);

    Collection<DebtDto> getDebtList();

    AbstractInputHandler getInputHandler();
}
