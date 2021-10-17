package com.dkatalist.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.dkatalist.atm.domain.ATMApplication;
import com.dkatalist.atm.domain.ATMApplicationDefault;
import com.dkatalist.atm.domain.ATMApplicationInputHandlerDefault;
import com.dkatalist.atm.domain.ATMMachine;
import com.dkatalist.atm.domain.AccountDb;
import com.dkatalist.atm.domain.AccountMemoryDb;
import com.dkatalist.atm.domain.CreateSessionArg;
import com.dkatalist.atm.domain.MediaInput;
import com.dkatalist.atm.domain.MediaOutput;
import com.dkatalist.atm.domain.ObjectFactory;
import com.dkatalist.atm.domain.Session;
import com.dkatalist.atm.domain.SessionDefault;
import com.dkatalist.atm.domain.SessionInputHandlerDefault;

public class App {
    public static void main(String[] args) {

        MediaInput inputReader = new MediaInput(){
            BufferedReader reader =  System.console() != null 
                ? new BufferedReader(System.console().reader())  
                : new BufferedReader(new InputStreamReader(System.in));

            @Override
            public String readLine() {
                try{
                    return reader.readLine();
                }
                catch(IOException ex){
                    return null;
                }
            }
        };

        MediaOutput inputWriter = new MediaOutput(){
            private PrintWriter writer =  System.console() != null 
                ? System.console().writer() 
                : new PrintWriter(new BufferedOutputStream(System.out));

            @Override
            public void writeln(String str) {
                writer.println(str);
            }
        };

        AccountDb db = new AccountMemoryDb();

        ObjectFactory<CreateSessionArg, Session> sessionFactory = arg -> new SessionDefault(arg.accountName, db, arg.eventLogout, session-> new SessionInputHandlerDefault(session, inputWriter));

        ATMApplication app = new ATMApplicationDefault(1, db, sessionFactory,  a-> new ATMApplicationInputHandlerDefault(a, inputWriter));

        ATMMachine machine = new ATMMachine(app, inputReader);
        machine.run();
    }
}
