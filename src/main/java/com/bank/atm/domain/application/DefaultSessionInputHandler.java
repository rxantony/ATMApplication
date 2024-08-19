package com.bank.atm.domain.application;

import static com.bank.atm.domain.common.Utils.streamFrom;

import java.util.HashMap;
import java.util.Optional;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.service.debt.command.reducedebt.ReduceDebtResult;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtResult;

public class DefaultSessionInputHandler extends AbstractInputHandler {
	private final Session session;
	private final MediaOutput output;
	protected final HashMap<String, String> commandInfos = new HashMap<>();

	public DefaultSessionInputHandler(Session session, MediaOutput output) {
		Guard.validateArgNotNull(session, "session");
		Guard.validateArgNotNull(output, "output");
		this.session = session;
		this.output = output;
		initCommandInfos();
	}

	private void initCommandInfos() {
		commandInfos.put("deposit", "deposit [int amount]");
		commandInfos.put("withdraw", "withdraw [int amount]");
		commandInfos.put("transfer", "transfer [string accountName], [int amount]");
		commandInfos.put("logout", "logout");
		commandInfos.put("help", "help");
		commandInfos.put("exit", "exit");
	}

	@Override
	public void showCommands() {
		output.writeln("Available commands:");
		commandInfos.values().forEach(c -> output.writeln("- " + c));
	}

	@Override
	protected void showError(Exception ex) {
		output.writeln(ex.getMessage());
	}

	@Override
	protected void showCommandInfo(String command) {
		output.writeln(commandInfos.get(command));
	}

	@Override
	protected boolean handleInternal(String command, String... args) throws Exception {
		if ("deposit".equals(command)) {
			var amount = Integer.parseInt(args[0]);
			var result = session.deposit(amount);
			printPaidDebts(result.getPaidDebts());
			printBalance(result.getBalance());
			return true;
		}

		if ("transfer".equals(command)) {
			var recipient = args[0];
			var amount = Integer.parseInt(args[1]);
			var result = session.transfer(recipient, amount);
			printPaidDebts(result.getPaidDebts());
			printDebt(result.getRequestDebt());
			printBalance(result.getBalance());
			return true;
		}

		if ("withdraw".equals(command)) {
			var amount = Integer.parseInt(args[0]);
			var result = session.withdraw(amount);
			printBalance(result.getBalance());
			return true;
		}

		if ("logout".equals(command)) {
			var accName = session.getAccount().getName();
			session.logout();
			output.writelnf("Goodbye, %s!", accName);
			return true;
		}

		return false;
	}

	private void printBalance(int balance) {
		output.writelnf("Your balance is $%d", balance);
	}

	private void printDebt(RequestDebtResult debt){
		Optional.ofNullable(debt)
		.ifPresent(d -> output.writelnf("owe $%d to %s", d.getDebt().getAmount(), d.getDebt().getAccountName2()));
	}

	private void printPaidDebts(Iterable<ReduceDebtResult> paidDebts){
		Optional.ofNullable(paidDebts)
			.map(pd -> streamFrom(pd))
			.ifPresent(s-> s.forEach(d->output.writelnf("paid debt $%d to %s", d.getAmount(), d.getDebt().getAccountName2())));
	}
}
