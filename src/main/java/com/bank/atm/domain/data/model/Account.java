package com.bank.atm.domain.data.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

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
  @NotBlank
  private String name;

  @Min(0)
  private int balance;

  public Account(Account acc) {
    name = acc.getName();
    balance = acc.getBalance();
  }
}
