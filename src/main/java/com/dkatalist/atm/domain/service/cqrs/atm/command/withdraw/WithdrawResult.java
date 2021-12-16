package com.dkatalist.atm.domain.service.cqrs.atm.command.withdraw;

import com.dkatalist.atm.domain.service.cqrs.TransactionResult;

public class WithdrawResult extends TransactionResult {

    public WithdrawResult(String accountName) {
        super(accountName);
    }

    public WithdrawResult(String accountName, int amount, int balace) {
        super(accountName, amount, balace);
        setAmount(amount);
        setBalance(balace);
    }
}