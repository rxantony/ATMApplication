package com.bank.atm.domain.service.user.command.withdraw;

import com.bank.atm.domain.service.user.command.TransactionResult;

public class WithdrawResult extends TransactionResult {

    public WithdrawResult(String accountName) {
        super(accountName);
    }

    public WithdrawResult(String accountName, int amount, int balace) {
        super(accountName, amount, balace);
    }
}