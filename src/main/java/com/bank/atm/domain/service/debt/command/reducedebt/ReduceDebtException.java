package com.bank.atm.domain.service.debt.command.reducedebt;

import com.bank.atm.domain.common.ATMException;
import lombok.Getter;

@Getter
public class ReduceDebtException extends ATMException {
  private final String accountName1;
  private final String accountName2;

  public ReduceDebtException(String accountName1, String accountName2, String message) {
    super(message);
    this.accountName1 = accountName1;
    this.accountName2 = accountName2;
  }

  public static ReduceDebtException canNotReduceDebt(String accountName1, String accountName2, int amount){
    return new ReduceDebtException(accountName1, accountName2, 
      String.format("can not reduce debt, account name 1: %s,  account name 2: %s with amount %d", accountName1, accountName2, amount));
  }
}
