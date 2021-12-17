package com.dkatalist.atm.domain.service.account.query.getAccount;

import java.util.Optional;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.common.handler.Handler;
import com.dkatalist.atm.domain.data.Account;
import com.dkatalist.atm.domain.data.AccountRepository;

public class GetAccountQuery implements Handler<GetAccountRequest, Optional<Account>> {

    private final AccountRepository accountRepo;

    public GetAccountQuery(AccountRepository accountRepo) {
        Guard.validateArgNotNull(accountRepo, "accountRepo");
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<Account> execute(GetAccountRequest request) {
        return accountRepo.get(request.getAccountName());
    }
}