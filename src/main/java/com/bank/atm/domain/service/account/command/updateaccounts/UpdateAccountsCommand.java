package com.bank.atm.domain.service.account.command.updateaccounts;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.dto.AccountDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountsCommand implements Request<Collection<AccountDto>> {
  @NotEmpty
  @Singular
  private Collection<UpdateAccountCommand> requests;
}