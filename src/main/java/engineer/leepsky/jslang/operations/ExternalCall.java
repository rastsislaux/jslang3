package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class ExternalCall implements Operation {
    private String command;

    public ExternalCall(String command) {
        this.command = command;
    }

    static class ExternalCallException extends RuntimeException { }

    @Override
    public void execute(Stack stack) {
        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.directory(new File("."));
        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            System.out.println("Error while calling an external method: " + e.getMessage());
            System.exit(1);
        }
        System.out.println(p.inputReader().lines().collect(Collectors.joining("\n")));
        if (p.exitValue() != 0) throw new ExternalCallException();
    }
}
