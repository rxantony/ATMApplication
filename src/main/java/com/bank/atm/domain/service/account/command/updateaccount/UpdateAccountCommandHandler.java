package com.bank.atm.domain.service.account.command.updateaccount;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.repository.AccountRepository;
import com.bank.atm.domain.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateAccountCommandHandler 
  extends AbstractRequestHandler<UpdateAccountCommand, Optional<AccountDto>> {

  @NotNull
  private final AccountRepository repo;

  @NotNull
  private final AccountMapper mapper;

  @Override
  public Optional<AccountDto> handle(UpdateAccountCommand request) throws Exception {
   return repo.get(request.getName())
      .map(a -> Optional.of(request.getBalanceUpdate().updateBalance(a.getBalance()))
        .filter(b -> b > 0)
        .flatMap(b -> {
          a.setBalance(b);
          return repo.update(a);
        })
        .map(mapper::toDto)
        .orElseThrow(()-> UpdateAccountException.amountMustNotbeLowerThanZero(mapper.toDto(a)))
      );
  }
}
