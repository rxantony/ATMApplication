package com.bank.atm.domain.service.debt.query.getdebt;

import java.util.Optional;

import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.data.dto.DebtDto;

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
public class GetOptDebtQuery implements Request<Optional<DebtDto>> {
  @NotBlank
  private String accountName1;

  @NotBlank
  private String accountName2;

  private boolean debtor;
}