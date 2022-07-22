package com.bank.atm.domain.service.account.command.createaccount;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.AccountRepository;

public class CreateAccountCommand extends AbstractHandler<CreateAccountRequest, CreateAccountResult> {
    private final AccountRepository accountRepo;

    public CreateAccountCommand(AccountRepository accountRepo) {
        super(CreateAccountRequest.class);
        Guard.validateArgNotNull(accountRepo, "accountRepo");
        this.accountRepo = accountRepo;
    }

    @Override
    public CreateAccountResult handle(CreateAccountRequest request) {
        var oacc = accountRepo.get(request.getAccountName());
        if (oacc.isPresent())
            return new CreateAccountResult(oacc.get().getName(), oacc.get().getBalance());
             
        var newAcc = new Account(request.getAccountName(), request.getBalance());
        accountRepo.add(newAcc);
        return new CreateAccountResult(newAcc.getName(), newAcc.getBalance());
    }

}
