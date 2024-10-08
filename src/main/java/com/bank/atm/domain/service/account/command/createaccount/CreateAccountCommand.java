package com.bank.atm.domain.service.account.command.createaccount;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.data.dto.AccountDto;

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
public class CreateAccountCommand implements Request<AccountDto> {
	@NotBlank
	private String name;
	@Min(0)
	private int balance;
}
