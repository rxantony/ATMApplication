package com.bank.atm.domain.service.user.command.requestdebt;

import com.bank.atm.domain.data.dto.DebtDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestDebtResult {
  private String accountName;
  private int amount;
  private int requestAmount;
  private DebtDto debt;
  private DebtDto receivable;
}
