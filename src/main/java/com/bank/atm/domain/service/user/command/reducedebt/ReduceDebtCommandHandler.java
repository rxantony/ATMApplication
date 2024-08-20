package com.bank.atm.domain.service.user.command.reducedebt;

import java.util.Optional;
import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommand;
import com.bank.atm.domain.service.debt.command.updatedebts.UpdateDebtsCommand;
import com.bank.atm.domain.service.debt.query.getdebt.GetOptDebtQuery;
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
		return getDebt(request.getAccountName1(),request.getAccountName2())
			.filter(d -> d.getAmount() != 0)
			.map(d -> reduceDebt(request, d))
			.map(r -> updateAccount(r));
	}

	private ReduceDebtResult reduceDebt(ReduceDebtCommand request, DebtDto debt) {
		var isDebt = debt.getAmount() < 1;
		var debtAmount = Math.abs(debt.getAmount());
		var actualAmount = debtAmount < request.getAmount() ? debtAmount : request.getAmount();
		
		reduceDebt(debt, actualAmount);
		return getDebt(debt.getAccountName2(), debt.getAccountName1())
			.map(d2 -> reduceDebt(d2, actualAmount))
			.map(d2 -> updateDebts(isDebt, debt, d2, request.getAmount(), actualAmount))
			.orElse(null);
	}

	private DebtDto reduceDebt(DebtDto debt, int amount) {
		if(debt.getAmount() > 0){
			debt.setAmount(debt.getAmount() - amount);
		}
		else{
			debt.setAmount(debt.getAmount() + amount);
		}
		return debt;
	}

	private Optional<DebtDto> getDebt(String accountName1, String accountName2){
		return manager.execute(GetOptDebtQuery.builder()
			.accountName1(accountName1)
			.accountName2(accountName2)
			.build());
	}

	private ReduceDebtResult updateDebts(boolean isDebt, DebtDto debt1, DebtDto debt2, int requestAmount, int actualAmount) {
		manager.execute(UpdateDebtsCommand.builder()
		.debt(debt1)
		.debt(debt2) 
		.build());

		return ReduceDebtResult.builder()
			.isDebt(isDebt)
			.remainder(requestAmount - actualAmount)
			.amount(actualAmount)
			.debt1(debt1)
			.debt2(debt2)
			.build();
	}

	private ReduceDebtResult updateAccount(ReduceDebtResult result) {
		if(result.isDebt()){
			manager.execute(UpdateAccountsCommand.builder()
					.request(UpdateAccountCommand.builder()
							.name(result.getDebt1().getAccountName1())
							.balanceUpdate(BalanceUpdate.of(result.getAmount(), false))
							.build())
					.request(UpdateAccountCommand.builder()
							.name(result.getDebt2().getAccountName1())
							.balanceUpdate(BalanceUpdate.of(result.getAmount(), true))
							.build())
					.build());
		}
		return result;
	}
}
