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
            String line = input.readLine().trim();
            if(line.equals("exit")) 
                return;
            Session session = app.getSession();
            try{
                InputHandler handler;
                if(session == null)
                    handler = app.getInputHandler();
                else 
                    handler = session.getInputHandler();
                handler.handle(line);
            }
            catch(AccountNotExistsException ex){
                if(session != null)
                    session.logout();
            }
        }
    }
}
