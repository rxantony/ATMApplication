package com.bank.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.bank.atm.domain.application.ATMMachine;
import com.bank.atm.domain.application.MediaInput;
import com.bank.atm.domain.application.MediaOutput;
import com.bank.atm.domain.application.DefaultSession;
import com.bank.atm.domain.application.DefaultSessionInputHandler;
import com.bank.atm.domain.application.DefaultSessionManager;
import com.bank.atm.domain.application.SessionManagerFactory;
import com.bank.atm.domain.application.DefaultSessionManagerInputHandler;
import com.bank.atm.domain.common.handler.DefaultHandlerManager;
import com.bank.atm.domain.data.DefaultAccountRepository;
import com.bank.atm.domain.data.DefaultOweRepository;
import com.bank.atm.domain.service.account.command.createaccount.CreateAccountCommand;
import com.bank.atm.domain.service.account.query.getaccount.GetAccountQuery;
import com.bank.atm.domain.service.user.command.deposit.DepositCommand;
import com.bank.atm.domain.service.user.command.owe.ReduceOweFromCommand;
import com.bank.atm.domain.service.user.command.owe.ReduceOweToCommand;
import com.bank.atm.domain.service.user.command.owe.RequestOweToCommand;
import com.bank.atm.domain.service.user.command.transfer.TransferCommand;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawCommand;
import com.bank.atm.domain.service.user.query.getowelist.GetOweListQuery;

public final class App {
    private App() {}

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
                    return line == null ? "exit" : line;
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
            public void writeln(String str) {
                writer.println(str);
            }

            @Override
            public void writelnf(String format, Object... args) {
                writer.println(String.format(format, args));
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
        var oweRepo = new DefaultOweRepository();

        var handlerMgr = new DefaultHandlerManager();
        handlerMgr.registerHandler(new GetAccountQuery(accRepo))
                .registerHandler(new GetOweListQuery(oweRepo))
                .registerHandler(new RequestOweToCommand(oweRepo,
                        new ReduceOweFromCommand(oweRepo,
                                new ReduceOweToCommand(oweRepo, null))))
                .registerHandler(new CreateAccountCommand(accRepo))
                .registerHandler(new TransferCommand(accRepo, handlerMgr))
                .registerHandler(new WithdrawCommand(accRepo))
                .registerHandler(new DepositCommand(accRepo, oweRepo, handlerMgr));

        // 2. create session manager
        SessionManagerFactory sessionMgrFactory = () -> new DefaultSessionManager(handlerMgr,
                (accName, logoutCallback) -> new DefaultSession(accName, handlerMgr, logoutCallback,
                        session -> new DefaultSessionInputHandler(session, output)),
                sessionMgr -> new DefaultSessionManagerInputHandler(sessionMgr, output));

        // 3. create and run atm machine.
        var atm = new ATMMachine(sessionMgrFactory, input, output);
        atm.run();
    }
}
