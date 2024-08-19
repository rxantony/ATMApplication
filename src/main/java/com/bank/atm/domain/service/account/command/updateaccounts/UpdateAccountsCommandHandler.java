package com.bank.atm.domain.service.account.command.updateaccounts;

import java.util.Collection;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.data.repository.AccountRepository;
import com.bank.atm.domain.service.dto.AccountDto;
import com.bank.atm.domain.data.model.Account;
import com.bank.atm.domain.service.mapper.AccountMapper;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateAccountsCommandHandler 
  extends AbstractRequestHandler<UpdateAccountsCommand, Collection<AccountDto>>{
  
  @NotNull
  private final AccountRepository repo;

  @NotNull
  private final AccountMapper mapper;

  @Override
  public Collection<AccountDto> handle(UpdateAccountsCommand request) throws Exception {
    return repo.update(toAccounts(request.getRequests())).stream()
      .map(mapper::toDto)
      .collect(Collectors.toList());
  }

  private Collection<Account> toAccounts(Collection<UpdateAccountCommand> requests){
    return requests.stream()
      .map(r -> repo.get(r.getName())
        .map(a -> Account.builder()
          .name(a.getName())
          .balance(r.getBalanceUpdate().updateBalance(a.getBalance()))
          .build())
        .orElse(null)
      )
      .collect(Collectors.toList());
  }
}
