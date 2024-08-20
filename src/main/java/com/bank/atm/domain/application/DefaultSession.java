package com.bank.atm.domain.application;

import java.util.Collection;
import java.util.function.Consumer;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.common.handler.HandlerExtensions;
import com.bank.atm.domain.common.handler.RequestHandlerManager;
import com.bank.atm.domain.data.dto.AccountDto;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.exception.AccountNotExistsException;
import com.bank.atm.domain.service.account.query.getoptaccount.GetOptAccountQuery;
import com.bank.atm.domain.service.debt.query.getdebtlist.GetDebtListQuery;
import com.bank.atm.domain.service.user.command.deposit.DepositCommand;
import com.bank.atm.domain.service.user.command.deposit.DepositResult;
import com.bank.atm.domain.service.user.command.transfer.TransferCommand;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawCommand;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawResult;

import lombok.experimental.ExtensionMethod;

@ExtensionMethod(HandlerExtensions.class)
public class DefaultSession implements Session {
	private boolean sessionClosed;
	private final String accountName;
	private final RequestHandlerManager manager;
	private final AbstractInputHandler inputHandler;
	private final Consumer<String> logoutCallback;

	public DefaultSession(String accountName, RequestHandlerManager manager, Consumer<String> logoutCallback,
			AbstractInputHandlerFactory<Session> inputHandlerFactory) {

		Guard.validateArgNotNullOrEmpty(accountName, "accountName");
		Guard.validateArgNotNull(manager, "manager");
		Guard.validateArgNotNull(logoutCallback, "logoutCallback");
		Guard.validateArgNotNull(inputHandlerFactory, "inputhandlerFactory");

		this.accountName = accountName;
		this.manager = manager;
		this.logoutCallback = logoutCallback;
		this.inputHandler = inputHandlerFactory.create(this);
	}

	@Override
	public AccountDto getAccount() {
		validateSessionExpired();
		return manager.execute(new GetOptAccountQuery(accountName))
				.orElseThrow(() -> {
					logout();
					return AccountNotExistsException.create(accountName);
				});
	}

	@Override
	public void logout() {
		validateSessionExpired();
		sessionClosed = true;
		logoutCallback.accept(accountName);
	}

	@Override
	public DepositResult deposit(int amount) {
		validateSessionExpired();
		return manager.execute(new DepositCommand(accountName, amount));
	}

	@Override
	public TransferResult transfer(String recipient, int amount) {
		validateSessionExpired();
		return manager.execute(new TransferCommand(accountName, recipient, amount));
	}

	@Override
	public WithdrawResult withdraw(int amount) {
		validateSessionExpired();
		return manager.execute(new WithdrawCommand(accountName, amount));
	}

	@Override
	public Collection<DebtDto> getDebtList(boolean all) {
		validateSessionExpired();
		return manager.execute(new GetDebtListQuery(accountName, all));
	}

	@Override
	public AbstractInputHandler getInputHandler() {
		validateSessionExpired();
		return inputHandler;
	}

	private void validateSessionExpired() {
		if (sessionClosed)
			throw new SessionExpiredException();
	}

	@Override
	public void close() throws Exception {
		if (!sessionClosed)
			logout();
	}
}
