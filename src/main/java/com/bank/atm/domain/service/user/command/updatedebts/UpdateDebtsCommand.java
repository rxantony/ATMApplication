package com.bank.atm.domain.service.user.command.updatedebts;

import java.util.Collection;

import javax.validation.constraints.NotEmpty;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.service.dto.DebtDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDebtsCommand implements Request<Collection<DebtDto>> {
  @NotEmpty
  @Singular
  private Collection<DebtDto> debts;
}
