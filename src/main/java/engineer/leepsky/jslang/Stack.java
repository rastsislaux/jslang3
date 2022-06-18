package engineer.leepsky.jslang;

import java.util.ArrayList;
import java.util.List;

public class Stack extends ArrayList<Variable> {

    static class SlangStackError extends RuntimeException {
        protected SlangStackError() {
            super();
        }

        protected SlangStackError(String string) {
            super(string);
        }
    }

    static class NotEnoughArgumentsError extends SlangStackError {
        protected NotEnoughArgumentsError() {
            super();
        }

        protected NotEnoughArgumentsError(String string) {
            super(string);
        }
    }

    static class ArgumentTypeMismatchError extends SlangStackError {
        protected ArgumentTypeMismatchError() {
            super();
        }

        protected ArgumentTypeMismatchError(String string) {
            super(string);
        }
    }

    public void checkSize(int size) {
        checkSize(size, size);
    }

    public void checkSize(int min, int max) {
        if (this.size() < min || this.size() > max)
            throw new NotEnoughArgumentsError(
                    "Expected number of args is between `%d` and `%d`, but got `%d` instead."
                            .formatted(
                                    min, max, size()
                            ));
    }

    public Variable pop() {
        Variable var = this.get(this.size() - 1);
        this.remove(this.size() - 1);
        return var;
    }

    public void checkTypes(List<Type> types, String where) {
        int i = this.size();

        for (Type type : types) {
            Type thisType = this.get(--i).type();
            if (type != thisType && type != Type.ANY)
                throw new ArgumentTypeMismatchError(
                        "Expected argument/return value of type `%s` for function `%s`, but got `%s` instead."
                                .formatted(
                                        type, where, this.get(i).type()
                                ));
        }
    }

}
