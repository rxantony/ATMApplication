package com.dkatalist.atm.domain;

import java.util.HashMap;

public class SessionInputHandlerDefault extends AbstractInputHandler {
    private Session session;
    private MediaOutput output;
    private HashMap<String, String> commandInfos = new HashMap<>();

    public SessionInputHandlerDefault(Session session, MediaOutput output){
        Guard.validateArgNotNull(session, "session");
        Guard.validateArgNotNull(output, "output");
        this.session = session;
        this.output = output;
        initCommandInfos();
    }

    protected void initCommandInfos(){
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
    protected void handle(String command, String[] args) {
        try{
            if(command.equals("deposit")){
                int amount =  Integer.parseInt(args[0]);
                session.deposit(amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("withdraw")){
                int amount =  Integer.parseInt(args[0]);
                session.withdraw(amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("transfer")){
                String toAccName =  args[0];
                int amount =  Integer.parseInt(args[1]);
                session.transfer(toAccName, amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Transferred $%d to %s", amount, toAccName));
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("logout")){
                session.logout();
                output.writeln(String.format("Goodbye, %s!", session.getAccountName()));
            }
            else{
                output.writeln("available commands:");
                commandInfos.values().forEach(c-> output.writeln(c));
            }
        }
        catch(ATMBaseException ex){
            showError(ex);
        }
        
    }
}
