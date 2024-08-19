package com.bank.atm.domain.service.debt.command.adddebts;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.data.dto.DebtDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddDebtsCommand implements Request<Collection<DebtDto>> {
  @NotEmpty
  @Singular
  private Collection<DebtDto> debts;
}