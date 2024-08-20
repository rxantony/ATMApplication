package com.bank.atm.domain.application;

import static com.bank.atm.domain.common.Utils.streamFrom;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.data.dto.DebtDto;
import com.bank.atm.domain.service.user.command.reducedebt.ReduceDebtResult;
import com.bank.atm.domain.service.user.command.requestdebt.RequestDebtResult;

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
		commandInfos.put("debts", "debts");
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
			printReducedDebts(result.getPaidDebts());
			printBalance(result.getBalance());
			return true;
		}

		if ("transfer".equals(command)) {
			var recipient = args[0];
			var amount = Integer.parseInt(args[1]);
			var result = session.transfer(recipient, amount);
			printReducedDebt(result.getReducedDebt());
			printRequestDebt(result.getRequestedDebt());
			printBalance(result.getBalance());
			return true;
		}

		if ("withdraw".equals(command)) {
			var amount = Integer.parseInt(args[0]);
			var result = session.withdraw(amount);
			printBalance(result.getBalance());
			return true;
		}

		if ("debts".equals(command)) {
			var result = session.getDebtList(true);
			printDebts(result);
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

	private void printDebts(Collection<DebtDto> debts) {
		var accName = session.getAccount().getName();
		Optional.ofNullable(debts)
				.filter(ds -> ObjectUtils.isNotEmpty(ds))
				.map(ds -> {
					ds.stream()
							.filter(d -> d.getAccountName1().equals(accName))
							.forEach(d -> {
								ds.stream()
										.filter(d2 -> d2.getAccountName1().equals(d.getAccountName2()))
										.findFirst()
										.ifPresent(d2 -> {
											output.writelnf("%d -> %s : %s", d.getAmount(), d2.getAccountName1(), d2.getAmount());
										});
							});
					return output;
				})
				.orElseGet(() -> output.writelnf("no debts exists"));
	}

	private void printRequestDebt(RequestDebtResult debt) {
		Optional.ofNullable(debt)
				.ifPresent(d -> output.writelnf("owe $%d to %s", d.getDebt().getAmount(), d.getDebt().getAccountName2()));
	}

	private void printReducedDebt(ReduceDebtResult debt) {
		Optional.ofNullable(debt)
				.ifPresent(d -> output.writelnf("paid debt $%d to %s", d.getAmount(), d.getDebt1().getAccountName2()));
	}

	private void printReducedDebts(Iterable<ReduceDebtResult> debts) {
		Optional.ofNullable(debts)
				.ifPresent(p -> streamFrom(p).forEach(d -> printReducedDebt(d)));
	}
}
