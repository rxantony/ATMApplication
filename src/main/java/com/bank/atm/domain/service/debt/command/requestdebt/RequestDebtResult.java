package com.bank.atm.domain.service.debt.command.requestdebt;

import com.bank.atm.domain.data.dto.DebtDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestDebtResult {
  private int amount;
  private int requestAmount;
  private final DebtDto debt;
  private final DebtDto receivable;
}
