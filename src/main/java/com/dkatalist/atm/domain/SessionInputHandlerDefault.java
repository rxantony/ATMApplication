package com.dkatalist.atm.domain;

import java.util.HashMap;
import java.util.stream.Stream;

public class SessionInputHandlerDefault implements InputHandler {
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
    public void handle(String input) throws AccountNotExistsException{
		String[] args = input.replaceAll("\\s+", " ").split(" ");
		String command = args.length == 0 ? "help" : args[0];
        String[] params = Stream.of(args).skip(1).map(String::toLowerCase).toArray(size -> new String[size]);

        try{
            if(command.equals("deposit")){
                int amount =  Integer.parseInt(params[0]);
                session.deposit(amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("withdraw")){
                int amount =  Integer.parseInt(params[0]);
                session.withdraw(amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("transfer")){
                String toAccName =  params[0];
                int amount =  Integer.parseInt(params[1]);
                session.transfer(toAccName, amount);
                Account acc = session.getAccount();
                output.writeln(String.format("Transferred $%d to %s", amount, toAccName));
                output.writeln(String.format("Your balance is $%d", acc.getSaving()));
            }
            else if(command.equals("logout")){
                Account acc = session.getAccount();
                session.logout();
                output.writeln(String.format("Goodbye, %s!", acc.getName()));
            }
            else{
                output.writeln("available commands:");
                commandInfos.values().forEach(c-> output.writeln(c));
            }
        }
        catch(IndexOutOfBoundsException ex){
            output.writeln(commandInfos.get(command));
        }
        catch(SessionBaseException|IllegalArgumentException ex){
            if(ex instanceof AccountNotExistsException) {
                AccountNotExistsException iex =  (AccountNotExistsException) ex;
                Account acc = session.getAccount();
                if(iex.getAccountName().equals(acc.getName()))
                    throw iex;
            }
            output.writeln(ex.getMessage());
        }
    }
}
