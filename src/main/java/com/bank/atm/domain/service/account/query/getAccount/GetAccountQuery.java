package com.bank.atm.domain.service.account.query.getAccount;

import java.util.Optional;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.Account;
import com.bank.atm.domain.data.AccountRepository;

public class GetAccountQuery extends AbstractHandler<GetAccountRequest, Optional<Account>> {
    private final AccountRepository accountRepo;

    public GetAccountQuery(AccountRepository accountRepo) {
        super(GetAccountRequest.class);
        Guard.validateArgNotNull(accountRepo, "accountRepo");
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<Account> execute(GetAccountRequest request) {
        return accountRepo.get(request.getAccountName());
    }
}