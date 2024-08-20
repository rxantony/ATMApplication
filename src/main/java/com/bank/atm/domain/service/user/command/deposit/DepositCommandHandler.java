package com.bank.atm.domain.service.user.command.deposit;

import java.util.Optional;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.user.command.reducedebts.ReduceDebtsCommand;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod({ HandlerExtensions.class })
public class DepositCommandHandler 
	extends AbstractRequestHandler<DepositCommand, DepositResult> {
	
	private final RequestHandlerManager manager;

	@Override
	public DepositResult handle(DepositCommand request) throws Exception {
		return deposit(request)
				.map(a -> reduceDebts(request, a))
				.orElseThrow(() -> DepositException.invalidAcount(request.getAccountName(), request.getAmount()));
	}

	private Optional<AccountDto> deposit(DepositCommand request) {
		return manager.execute(UpdateAccountCommand.builder()
				.name(request.getAccountName())
				.balanceUpdate(BalanceUpdate.of(request.getAmount(), true))
				.build());
	}

	private DepositResult reduceDebts(DepositCommand request, AccountDto account) {
		var debt = manager.execute(ReduceDebtsCommand.builder()
				.accountName(request.getAccountName())
				.amount(request.getAmount())
				.build());

		return DepositResult.builder()
				.accountName(account.getName())
				.balance(account.getBalance() - debt.getTotalAmount())
				.amount(request.getAmount() - debt.getTotalAmount())
				.requestAmount(request.getAmount() )
				.paidDebts(debt.getReduceDebts())
				.build();
	}
}
