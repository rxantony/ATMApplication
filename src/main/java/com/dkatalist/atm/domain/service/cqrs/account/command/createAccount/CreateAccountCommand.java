package com.dkatalist.atm.domain.service.cqrs.account.command.createAccount;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.cqrs.handler.Handler;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;

public class CreateAccountCommand implements Handler<CreateAccountRequest, Account> {

    private final AccountRepository accountRepo;

    public CreateAccountCommand(AccountRepository accountRepo) {
        Guard.validateArgNotNull(accountRepo, "accountRepo");
        this.accountRepo = accountRepo;
    }

    @Override
    public Account execute(CreateAccountRequest request) {
        var oacc = accountRepo.get(request.getAccountName());
        if (oacc.isPresent())
            return oacc.get();
        var newAcc = new Account(request.getAccountName(), request.getBalance());
        accountRepo.add(newAcc);
        return newAcc;
    }

}
