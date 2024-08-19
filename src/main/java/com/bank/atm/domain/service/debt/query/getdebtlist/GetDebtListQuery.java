package com.bank.atm.domain.service.debt.query.getdebtlist;

import java.util.Collection;

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
public class GetDebtListQuery implements Request<Collection<DebtDto>> {
	@NotBlank
	private String accountName;

	private boolean debtor;
}
