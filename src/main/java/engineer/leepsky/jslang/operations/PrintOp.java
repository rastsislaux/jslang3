package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

public class PrintOp implements Operation {

    PrintStream ps;

    public PrintOp(PrintStream ps) {
        this.ps = ps;
    }

    @Override
    public void execute(Stack stack) {
        ps.print(stack.get(stack.size() - 1).value());
        stack.remove(stack.size() - 1);
    }

    @Override
    public String toString() {
        return "PrintOp{" +
                "ps=" + ps +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintOp printOp = (PrintOp) o;
        return Objects.equals(ps, printOp.ps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ps);
    }
}
