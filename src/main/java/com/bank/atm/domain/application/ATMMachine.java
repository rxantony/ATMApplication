package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;

public class ATMMachine {
    private final MediaInput input;
    private final MediaOutput output;
    private final SessionManager sessionMgr;

    public ATMMachine(SessionManager sessionMgr, MediaInput input, MediaOutput output) {
        Guard.validateArgNotNull(sessionMgr, "sessionMgr");
        Guard.validateArgNotNull(input, "input");
        Guard.validateArgNotNull(output, "output");
        this.sessionMgr = sessionMgr;
        this.input = input;
        this.output = output;
    }

    public void run() {
        output.writeln("Welcome to atm.");
        sessionMgr.getInputHandler().showCommands();

        while (true) {
            var session = sessionMgr.getSession();
            var line = input.readLine().trim();
            if (line.equals("exit")) {
                if (session != null)
                    session.logout();
                return;
            }
            AbstractInputHandler handler;
            if (session == null)
                handler = sessionMgr.getInputHandler();
            else
                handler = session.getInputHandler();
            handler.handle(line);
        }
    }
}
