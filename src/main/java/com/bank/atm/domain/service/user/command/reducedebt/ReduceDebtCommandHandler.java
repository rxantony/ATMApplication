package com.bank.atm.domain.service.user.command.reducedebt;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.service.dto.DebtDto;
import com.bank.atm.domain.service.user.query.getdebt.GetOptDebtQuery;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommand;
import com.bank.atm.domain.service.user.command.updatedebts.UpdateDebtsCommand;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod(HandlerExtensions.class)
public class ReduceDebtCommandHandler 
	extends AbstractRequestHandler<ReduceDebtCommand, Optional<ReduceDebtResult>> {

	@NotNull
	private final RequestHandlerManager manager;

	@Override
	public Optional<ReduceDebtResult> handle(ReduceDebtCommand request) {
		return reduceDebt(request)
				.map(r -> updateAccount(r));
	}

	private Optional<ReduceDebtResult> reduceDebt(ReduceDebtCommand request) {
		return manager.execute(GetOptDebtQuery.builder()
				.accountName1(request.getAccountName1())
				.accountName2(request.getAccountName2())
				.debtor(true)
				.build())
				.filter(d -> d.getAmount() < 0)
				.map(d -> reduceDebt(d, request.getAmount()));
	}

	private ReduceDebtResult reduceDebt(DebtDto debt, int amount) {
		var actualAmount = debt.getAmount() < amount ? Math.abs(debt.getAmount()) : amount;

		debt.setAmount(debt.getAmount() + actualAmount);

		var receivable = reduceReceiveable(debt.getAccountName2(), debt.getAccountName1(), actualAmount);

		manager.execute(UpdateDebtsCommand.builder()
				.debt(debt)
				.debt(receivable)
				.build());

		return ReduceDebtResult.builder()
				.remainder(amount - actualAmount)
				.amount(actualAmount)
				.debt(debt)
				.receivable(receivable)
				.build();
	}

	private DebtDto reduceReceiveable(String accountName1, String accountName2, int amount) {
		return manager.execute(GetOptDebtQuery.builder()
				.accountName1(accountName1)
				.accountName2(accountName2)
				.debtor(false)
				.build())
				.map(d -> {
					d.setAmount(d.getAmount() - amount);
					return d;
				})
				.orElseThrow(() -> ReduceDebtException.canNotReduceDebt(accountName2, accountName1, amount));
	}

	private ReduceDebtResult updateAccount(ReduceDebtResult result) {
		manager.execute(UpdateAccountsCommand.builder()
				.request(UpdateAccountCommand.builder()
						.name(result.getDebt().getAccountName1())
						.balanceUpdate(BalanceUpdate.of(result.getAmount(), false))
						.build())
				.request(UpdateAccountCommand.builder()
						.name(result.getReceivable().getAccountName1())
						.balanceUpdate(BalanceUpdate.of(result.getAmount(), true))
						.build())
				.build());
		return result;
	}
}
