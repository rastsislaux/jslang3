package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;
import engineer.leepsky.jslang.Type;
import engineer.leepsky.jslang.Variable;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import static engineer.leepsky.jslang.Type.*;
import static engineer.leepsky.jslang.Type.STRING;

public class ExternalCall implements Operation {
    private String command;

    private List<Type> inTypes;

    private List<Type> retTypes;

    public ExternalCall(String command, List<Type> inTypes, List<Type> retTypes) {
        this.command = command;
        this.inTypes = inTypes;
        this.retTypes = retTypes;
    }

    public List<Type> getInTypes() {
        return inTypes;
    }

    public void setInTypes(List<Type> inTypes) {
        this.inTypes = inTypes;
    }

    public List<Type> getRetTypes() {
        return retTypes;
    }

    public void setRetTypes(List<Type> retTypes) {
        this.retTypes = retTypes;
    }

    static class ExternalCallException extends RuntimeException { }

    static class ExternCallArgTypeMismatchException extends ExternalCallException { }

    static class ExternalCallOutTypeUnknown extends ExternalCallException { }

    @Override
    public void execute(Stack stack) {

        if (inTypes.get(0) != Type.VOID) {
            stack.checkSize(inTypes.size(), Integer.MAX_VALUE);
            stack.checkTypes(inTypes, command);
        }

        ProcessBuilder pb = new ProcessBuilder(command.split(" "));
        pb.directory(new File("."));

        var ref = new Object() {
            Process p = null;
        };

        try {
            ref.p = pb.start();
        } catch (IOException e) {
            System.out.println("Error while calling an external method: " + e.getMessage());
            System.exit(1);
        }

        for (Type argType : inTypes) {
            if (argType == Type.VOID) break;
            Variable var = stack.pop();
            if (var.type() != argType && argType != ANY) throw new ExternCallArgTypeMismatchException();
            try {
                ref.p.outputWriter().write(var.value().toString() + "\n");
                ref.p.outputWriter().flush();
            } catch (IOException e) {
                System.out.println("Error while passing args to external method: " + e.getMessage());
                System.exit(1);
            }
        }

        ref.p.inputReader().lines().forEachOrdered(line -> {

            String[] spl = line.split(":");
            String typename = spl[0];
            String value = spl[1];

            switch (typename) {
                case "print" -> System.out.println(value);
                case "input" -> {
                    try {
                        ref.p.outputWriter().write(new Scanner(System.in).nextLine() + "\n");
                        ref.p.outputWriter().flush();
                    } catch (IOException e) {
                        System.out.println("Error while passing args to external method: " + e.getMessage());
                        System.exit(1);
                    }
                }
                case "int8" -> stack.add(new Variable(INT8, (byte)Integer.parseInt(value)));
                case "int16" -> stack.add(new Variable(INT16, (short)Integer.parseInt(value)));
                case "int32" -> stack.add(new Variable(INT32, Integer.parseInt(value)));
                case "int64" -> stack.add(new Variable(INT64, Long.parseLong(value)));
                case "float32" -> stack.add(new Variable(FLOAT32, Float.parseFloat(value)));
                case "float64" -> stack.add(new Variable(FLOAT64, Double.parseDouble(value)));
                case "bool" -> stack.add(new Variable(BOOLEAN, Boolean.parseBoolean(value)));
                case "string" -> stack.add(new Variable(STRING, value));
                default -> throw new ExternalCallOutTypeUnknown();
            }
        });

        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (ref.p.exitValue() != 0) throw new ExternalCallException();

        if (retTypes.get(0) != Type.VOID) {
            stack.checkSize(retTypes.size(), Integer.MAX_VALUE);
            stack.checkTypes(retTypes, command);
        }
    }

    @Override
    public String toString() {
        return "ExternalCall{" +
                "command='" + command + '\'' +
                ", inTypes=" + inTypes +
                ", retTypes=" + retTypes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalCall that = (ExternalCall) o;
        return Objects.equals(command, that.command) && Objects.equals(inTypes, that.inTypes) && Objects.equals(retTypes, that.retTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, inTypes, retTypes);
    }
}
