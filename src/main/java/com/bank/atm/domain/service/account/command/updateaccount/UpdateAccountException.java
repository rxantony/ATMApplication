package com.bank.atm.domain.service.account.command.updateaccount;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.ATMException;
import com.bank.atm.domain.data.dto.AccountDto;

import lombok.Getter;

@Getter
public class UpdateAccountException extends ATMException{

  @NotNull
  private final AccountDto account;

  public UpdateAccountException(AccountDto account, String message) {
    super(message);
    this.account = account;
  }

  public static UpdateAccountException amountMustNotbeLowerThanZero(AccountDto account){
    return new UpdateAccountException(account, String.format("amount must not be lower than zero"));
  }
}