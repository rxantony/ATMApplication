package com.bank.atm.domain.service.user.command.withdraw;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.handler.Request;

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
public class WithdrawCommand implements Request<WithdrawResult> {
	@NotBlank
	private String accountName;

	@Min(1)
	private int amount;
}
