package com.dkatalist.atm.domain.service.atm.command.withdraw;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.AccountRepository;
import com.dkatalist.atm.domain.service.ServiceException;
import com.dkatalist.atm.domain.service.atm.command.AbstractATMCommand;

public class WithdrawCommand extends AbstractATMCommand<WithdrawRequest, WithdrawResult> {

    public WithdrawCommand(AccountRepository accRepo) {
        super(accRepo);
    }

    @Override
    public WithdrawResult execute(WithdrawRequest request) throws ServiceException {
        Guard.validateArgNotNullOrEmpty(request.getAccountName(), "accountName");

        if (request.getAmount() < 1)
            throw WithdrawException.amountMustGreaterThanOrEqualsTo(request.getAccountName(), request.getAmount());

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
