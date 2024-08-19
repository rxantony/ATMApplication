package com.bank.atm.domain.service.user.command.reducedebts;

import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.model.Debt;
import com.bank.atm.domain.data.repository.DebtRepository;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommand;
import com.bank.atm.domain.service.dto.DebtDto;
import com.bank.atm.domain.service.mapper.DebtMapper;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtException;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;
import com.bank.atm.domain.service.user.command.updatedebts.UpdateDebtsCommand;
import com.bank.atm.domain.service.user.query.getdebt.GetOptDebtQuery;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod(HandlerExtensions.class)
public class ReduceDebtsCommandHandler 
	extends AbstractRequestHandler<ReduceDebtsCommand, ReduceDebtsResult> {

	@NotNull
	private final RequestHandlerManager manager;

	@NotNull
	private final DebtRepository repo;

	@NotNull
	private final DebtMapper mapper;

  @Override
  public ReduceDebtsResult handle(ReduceDebtsCommand request) throws Exception {
    var debts = repo.getDebtToList(request.getAccountName()).stream()
      .map(d -> reduceDebt(request, d))
			.map(r -> updateAccount(r))
			.collect(Collectors.toList());

		return ReduceDebtsResult.builder()
			.reduceDebts(debts)
			.build();
  }

  private ReduceDebtResult reduceDebt(ReduceDebtsCommand request, Debt debt) {
    if(request.getAmount() == 0){
      return null;
    }

		var actualAmount = debt.getAmount() < request.getAmount() ? 
      Math.abs(debt.getAmount()) : 
      request.getAmount();

		debt.setAmount(debt.getAmount() + actualAmount);
		var receivable = reduceReceiveable(debt.getAccountName2(), debt.getAccountName1(), actualAmount);

		var debtDto = mapper.toDto(debt);
		manager.execute(UpdateDebtsCommand.builder()
				.debt(debtDto)
				.debt(receivable)
				.build());

    request.setAmount(request.getAmount() - actualAmount);
		return ReduceDebtResult.builder()
				.remainder(request.getAmount())
				.amount(actualAmount)
				.debt(debtDto)
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
