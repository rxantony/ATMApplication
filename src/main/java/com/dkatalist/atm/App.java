package com.dkatalist.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.dkatalist.atm.domain.application.ATMMachine;
import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;
import com.dkatalist.atm.domain.application.SessionDefault;
import com.dkatalist.atm.domain.application.SessionInputHandlerDefault;
import com.dkatalist.atm.domain.application.SessionManagerDefault;
import com.dkatalist.atm.domain.application.SessionManagerInputHandlerDefault;
import com.dkatalist.atm.domain.common.handler.HandlerManagerDefault;
import com.dkatalist.atm.domain.data.AccountRepositoryDefault;
import com.dkatalist.atm.domain.data.OweRepositoryDefault;
import com.dkatalist.atm.domain.service.account.command.createAccount.CreateAccountCommand;
import com.dkatalist.atm.domain.service.account.query.getAccount.GetAccountQuery;
import com.dkatalist.atm.domain.service.atm.command.deposit.DepositCommand;
import com.dkatalist.atm.domain.service.atm.command.owe.ReduceOweFromCommand;
import com.dkatalist.atm.domain.service.atm.command.owe.ReduceOweToCommand;
import com.dkatalist.atm.domain.service.atm.command.owe.RequestOweToCommand;
import com.dkatalist.atm.domain.service.atm.command.transfer.TransferCommand;
import com.dkatalist.atm.domain.service.atm.command.withdraw.WithdrawCommand;
import com.dkatalist.atm.domain.service.atm.query.getOweList.GetOweListQuery;

public final class App {
    private App() {
    }

    public static void main(String[] xargs) throws IOException, URISyntaxException {
        final var args = new String[] { "input_file.txt" };

        // 1. create machine media outpu
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
        MediaInput inputReader = () -> {
            try {
                var line = ireader.readLine();
                if (args.length != 0)
                    System.out.printf("%n$ %s%n", line);
                return line == null ? "exit" : line;
            } catch (IOException ex) {
                return "exit";
            }
        };

        // 2. create machine media input
        var writer = System.console() != null ? System.console().writer()
                : new PrintWriter(new BufferedOutputStream(System.out));
        var inputWriter = new MediaOutput() {
            @Override
            public void writeln(String str) {
                writer.println(str);
            }

            @Override
            public void writelnf(String format, Object... args) {
                writer.println(String.format(format, args));
            }
        };
        // 3. run atm machine
        try {
            runAtmMachine(inputReader, inputWriter);
        } finally {
            reader.close();
        }
    }

    private static void runAtmMachine(MediaInput inputReader, MediaOutput inputWriter) {
        // 4. create repos
        var accRepo = new AccountRepositoryDefault();
        var oweRepo = new OweRepositoryDefault();

        // 5. create owe commands and handler manager
        var oweCmd = new ReduceOweFromCommand(oweRepo
        , new ReduceOweToCommand(oweRepo
            , new RequestOweToCommand(oweRepo, null)));

        var transferCmd = new TransferCommand(accRepo, oweCmd);
        var handlerMgr = new HandlerManagerDefault()
            .registerHandler(new GetAccountQuery(accRepo))
            .registerHandler(new GetOweListQuery(oweRepo))
            .registerHandler(new CreateAccountCommand(accRepo))
            .registerHandler(transferCmd)
            .registerHandler(new WithdrawCommand(accRepo))
            .registerHandler(new DepositCommand(accRepo, oweRepo, transferCmd));

        // 6. create session manager
        var sessionMgr = new SessionManagerDefault(handlerMgr, 
            arg -> new SessionDefault(arg.accountName, handlerMgr, arg.eventLogout,
                session -> new SessionInputHandlerDefault(session, inputWriter)),
            mgr -> new SessionManagerInputHandlerDefault(mgr, inputWriter));

        // 7. create and run atm machine.
        var atm = new ATMMachine(sessionMgr, inputReader);
        atm.run();
    } 
}
