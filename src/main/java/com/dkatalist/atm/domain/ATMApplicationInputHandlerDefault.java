package com.dkatalist.atm.domain;

import java.util.HashMap;
import java.util.stream.Stream;

public class ATMApplicationInputHandlerDefault implements InputHandler {
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
    public void handle(String input) throws AccountNotExistsException{
		String[] args = input.replaceAll("\\s+", " ").split(" ");
		String command = args.length == 0 ? "help" : args[0];
        String[] params = Stream.of(args).skip(1).map(String::toLowerCase).toArray(size -> new String[size]);

        try{
            if(command.equals("login")){
                String userName =  params[0];
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
        catch(IndexOutOfBoundsException ex){
            output.writeln(commandInfos.get(command));
        }
        catch(IllegalArgumentException ex){
            output.writeln(ex.getMessage());
        }
    }
    
}
