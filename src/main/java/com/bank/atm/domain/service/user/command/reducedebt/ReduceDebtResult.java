package com.bank.atm.domain.service.user.command.reducedebt;

import com.bank.atm.domain.data.dto.DebtDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReduceDebtResult {
  private int amount;
  private int remainder;
  private boolean isDebt;
  private DebtDto debt1;
  private DebtDto debt2;
}
