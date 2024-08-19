package com.bank.atm.domain.service.user.command.reducedebt;

import com.bank.atm.domain.service.dto.DebtDto;

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
