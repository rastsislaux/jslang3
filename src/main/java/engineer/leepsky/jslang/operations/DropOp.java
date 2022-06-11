package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;

public class DropOp implements Operation {
    @Override
    public void execute(Stack stack) {
        stack.remove(stack.size() - 1);
    }
}
