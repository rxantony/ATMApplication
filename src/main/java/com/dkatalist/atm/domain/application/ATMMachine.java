package com.dkatalist.atm.domain.application;

import com.dkatalist.atm.domain.common.Guard;

public class ATMMachine {
    private SessionManager manager;
    private MediaInput input;

    public ATMMachine (SessionManager manager, MediaInput input){
        Guard.validateArgNotNull(manager, "manager");
        Guard.validateArgNotNull(input, "input");
        this.manager = manager;
        this.input = input;
    }

    public void run(){
        while(true){
            Session session = manager.getSession();
            String line = input.readLine().trim();
            if(line.equals("exit")){
                if(session != null)
                    session.logout();
                return;
            }
            AbstractInputHandler handler;
            if(session == null)
                handler = manager.getInputHandler();
            else 
                handler = session.getInputHandler();
            handler.handle(line);
        }
    }
}
