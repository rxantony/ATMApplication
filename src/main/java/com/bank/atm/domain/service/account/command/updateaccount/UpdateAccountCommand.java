package com.bank.atm.domain.service.account.command.updateaccount;

import java.util.Optional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.Request;
import com.bank.atm.domain.service.dto.AccountDto;

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
public class UpdateAccountCommand implements Request<Optional<AccountDto>> {
	@NotBlank
	private String name;

	@NotNull
	private BalanceUpdate balanceUpdate;

@Getter
@Setter
@Builder
@AllArgsConstructor
	public static class BalanceUpdate {
		private int balance;
		private boolean inc;
		public static BalanceUpdate of(int balance, boolean inc){
			return new BalanceUpdate(balance, inc);
		}
		public int updateBalance(int amount){
			return inc ? amount + balance : amount - balance;
		}
	}
}
