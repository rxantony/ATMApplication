package com.bank.atm.domain.data.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  private String name;
  private int balance;
  private Date createdAt;

  public Account(Account acc) {
    name = acc.getName();
    balance = acc.getBalance();
  }
}
