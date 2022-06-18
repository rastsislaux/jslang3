package engineer.leepsky.jslang;

import engineer.leepsky.jslang.operations.ExternalCall;
import engineer.leepsky.jslang.operations.Function;
import engineer.leepsky.jslang.operations.PushOp;

import java.io.IOException;
import java.util.*;

public class Main {

    private static final String MAIN_METHOD_NAME = "main";

    public static void main2(String[] args) {

        Stack stack = new Stack();

        Function func = new Function(
                "",
                List.of(""),
                List.of(Type.ANY),
                List.of(Type.VOID),
                Arrays.asList(
                        /* new ExternalCall(
                                "/home/rostislove/Git/jslang3/slang_std_cpp/build/io/input",
                                List.of(Type.VOID),
                                List.of(Type.STRING)
                        ), */
                        new PushOp(new Variable(Type.INT64, 128)),
                        new ExternalCall(
                                "/home/rostislove/Git/jslang3/slang_std_cpp/build/io/print",
                                List.of(Type.ANY),
                                List.of(Type.VOID)
                        )
                )
            );

        func.execute(stack);
    }

    public static void main(String[] args) throws IOException {

        String src = FileUtils.read("test.sc");

        Executable executable = new Executable(src);
        executable.execute();

    }
}