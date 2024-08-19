package com.bank.atm.domain.service.account.query.getoptaccount;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.repository.AccountRepository;
import com.bank.atm.domain.service.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetOptAccountQueryHandler 
  extends AbstractRequestHandler<GetOptAccountQuery, Optional<AccountDto>> {

  @NotNull
  private final AccountRepository repo;

  @NotNull
  private final AccountMapper manager;

  @Override
  public Optional<AccountDto> handle(GetOptAccountQuery request) throws Exception {
    return repo.get(request.getName())
        .map(manager::toDto);
  }
}
