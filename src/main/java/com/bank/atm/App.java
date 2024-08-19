package com.bank.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.validation.Validation;

import com.bank.atm.domain.application.ATMMachine;
import com.bank.atm.domain.application.MediaInput;
import com.bank.atm.domain.application.MediaOutput;
import com.bank.atm.domain.application.DefaultSession;
import com.bank.atm.domain.application.DefaultSessionInputHandler;
import com.bank.atm.domain.application.DefaultSessionManager;
import com.bank.atm.domain.application.SessionManagerFactory;
import com.bank.atm.domain.application.DefaultSessionManagerInputHandler;
import com.bank.atm.domain.common.handler.DefaultRequestHandlerManager;
import com.bank.atm.domain.data.repository.DefaultAccountRepository;
import com.bank.atm.domain.data.repository.DefaultDebRepository;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountCommandHandler;
import com.bank.atm.domain.service.account.command.updateaccount.UpdateAccountCommandHandler;
import com.bank.atm.domain.service.account.command.updateaccounts.UpdateAccountsCommandHandler;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountQueryHandler;
import com.bank.atm.domain.service.account.query.getoptaccount.GetOptAccountQueryHandler;
import com.bank.atm.domain.service.debt.command.adddebts.AddDebtsCommandHandler;
import com.bank.atm.domain.service.debt.command.reducedebt.ReduceDebtCommandHandler;
import com.bank.atm.domain.service.debt.command.reducedebts.ReduceDebtsCommandHandler;
import com.bank.atm.domain.service.debt.command.requestdebt.RequestDebtCommandHandler;
import com.bank.atm.domain.service.debt.command.updatedebts.UpdateDebtsCommandHandler;
import com.bank.atm.domain.service.debt.query.getdebt.GetOptDebtQueryHandler;
import com.bank.atm.domain.service.debt.query.getdebtlist.GetDebtListQueryHandler;
import com.bank.atm.domain.service.mapper.AccountMapper;
import com.bank.atm.domain.service.mapper.DebtMapper;
import com.bank.atm.domain.service.user.command.deposit.DepositCommandHandler;
import com.bank.atm.domain.service.user.command.transfer.TransferCommandHandler;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawCommandHandler;

public final class App {
	private App() {
	}

	public static void main(String[] args) {
		try (var input = createInput(args); var output = createOutput();) {
			runAtmMachine(input, output);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static MediaInput createInput(String[] args) throws Exception {
		BufferedReader reader = null;
		if (args.length == 0) {
			reader = System.console() != null ? new BufferedReader(System.console().reader())
					: new BufferedReader(new InputStreamReader(System.in));
		} else {
			var path = Paths.get(args[0]);
			if (!Files.exists(path)) {
				path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(args[0]).toURI());
			}
			reader = new BufferedReader(new FileReader(path.toFile()));
		}

		final var ireader = reader;
		return new MediaInput() {
			@Override
			public String readLine() {
				try {
					var line = ireader.readLine();
					if (args.length != 0)
						System.out.format("%n$ %s%n", line);
					return line == null ? "exit" : line.trim();
				} catch (IOException ex) {
					return "exit";
				}
			}

			@Override
			public void close() throws Exception {
				ireader.close();
			}
		};
	}

	private static MediaOutput createOutput() {
		var writer = System.console() != null ? System.console().writer()
				: new PrintWriter(new BufferedOutputStream(System.out));
		return new MediaOutput() {
			@Override
			public MediaOutput writeln(String str) {
				writer.println(str);
				return this;
			}

			@Override
			public MediaOutput writelnf(String format, Object... args) {
				writer.println(String.format(format, args));
				return this;
			}

			@Override
			public void close() throws Exception {
				writer.close();
			}
		};
	}

	private static void runAtmMachine(MediaInput input, MediaOutput output) {
		// 1. create repos
		var accRepo = new DefaultAccountRepository();
		var debtRepo = new DefaultDebRepository();
		var manager = new DefaultRequestHandlerManager(Validation.buildDefaultValidatorFactory().getValidator());
		manager.register(new GetAccountQueryHandler(accRepo, AccountMapper.INSTANCE))
				.register(new GetOptAccountQueryHandler(accRepo, AccountMapper.INSTANCE))
				.register(new GetOptDebtQueryHandler(debtRepo, DebtMapper.INSTANCE))
				.register(new GetDebtListQueryHandler(debtRepo, DebtMapper.INSTANCE))
				.register(new CreateAccountCommandHandler(accRepo, AccountMapper.INSTANCE))
				.register(new UpdateAccountCommandHandler(accRepo, AccountMapper.INSTANCE))
				.register(new UpdateAccountsCommandHandler(accRepo, AccountMapper.INSTANCE))
				.register(new AddDebtsCommandHandler(debtRepo, DebtMapper.INSTANCE))
				.register(new DepositCommandHandler(manager))
				.register(new ReduceDebtCommandHandler(manager))
				.register(new ReduceDebtsCommandHandler(manager, debtRepo, DebtMapper.INSTANCE))
				.register(new RequestDebtCommandHandler(manager, debtRepo, DebtMapper.INSTANCE))
				.register(new TransferCommandHandler(manager, AccountMapper.INSTANCE))
				.register(new UpdateDebtsCommandHandler(debtRepo, DebtMapper.INSTANCE))
				.register(new WithdrawCommandHandler(manager, AccountMapper.INSTANCE));

		// 2. create session manager
		SessionManagerFactory sessionMgrFactory = () -> new DefaultSessionManager(manager,
				(accName, cb) -> new DefaultSession(accName, manager, cb, 
				(session) -> new DefaultSessionInputHandler(session, output)),
				(sessionMgr) -> new DefaultSessionManagerInputHandler(sessionMgr, output));

		// 3. create and run atm machine.
		var atm = new ATMMachine(sessionMgrFactory, input, output);
		atm.run();
	}
}
