package engineer.leepsky.jslang;

import engineer.leepsky.jslang.operations.ExternalCall;
import engineer.leepsky.jslang.operations.Function;

import java.io.Serializable;
import java.util.Map;

public class Executable implements Serializable {

    private Map<String, Function> functionMap;
    private Map<String, ExternalCall> externalCallMap;

    public Executable(String sourceCode) {
        sourceCode = Preprocessor.applyDirectives(sourceCode);
        sourceCode = Preprocessor.removeComments(sourceCode);
        externalCallMap = Parser.parseExternals(sourceCode);
        functionMap = Parser.parseFunctions(sourceCode, externalCallMap);
        // System.out.println(functionMap);
    }

    public void execute() {
        functionMap.get("main").execute(new Stack());
    }

}
