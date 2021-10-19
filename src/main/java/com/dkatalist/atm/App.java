package com.dkatalist.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import com.dkatalist.atm.domain.application.ATMMachine;
import com.dkatalist.atm.domain.application.AbstractInputHandler;
import com.dkatalist.atm.domain.application.CreateSessionArg;
import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;
import com.dkatalist.atm.domain.application.Session;
import com.dkatalist.atm.domain.application.SessionDefault;
import com.dkatalist.atm.domain.application.SessionInputHandlerDefault;
import com.dkatalist.atm.domain.application.SessionManager;
import com.dkatalist.atm.domain.application.SessionManagerDefault;
import com.dkatalist.atm.domain.application.SessionManagerInputHandlerDefault;
import com.dkatalist.atm.domain.common.ObjectFactory;
import com.dkatalist.atm.domain.data.AccountDb;
import com.dkatalist.atm.domain.data.OweDb;
import com.dkatalist.atm.domain.service.ATMService;
import com.dkatalist.atm.domain.service.ATMServiceDefault;
import com.dkatalist.atm.domain.service.AccountService;
import com.dkatalist.atm.domain.service.AccountServiceDefault;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws IOException {

        // 1. create machine media
        BufferedReader reader = null;
        if (args.length == 0) {
            reader = System.console() != null ? new BufferedReader(System.console().reader())
                    : new BufferedReader(new InputStreamReader(System.in));
        } else {
            String path = Thread.currentThread().getContextClassLoader().getResource("input_file.txt").getFile();
            reader = new BufferedReader(new FileReader(path));
        }

        final BufferedReader ireader = reader;
        MediaInput inputMedia = () -> {
            try {
                String line = ireader.readLine();
                if(args.length != 0) 
                    System.out.println("=== input: " + line + " ===");
                return line == null ? "exit" : line;
            } catch (IOException ex) {
                return "exit";
            }
        };

        try{
            runATMMachine(inputMedia);
        }
        finally{
            reader.close();
        }
    }

    private static void runATMMachine(MediaInput inputReader){

        PrintWriter writer = System.console() != null ? System.console().writer() : new PrintWriter(new BufferedOutputStream(System.out));
        MediaOutput inputWriter = new MediaOutput(){
            @Override
            public void writeln(String str) {
                writer.println(str);
            }
            @Override
            public void writef(String format, Object... args) {
                writer.printf(format, args);
            }
        };

        // 2. create db connection
        AccountDb accDb = new AccountDb();
        OweDb oweDb = new OweDb();

        // 3. create service
        ATMService atmService = new ATMServiceDefault(accDb, oweDb);
        AccountService accService = new AccountServiceDefault(accDb);

        // 4. create factories
        ObjectFactory<SessionManager, AbstractInputHandler> atmInputHandlerFactory = a -> new SessionManagerInputHandlerDefault(
                a, inputWriter);

        ObjectFactory<Session, AbstractInputHandler> sessionInputHandlerFactory = session -> new SessionInputHandlerDefault(
                session, inputWriter);

        ObjectFactory<CreateSessionArg, Session> sessionFactory = arg -> new SessionDefault(arg.accountName, atmService,
                accService, arg.eventLogout, sessionInputHandlerFactory);

        // 4. create application
        SessionManager app = new SessionManagerDefault(accService, sessionFactory, atmInputHandlerFactory);

        // 5. create atm machine
        ATMMachine machine = new ATMMachine(app, inputReader);

        // 6. run atm machine
        machine.run();
    }
}
