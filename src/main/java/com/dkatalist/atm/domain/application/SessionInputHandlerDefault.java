package com.dkatalist.atm.domain.application;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.data.Owe;
import com.dkatalist.atm.domain.service.ATMService.DepositResult;
import com.dkatalist.atm.domain.service.ATMService.TransactionResult;
import com.dkatalist.atm.domain.service.ATMService.TransferResult;
import com.dkatalist.atm.domain.service.ServiceException;

public class SessionInputHandlerDefault extends AbstractInputHandler {
    private Session session;
    private MediaOutput output;
    private HashMap<String, String> commandInfos = new HashMap<>();

    public SessionInputHandlerDefault(Session session, MediaOutput output) {
        Guard.validateArgNotNull(session, "session");
        Guard.validateArgNotNull(output, "output");
        this.session = session;
        this.output = output;
        initCommandInfos();
    }

    protected void initCommandInfos() {
        commandInfos.put("deposit", "deposit [int amount]");
        commandInfos.put("withdraw", "withdraw [int amount]");
        commandInfos.put("transfer", "transfer [string accountName], [int amount]");
        commandInfos.put("logout", "logout");
        commandInfos.put("help", "help");
        commandInfos.put("exit", "exit");
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
    protected void handle(String command, String... args) {
        try {
            if (command.equals("deposit")) {
                int amount = Integer.parseInt(args[0]);
                DepositResult result = session.deposit(amount);
                result.transferList.forEach(this::printTransfer);
                printBalance(result);
                printOweInfos(result.transferList.stream().map(o -> o.oweList).flatMap(List<Owe>::stream)
                        .collect(Collectors.toList()));

            } else if (command.equals("transfer")) {
                String recipient = args[0];
                int amount = Integer.parseInt(args[1]);
                TransferResult result = session.transfer(recipient, amount);
                printTransfer(result);
                printBalance(result);
                printOweInfos(result.oweList);

            } else if (command.equals("withdraw")) {
                int amount = Integer.parseInt(args[0]);
                TransactionResult result = session.withdraw(amount);
                printBalance(result);

            } else if (command.equals("logout")) {
                String accName = session.getAccountName();
                session.logout();
                output.writef("Goodbye, %s!%s", accName, "\n");

            } else {
                output.writeln("available commands:");
                commandInfos.values().forEach(c -> output.writeln("- " + c));
            }
        } catch (ServiceException ex) {
            showError(ex);
        }
    }

    private void printTransfer(TransferResult result) {
        output.writef("Transfered $%d to %s%s", result.getAmount(), result.accountName, "\n");
    }

    private void printBalance(TransactionResult result) {
        output.writef("Your balance is $%d%s", result.getSaving(), "\n");
    }

    private void printOweInfos(List<Owe> oweList) {
        oweList.stream().filter(o -> o.getAccount1().equals(session.getAccountName())).forEach(o -> {
            if (o.getAmount() > 0)
                output.writef("Owed $%d fro %s%s", o.getAmount(), o.getAccount2(), "\n");
            else
                output.writef("Owed $%d to %s%s", -o.getAmount(), o.getAccount2(), "\n");
        });
    }
}
