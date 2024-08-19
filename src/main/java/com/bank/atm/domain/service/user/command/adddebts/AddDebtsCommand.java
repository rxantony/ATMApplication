package com.bank.atm.domain.service.user.command.adddebts;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;

import com.bank.atm.domain.common.handler.VoidRequest;
import com.bank.atm.domain.service.dto.DebtDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddDebtsCommand implements VoidRequest {
  @NotEmpty
  @Singular
  private Collection<DebtDto> debts;
}