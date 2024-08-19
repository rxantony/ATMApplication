package com.bank.atm.domain.service.user.command.requestdebt;

import com.bank.atm.domain.service.dto.DebtDto;

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
