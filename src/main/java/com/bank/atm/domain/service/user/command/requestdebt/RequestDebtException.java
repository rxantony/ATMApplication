package com.bank.atm.domain.service.user.command.requestdebt;

import com.bank.atm.domain.common.ATMException;
import lombok.Getter;

@Getter
public class RequestDebtException extends ATMException{

  private final String accountName1;
  private final String accountName2;

  public RequestDebtException(String accountName1, String accountName2, String message) {
    super(message);
    this.accountName1 = accountName1;
    this.accountName2 = accountName2;
  }

  public static RequestDebtException noDebtExists(String accountName1, String accountName2){
    return new RequestDebtException(accountName1, accountName2, 
      String.format("no debt exists, account name 1: %s,  account name 2: %s with amount", accountName1, accountName2));
  }

  public static RequestDebtException noSufficientAmountToBorrow(String accountName1, String accountName2){
    return new RequestDebtException(accountName1, accountName2, 
      String.format("no sufficient amount to borrow of %s", accountName2));
  }
}
