package com.dkatalist.atm.domain;

import java.util.HashMap;

public class ATMApplicationInputHandlerDefault extends AbstractInputHandler {
    private ATMApplication atm;
    private MediaOutput output;
    private HashMap<String, String> commandInfos = new HashMap<>();

    public ATMApplicationInputHandlerDefault(ATMApplication atm, MediaOutput output){
        Guard.validateArgNotNull(atm, "atm");
        Guard.validateArgNotNull(output, "output");
        this.atm = atm;
        this.output = output;
        initCommandInfos();
    }

    protected void initCommandInfos(){
        commandInfos.put("login", "login [string accountName]");
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
            if(command.equals("login")){
                String userName =  args[0];
                atm.login(userName);
                Session session = atm.getSession();
                output.writeln(String.format("Hello, %s!",userName));
                output.writeln(String.format("Your balance is $%d", session.getAccount().getSaving()));

            }
            else {
                output.writeln("available commands:");
                commandInfos.values().forEach(c-> output.writeln(c));
            }
        }
        catch(AccountNotExistsException ex){
            showError(ex);
        }
    }
    
}
