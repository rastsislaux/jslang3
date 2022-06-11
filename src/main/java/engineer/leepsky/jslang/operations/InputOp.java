package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;
import engineer.leepsky.jslang.Type;
import engineer.leepsky.jslang.Variable;

import java.io.PrintStream;
import java.io.Reader;
import java.util.Objects;
import java.util.Scanner;

public class InputOp implements Operation {

    @Override
    public void execute(Stack stack) {
        stack.add(new Variable(Type.STRING, new Scanner(System.in).nextLine()));
    }

    @Override
    public String toString() {
        return "InputOp{}";
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
