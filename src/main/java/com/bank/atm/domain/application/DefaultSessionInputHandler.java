package com.bank.atm.domain.application;

import static com.bank.atm.domain.common.Utils.streamFrom;

import java.util.HashMap;

import com.bank.atm.domain.common.Guard;
import com.bank.atm.domain.service.OweListResult;
import com.bank.atm.domain.service.user.command.AbstractTransactionResult;
import com.bank.atm.domain.service.user.command.transfer.TransferResult;

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
        var accName = session.getAccountName();

        if (command.equals("deposit")) {
            var amount = Integer.parseInt(args[0]);
            var result = session.deposit(amount);
            result.getTransfers().forEach(this::printTransfer);
            printBalance(result);
            printOweInfos(result);
            return true;

        } 

        if (command.equals("transfer")) {
            var recipient = args[0];
            var amount = Integer.parseInt(args[1]);
            var result = session.transfer(recipient, amount);
            printTransfer(result);
            printBalance(result);
            printOweInfos(result);
            return true;

        } 
        
        if (command.equals("withdraw")) {
            var amount = Integer.parseInt(args[0]);
            var result = session.withdraw(amount);
            printBalance(result);
            return true;

        } 
        
        if (command.equals("logout")) {
            session.logout();
            output.writelnf("Goodbye, %s!", accName);
            return true;
        }

        return false;
    }

    private void printTransfer(TransferResult result) {
        if (result.getAmount() == 0)
            return;
        output.writelnf("Transferred $%d to %s", result.getAmount(), result.getRecipient());
    }

    private void printBalance(AbstractTransactionResult result) {
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
