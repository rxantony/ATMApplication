package com.bank.atm.domain.service.user.command.withdraw;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.data.AccountRepository;
import com.bank.atm.domain.service.AbstractCommand;
import com.bank.atm.domain.service.ServiceException;

public class WithdrawCommand extends AbstractCommand<WithdrawRequest, WithdrawResult> {

    public WithdrawCommand(AccountRepository accRepo) {
        super(accRepo, WithdrawRequest.class);
    }

    @Override
    public WithdrawResult handle(WithdrawRequest request) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(request.getAccountName(), "accountName");

        if (request.getAmount() < 1)
            throw WithdrawException.amountBeMustGreaterThanOrEqualsTo(request.getAccountName(), request.getAmount());

        var acc = getAccount(request.getAccountName());
        var result = new WithdrawResult(request.getAccountName());
        if (request.getAmount() > acc.getBalance())
            throw WithdrawException.notEnoughAmount(request.getAccountName(), request.getAmount());

        acc.setBalance(acc.getBalance() - request.getAmount());
        updateAccounts(acc);

        result.setAmount(request.getAmount());
        result.setBalance(acc.getBalance());
        return result;
    }
}
