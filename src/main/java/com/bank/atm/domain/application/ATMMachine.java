package com.bank.atm.domain.application;

import com.bank.atm.domain.common.Guard;

public class ATMMachine {
    private SessionManager sessionMgr;
    private MediaInput input;

    public ATMMachine(SessionManager sessionMgr, MediaInput input) {
        Guard.validateArgNotNull(sessionMgr, "sessionMgr");
        Guard.validateArgNotNull(input, "input");
        this.sessionMgr = sessionMgr;
        this.input = input;
    }

    public void run() {
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
