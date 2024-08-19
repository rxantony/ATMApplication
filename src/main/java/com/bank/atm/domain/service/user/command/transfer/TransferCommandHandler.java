package com.bank.atm.domain.service.user.command.transfer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.common.CollectionExtensions;
import com.bank.atm.domain.common.handler.AbstractRequestHandler;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommand.BalanceUpdate;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommand;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountQuery;
import com.bank.atm.domain.service.debt.command.reducedebt.ReduceDebtCommand;
import com.bank.atm.domain.service.debt.command.reducedebt.ReduceDebtResult;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtCommand;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtResult;
import com.bank.atm.domain.service.mapper.AccountMapper;

import lombok.RequiredArgsConstructor;
import lombok.experimental.ExtensionMethod;

@RequiredArgsConstructor
@ExtensionMethod({ CollectionExtensions.class, HandlerExtensions.class })
public class TransferCommandHandler
		extends AbstractRequestHandler<TransferCommand, TransferResult> {

	@NotNull
	private final RequestHandlerManager manager;

	@NotNull
	private final AccountMapper mapper;

	@Override
	public TransferResult handle(TransferCommand request) throws Exception {
		if (request.getRecipientName().equals(request.getAccountName())) {
			throw TransferException.cannotTransferToSameAccount(request.getAccountName(), request.getRecipientName(),
					request.getAmount());
		}
		// request debt if required
		return requestDebt(request)
				.map(r -> transfer(request, r, null))
				.orElseGet(() -> transfer(request, null, reduceDebtAndReceivable(request)));
	}

	private Optional<RequestDebtResult> requestDebt(TransferCommand request) {
		var acc = manager.execute(new GetAccountQuery(request.getAccountName()));
		return Optional.of(acc)
				.filter(a -> a.getBalance() < request.getAmount())
				.flatMap(a -> manager.execute(RequestDebtCommand.builder()
						.accountName1(request.getAccountName())
						.accountName2(request.getRecipientName())
						.amount(request.getAmount() - a.getBalance())
						.build()));

	}

	private TransferResult transfer(TransferCommand request, RequestDebtResult requestDebt,
			Collection<ReduceDebtResult> paidDebts) {
		var acc = manager.execute(new GetAccountQuery(request.getAccountName()));
		if (request.getAmount() > 0) {
			var recAcc = manager.execute(new GetAccountQuery(request.getRecipientName()));

			acc.setBalance(acc.getBalance() - request.getAmount());
			recAcc.setBalance(recAcc.getBalance() + request.getAmount());

			manager.execute(UpdateAccountsCommand.builder()
					.request(UpdateAccountCommand.builder()
							.name(acc.getName())
							.balanceUpdate(BalanceUpdate.of(request.getAmount(), false))
							.build())
					.request(UpdateAccountCommand.builder()
							.name(recAcc.getName())
							.balanceUpdate(BalanceUpdate.of(request.getAmount(), true))
							.build())
					.build());
		}
		return TransferResult.builder()
				.accountName(request.getAccountName())
				.recipient(request.getRecipientName())
				.amount(request.getAmount())
				.balance(acc.getBalance())
				.paidDebts(paidDebts)
				.requestDebt(requestDebt)
				.build();
	}

	private Collection<ReduceDebtResult> reduceDebtAndReceivable(TransferCommand request) {
		var result = new ArrayList<ReduceDebtResult>();
		return Optional.of(result)
				.flatMap(r -> reduceDebt(request).map(d -> r.addItem(d)))
				.flatMap(r -> reduceReceivable(request).map(d -> r.addItem(d)))
				.orElse(result);
	}

	private Optional<ReduceDebtResult> reduceDebt(TransferCommand request) {
		return Optional.of(request.getAmount())
				.filter(a -> a != 0)
				.flatMap(a -> manager.execute(ReduceDebtCommand.builder()
						.accountName1(request.getAccountName())
						.accountName2(request.getRecipientName())
						.amount(a)
						.build())
						.map(r -> {
							request.setAmount(r.getRemainder());
							return r;
						}));
	}

	private Optional<ReduceDebtResult> reduceReceivable(TransferCommand request) {
		return Optional.of(request.getAmount())
				.filter(a -> a != 0)
				.flatMap(a -> manager.execute(ReduceDebtCommand.builder()
						.accountName1(request.getRecipientName())
						.accountName2(request.getAccountName())
						.amount(a)
						.build())
						.map(r -> {
							request.setAmount(r.getRemainder());
							return r;
						}));
	}
}
