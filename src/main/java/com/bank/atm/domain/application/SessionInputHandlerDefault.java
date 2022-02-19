package com.bank.atm.domain.application;

import static com.bank.atm.domain.common.Utils.streamFrom;

import java.util.HashMap;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.service.OweListResult;
import com.bank.atm.domain.service.ServiceException;
import com.bank.atm.domain.service.TransactionResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;

public class SessionInputHandlerDefault extends AbstractInputHandler {
    private final Session session;
    private final MediaOutput output;
    protected final HashMap<String, String> commandInfos = new HashMap<>();

    public SessionInputHandlerDefault(Session session, MediaOutput output) {
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
        handleInternal("");
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
    protected void handleInternal(String command, String... args) {
        String accName = session.getAccountName();
        try {
            if (command.equals("deposit")) {
                var amount = Integer.parseInt(args[0]);
                var result = session.deposit(amount);
                result.getTransfers().forEach(this::printTransfer);
                printBalance(result);
                printOweInfos(result);

            } else if (command.equals("transfer")) {
                var recipient = args[0];
                var amount = Integer.parseInt(args[1]);
                var result = session.transfer(recipient, amount);
                printTransfer(result);
                printBalance(result);
                printOweInfos(result);

            } else if (command.equals("withdraw")) {
                var amount = Integer.parseInt(args[0]);
                var result = session.withdraw(amount);
                printBalance(result);

            } else if (command.equals("logout")) {
                session.logout();
                output.writelnf("Goodbye, %s!", accName);

            } else {
                output.writeln("Available commands:");
                commandInfos.values().forEach(c -> output.writeln("- " + c));
            }
        } catch (ServiceException ex) {
            showError(ex);
        }
    }

    private void printTransfer(TransferResult result) {
        if (result.getAmount() == 0)
            return;
        output.writelnf("Transferred $%d to %s", result.getAmount(), result.getRecipient());
    }

    private void printBalance(TransactionResult result) {
        output.writelnf("Your balance is $%d", result.getBalance());
    }

    private void printOweInfos(OweListResult result) {
        streamFrom(result.getOwes()).filter(o -> o.getAccount1().equals(result.getAccountName()) && o.getAmount() != 0)
                .forEach(o -> {
                    if (o.getAmount() > 0)
                        output.writelnf("Owed $%d from %s", o.getAmount(), o.getAccount2());
                    else
                        output.writelnf("Owed $%d to %s", -o.getAmount(), o.getAccount2());
                });
    }
}
