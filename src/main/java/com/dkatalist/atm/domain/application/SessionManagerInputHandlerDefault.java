package com.dkatalist.atm.domain.application;

import java.util.HashMap;

import com.dkatalist.atm.domain.common.Guard;
import com.dkatalist.atm.domain.service.ServiceException;

public class SessionManagerInputHandlerDefault extends AbstractInputHandler {
    private SessionManager app;
    private MediaOutput output;
    private HashMap<String, String> commandInfos = new HashMap<>();

    public SessionManagerInputHandlerDefault(SessionManager app, MediaOutput output) {
        Guard.validateArgNotNull(app, "app");
        Guard.validateArgNotNull(output, "output");
        this.app = app;
        this.output = output;
        initCommandInfos();
    }

    protected void initCommandInfos() {
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
    protected void handle(String command, String... args) {
        try {
            if (command.equals("login")) {
                String userName = args[0];
                app.login(userName);
                Session session = app.getSession();
                output.writeln(String.format("Hello, %s!", userName));
                output.writeln(String.format("Your balance is $%d", session.getAccount().getSaving()));
                session.getOweList().stream().filter(o -> o.getAmount() != 0)
                    .forEach(o ->{ 
                        if(o.getAmount() > 0){
                            output.writef("Owed %d from %s%s", o.getAmount(), o.getAccount2(),"\n");
                        }
                        else{
                            output.writef("Owed %d to %s%s", -o.getAmount(), o.getAccount2(),"\n");
                        }
                    }
                );
            } else {
                output.writeln("available commands:");
                commandInfos.values().forEach(c -> output.writeln("- " + c));
            }
        } catch (ServiceException ex) {
            showError(ex);
        }
    }

}
