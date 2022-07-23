package com.bank.atm.domain.service.user.command.withdraw;

import com.bank.atm.domain.service.user.command.AbstractTransactionResult;

public class WithdrawResult extends AbstractTransactionResult {

    public WithdrawResult(String accountName) {
        super(accountName);
    }

    public WithdrawResult(String accountName, int amount, int balace) {
        super(accountName, amount, balace);
    }
}