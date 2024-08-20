package com.bank.atm.domain.service.user.command.requestdebt;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.data.repository.DebtRepository;

import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountQuery;
import com.bank.atm.domain.service.debt.command.adddebts.AddDebtsCommand;
import com.bank.atm.domain.service.debt.command.updatedebts.UpdateDebtsCommand;
import com.bank.atm.domain.service.debt.query.getdebt.GetOptDebtQuery;
import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommand;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod(HandlerExtensions.class)
public class RequestDebtCommandHandler
		extends AbstractRequestHandler<RequestDebtCommand, RequestDebtResult> {

	@NotNull
	private final RequestHandlerManager manager;

	@NotNull
	private final DebtRepository repo;

	@Override
	public RequestDebtResult handle(RequestDebtCommand request) {
		var account = getAccount(request.getAccountName2());
		return Optional.of(account.getBalance())
				.filter(b -> b > 0)
				.map(b -> calcAvailableAmount(request.getAmount(), b))
				.map(a -> makeDebt(request, a))
				.map(r -> updateAccount(request, r))
				.orElseThrow(() -> RequestDebtException.noSufficientAmountToBorrow(request.getAccountName1(),
						request.getAccountName2()));
	}

	private AccountDto getAccount(String accountName) {
		return manager.execute(new GetAccountQuery(accountName));
	}

	private int calcAvailableAmount(int requestAmount, int availableBalance) {
		return requestAmount > availableBalance ? availableBalance : requestAmount;
	}

	private RequestDebtResult makeDebt(RequestDebtCommand request, int amount) {
		return manager.execute(GetOptDebtQuery.builder()
				.accountName1(request.getAccountName1())
				.accountName2(request.getAccountName2())
				.build())
				.map(d -> updateDebt(request, d, amount))
				.orElseGet(() -> createDebt(request, amount));
	}

	private RequestDebtResult createDebt(RequestDebtCommand request, int amount) {
		var acc = manager.execute(new GetAccountQuery(request.getAccountName1()));

		var recAcc = manager.execute(new GetAccountQuery(request.getAccountName2()));

		var debt = DebtDto.builder()
				.accountName1(acc.getName())
				.accountName2(recAcc.getName())
				.amount(-amount)
				.build();

		var reciveable = DebtDto.builder()
				.accountName1(recAcc.getName())
				.accountName2(acc.getName())
				.amount(amount)
				.build();

		manager.execute(AddDebtsCommand.builder()
				.debt(debt)
				.debt(reciveable)
				.build());

		return RequestDebtResult.builder()
				.amount(amount)
				.requestAmount(request.getAmount())
				.debt(debt)
				.receivable(reciveable)
				.build();
	}

	private RequestDebtResult updateDebt(RequestDebtCommand request, DebtDto debt, int amount) {
		var receiveable = manager.execute(GetOptDebtQuery.builder()
				.accountName1(debt.getAccountName2())
				.accountName2(debt.getAccountName1())
				.build())
				.map(r -> {
					debt.setAmount(debt.getAmount() - amount);
					r.setAmount(r.getAmount() + amount);
					return r;
				})
				.orElseThrow(() -> RequestDebtException.noDebtExists(debt.getAccountName1(), debt.getAccountName2()));

		manager.execute(UpdateDebtsCommand.builder()
				.debt(debt)
				.debt(receiveable)
				.build());

		return RequestDebtResult.builder()
				.accountName(request.getAccountName1())
				.amount(amount)
				.requestAmount(request.getAmount())
				.debt(debt)
				.receivable(receiveable)
				.build();
	}

	private RequestDebtResult updateAccount(RequestDebtCommand request, RequestDebtResult result) {
		manager.execute(UpdateAccountsCommand.builder()
				.request(UpdateAccountCommand.builder()
						.name(request.getAccountName1())
						.balanceUpdate(BalanceUpdate.of(request.getAmount(), true))
						.build())
				.request(UpdateAccountCommand.builder()
						.name(request.getAccountName2())
						.balanceUpdate(BalanceUpdate.of(request.getAmount(), false))
						.build())
				.build());
		return result;
	}
}
