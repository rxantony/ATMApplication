package com.bank.atm.domain.service.account.command.createAccount;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.AccountRepository;

public class CreateAccountCommand extends AbstractHandler<CreateAccountRequest, Account> {
    private final AccountRepository accountRepo;

    public CreateAccountCommand(AccountRepository accountRepo) {
        super(CreateAccountRequest.class);
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
