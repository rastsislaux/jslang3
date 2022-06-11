package engineer.leepsky.jslang;

import engineer.leepsky.jslang.operations.ExternalCall;
import engineer.leepsky.jslang.operations.Function;
import engineer.leepsky.jslang.operations.PrintOp;
import engineer.leepsky.jslang.operations.PushOp;

import java.io.IOException;
import java.util.*;

public class Main {

    private static final String MAIN_METHOD_NAME = "main";

    public static void main(String[] args) throws IOException {

        String src = FileUtils.read("test.sc");
        src = Preprocessor.applyDirectives(src);
        src = Preprocessor.removeComments(src);

        Map<String, Function> functionMap = Parser.parseFunctions(src);
        // System.out.println(functionMap);

        Stack stack = new Stack();

        functionMap.get(MAIN_METHOD_NAME).execute(stack);
        // System.out.println(stack);

    }
}