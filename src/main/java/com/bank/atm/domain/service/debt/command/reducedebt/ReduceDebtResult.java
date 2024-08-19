package com.bank.atm.domain.service.debt.command.reducedebt;

import com.bank.atm.domain.data.dto.DebtDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReduceDebtResult {
  private final int amount;
  private final int remainder;
  private final DebtDto debt;
  private final DebtDto receivable;
}
