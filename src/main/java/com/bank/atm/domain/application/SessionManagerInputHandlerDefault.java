package com.bank.atm.domain.application;

import java.util.HashMap;

import com.bank.atm.domain.common.Guard;

public class SessionManagerInputHandlerDefault extends AbstractInputHandler {
    private final SessionManager sessionMgr;
    private final MediaOutput output;
    private final HashMap<String, String> commandInfos = new HashMap<>();

    public SessionManagerInputHandlerDefault(SessionManager sessionMgr, MediaOutput output) {
        Guard.validateArgNotNull(sessionMgr, "sessionMgr");
        Guard.validateArgNotNull(output, "output");
        this.sessionMgr = sessionMgr;
        this.output = output;
        initCommandInfos();
    }

    protected void initCommandInfos() {
        commandInfos.put("login", "login [string accountName]");
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
    protected void handleInternal(String command, String... args) throws Exception {
        if (command.equals("login")) {
            String userName = args[0];
            sessionMgr.login(userName);
            Session session = sessionMgr.getSession();
            output.writeln(String.format("Hello, %s!", userName));
            output.writeln(String.format("Your balance is $%d", session.getAccount().getBalance()));
            session.getOweList().stream().filter(o -> o.getAmount() != 0)
                    .forEach(o -> {
                        if (o.getAmount() > 0) {
                            output.writelnf("Owed %d from %s", o.getAmount(), o.getAccount2());
                        } else {
                            output.writelnf("Owed %d to %s", -o.getAmount(), o.getAccount2());
                        }
                    });
        } else {
            showCommands();
        }
    }

}
