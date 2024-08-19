package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;

public class ATMMachine {
    private final MediaInput input;
    private final MediaOutput output;
    private final SessionManagerFactory sessionMgrFactory;

    public ATMMachine(SessionManagerFactory sessionMgrFactory, MediaInput input, MediaOutput output) {
        Guard.validateArgNotNull(sessionMgrFactory, "sessionMgrFactory");
        Guard.validateArgNotNull(input, "input");
        Guard.validateArgNotNull(output, "output");
        this.sessionMgrFactory = sessionMgrFactory;
        this.input = input;
        this.output = output;
    }

    public void run() {
        output.writeln("Welcome to atm.");
        try (var sessionMgr = sessionMgrFactory.create();) {
            sessionMgr.getInputHandler().showCommands();
            while (true) {
                var line = input.readLine();
                if ("exit".equals(line)) {
                    return;
                }
                if (!sessionMgr.hasActiveSession()) {
                    sessionMgr.getInputHandler().handle(line);
                } else {
                    sessionMgr.getSession().getInputHandler().handle(line);
                }
            }
        } catch (Exception ex) {
            output.writeln(ex.getMessage());
        }
    }
}
