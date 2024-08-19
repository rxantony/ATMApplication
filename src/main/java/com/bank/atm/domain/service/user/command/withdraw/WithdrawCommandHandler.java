package com.bank.atm.domain.service.user.command.withdraw;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.mapper.AccountMapper;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountQuery;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod(HandlerExtensions.class)
public class WithdrawCommandHandler 
	extends AbstractRequestHandler<WithdrawCommand, WithdrawResult>{

	@NotNull
	private final RequestHandlerManager manager;

	@NotNull
	private final AccountMapper mapper;

	@Override
	public WithdrawResult handle(WithdrawCommand request) throws Exception {
		var acc = manager.execute(new GetAccountQuery(request.getAccountName()));
		if (request.getAmount() > acc.getBalance()){
			throw WithdrawException.notEnoughAmount(request.getAccountName(), request.getAmount());
		}

		return manager.execute(UpdateAccountCommand.builder()
			.name(request.getAccountName())
			.balanceUpdate(BalanceUpdate.of(request.getAmount(), false))
			.build())
		.map(a -> WithdrawResult.builder()
			.accountName(request.getAccountName())
			.amount(request.getAmount())
			.balance(a.getBalance())
			.build())
		.orElse(null);
	}
}
