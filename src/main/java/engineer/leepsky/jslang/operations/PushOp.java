package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;
import engineer.leepsky.jslang.Variable;

import java.util.Objects;

public class PushOp implements Operation {

    private Variable variable;

    public PushOp(Variable variable) {
        this.variable = variable;
    }

    @Override
    public void execute(Stack stack) {
        stack.add(variable);
    }

    @Override
    public String toString() {
        return "PushOp{" +
                "variable=" + variable +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PushOp pushOp = (PushOp) o;
        return Objects.equals(variable, pushOp.variable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable);
    }

}
