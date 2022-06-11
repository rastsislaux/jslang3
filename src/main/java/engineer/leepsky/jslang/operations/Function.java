package engineer.leepsky.jslang.operations;

import engineer.leepsky.jslang.Stack;
import engineer.leepsky.jslang.Type;

import java.util.List;
import java.util.Objects;

public class Function implements Operation {

    private String name;
    private List<String> namespaces;
    private List<Type> inTypes;
    private List<Type> retTypes;
    private List<Operation> operations;

    public Function(String name,
                    List<String> namespaces,
                    List<Type> inTypes,
                    List<Type> retTypes,
                    List<Operation> operations) {
        this.name = name;
        this.namespaces = namespaces;
        this.inTypes = inTypes;
        this.retTypes = retTypes;
        this.operations = operations;
    }

    @Override
    public void execute(Stack stack) {
        if (inTypes.get(0) != Type.VOID) {
            stack.checkSize(inTypes.size());
            stack.checkTypes(inTypes, name);
        }
        for (Operation op : operations) {
            op.execute(stack);
        }
        if (retTypes.get(0) != Type.VOID) {
            stack.checkSize(retTypes.size());
            stack.checkTypes(retTypes, name);
        }
    }

    public List<String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<String> namespaces) {
        this.namespaces = namespaces;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
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

    @Override
    public String toString() {
        return "Function{" +
                "namespaces=" + namespaces +
                ", inTypes=" + inTypes +
                ", retTypes=" + retTypes +
                ", operations=" + operations +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return Objects.equals(namespaces, function.namespaces) && Objects.equals(inTypes, function.inTypes) && Objects.equals(retTypes, function.retTypes) && Objects.equals(operations, function.operations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespaces, inTypes, retTypes, operations);
    }
}
