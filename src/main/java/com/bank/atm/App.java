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
import com.bank.atm.domain.application.SessionDefault;
import com.bank.atm.domain.application.SessionInputHandlerDefault;
import com.bank.atm.domain.application.SessionManagerDefault;
import com.bank.atm.domain.application.SessionManagerInputHandlerDefault;
import com.bank.atm.domain.common.handler.HandlerManagerDefault;
import com.bank.atm.domain.data.AccountRepositoryDefault;
import com.bank.atm.domain.data.OweRepositoryDefault;
import com.bank.atm.domain.service.account.command.createAccount.CreateAccountCommand;
import com.bank.atm.domain.service.account.query.getAccount.GetAccountQuery;
import com.bank.atm.domain.service.user.command.deposit.DepositCommand;
import com.bank.atm.domain.service.user.command.owe.ReduceOweFromCommand;
import com.bank.atm.domain.service.user.command.owe.ReduceOweToCommand;
import com.bank.atm.domain.service.user.command.owe.RequestOweToCommand;
import com.bank.atm.domain.service.user.command.transfer.TransferCommand;
import com.bank.atm.domain.service.user.command.withdraw.WithdrawCommand;
import com.bank.atm.domain.service.user.query.getOweList.GetOweListQuery;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        try(var input = createInput(args); var output = createOutput();){
            runAtmMachine(input, output);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private static MediaInput createInput(String[] args) throws Exception{
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
                        System.out.printf("%n$ %s%n", line);
                    return line == null ? "exit" : line;
                } catch (IOException ex) {
                    return "exit";
                }
            }

            @Override
            public void close() throws Exception {
                ireader.close();;
            }
        };
    }

    private static MediaOutput createOutput(){
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
        var accRepo = new AccountRepositoryDefault();
        var oweRepo = new OweRepositoryDefault();

        // 2. create owe commands and handler manager
        var oweCmd = new ReduceOweFromCommand(oweRepo,
                new ReduceOweToCommand(oweRepo, new RequestOweToCommand(oweRepo, null)));

        var transferCmd = new TransferCommand(accRepo, oweCmd);
        var handlerMgr = new HandlerManagerDefault()
                .registerHandler(new GetAccountQuery(accRepo))
                .registerHandler(new GetOweListQuery(oweRepo))
                .registerHandler(new CreateAccountCommand(accRepo))
                .registerHandler(transferCmd)
                .registerHandler(new WithdrawCommand(accRepo))
                .registerHandler(new DepositCommand(accRepo, oweRepo, transferCmd));

        // 3. create session manager
        var sessionMgr = new SessionManagerDefault(handlerMgr,
                arg -> new SessionDefault(arg.accountName, handlerMgr, arg.eventLogout,
                        session -> new SessionInputHandlerDefault(session, output)),
                mgr -> new SessionManagerInputHandlerDefault(mgr, output));

        // 4. create and run atm machine.
        var atm = new ATMMachine(sessionMgr, input, output);
        atm.run();
    }
}
