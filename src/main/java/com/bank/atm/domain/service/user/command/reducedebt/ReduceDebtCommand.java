package com.bank.atm.domain.service.user.command.reducedebt;

import java.util.Optional;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import com.bank.atm.domain.common.handler.Request;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReduceDebtCommand implements Request<Optional<ReduceDebtResult>> {
	@NotBlank
	private String accountName1;

	@NotBlank
	private String accountName2;

	@Min(1)
	private int amount;

	private boolean debtor;
}