package com.bank.atm.domain.service.account.query.getaccount;

import java.util.Optional;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.AbstractHandler;
import com.bank.atm.domain.data.AccountRepository;

public class GetAccountQuery extends AbstractHandler<GetAccountRequest, Optional<GetAccountResult>> {
    private final AccountRepository accountRepo;

    public GetAccountQuery(AccountRepository accountRepo) {
        super(GetAccountRequest.class);
        Guard.validateArgNotNull(accountRepo, "accountRepo");
        this.accountRepo = accountRepo;
    }

    @Override
    public Optional<GetAccountResult> handle(GetAccountRequest request) {
        var acc = accountRepo.get(request.getAccountName());
        if(!acc.isPresent())
            return Optional.empty();

        var result =  new GetAccountResult(acc.get().getName(), acc.get().getBalance());
        return Optional.of(result);
    }
}