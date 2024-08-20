package com.bank.atm.domain.application;

import java.util.HashMap;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;

import com.bank.atm.domain.common.Guard;

public class DefaultSessionManagerInputHandler extends AbstractInputHandler {
	private final SessionManager sessionMgr;
	private final MediaOutput output;
	private final HashMap<String, String> commandInfos = new HashMap<>();

	public DefaultSessionManagerInputHandler(SessionManager sessionMgr, MediaOutput output) {
		Guard.validateArgNotNull(sessionMgr, "sessionMgr");
		Guard.validateArgNotNull(output, "output");
		this.sessionMgr = sessionMgr;
		this.output = output;
		initCommandInfos();
	}

	private void initCommandInfos() {
		commandInfos.put("login", "login [string accountName]");
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
		return Optional.of(command)
				.filter(c -> "login".equals(c))
				.map(c -> args[0])
				.map(u -> login(u))
				.orElse(false);
	}

	private boolean login(String userName) {
		var session = sessionMgr.login(userName);
		output
				.writeln(String.format("Hello, %s!", userName))
				.writeln(String.format("Your balance is $%d", session.getAccount().getBalance()));

		var accName = session.getAccount().getName();
		var dbList = session.getDebtList(true);
		Optional.ofNullable(dbList)
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
		return true;
	}

}
