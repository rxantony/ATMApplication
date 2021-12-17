package com.dkatalist.atm;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.dkatalist.atm.domain.application.MediaInput;
import com.dkatalist.atm.domain.application.MediaOutput;

public final class App {
    private App() {
    }

    public static void main(String[] xargs) throws IOException, URISyntaxException {
        final var args = new String[] { "input_file.txt" };

        // 1. create machine media outpu
        BufferedReader reader = null;
        if (args.length == 0) {
            reader = System.console() != null ? new BufferedReader(System.console().reader())
                    : new BufferedReader(new InputStreamReader(System.in));
        } else {
            var path = Paths.get(args[0]);
            if (!Files.exists(path)) {
                path = Paths.get(Thread.currentThread().getContextClassLoader().getResource(args[0]).toURI());
            }
            reader = new BufferedReader(new FileReader(path.toFile()));
        }

        final var ireader = reader;
        MediaInput inputReader = () -> {
            try {
                var line = ireader.readLine();
                if (args.length != 0)
                    System.out.printf("%n$ %s%n", line);
                return line == null ? "exit" : line;
            } catch (IOException ex) {
                return "exit";
            }
        };

        // 2. create machine media input
        var writer = System.console() != null ? System.console().writer()
                : new PrintWriter(new BufferedOutputStream(System.out));
        var inputWriter = new MediaOutput() {
            @Override
            public void writeln(String str) {
                writer.println(str);
            }

            @Override
            public void writelnf(String format, Object... args) {
                writer.println(String.format(format, args));
            }
        };
        // 3. run atm machine
        try {
            ATMMachineRunner atmRunner = new CqrsATMMachineRunner();
            atmRunner.run(inputReader, inputWriter);
        } finally {
            reader.close();
        }
    }
}
