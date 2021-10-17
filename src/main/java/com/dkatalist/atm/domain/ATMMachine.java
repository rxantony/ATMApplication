package com.dkatalist.atm.domain;

public class ATMMachine {
    private ATMApplication app;
    private MediaInput input;

    public ATMMachine (ATMApplication app, MediaInput input){
        Guard.validateArgNotNull(app, "app");
        Guard.validateArgNotNull(input, "input");
        this.app = app;
        this.input = input;
    }

    public void run(){
        while(true){
            Session session = app.getSession();
            String line = input.readLine().trim();
            if(line.equals("exit")){
                session.logout();
                return;
            }

            AbstractInputHandler handler;

            if(session == null)
                handler = app.getInputHandler();
            else 
                handler = session.getInputHandler();
            handler.handle(line);
        }
    }
}
