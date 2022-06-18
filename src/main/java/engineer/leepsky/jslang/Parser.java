package engineer.leepsky.jslang;

import engineer.leepsky.jslang.operations.*;

import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static engineer.leepsky.jslang.Type.*;

public class Parser {

    // TODO: rework std
    private static Path LIBS_PATH = Path.of("/home/rostislove/Git/jslang3/slang_std_cpp/build");

    private Parser() { }

    private static Map<String, Type> stringTypeMap = Map.ofEntries(
            Map.entry("int8", INT8),
            Map.entry("int16", INT16),
            Map.entry("int32", INT32),
            Map.entry("int64", INT64),
            Map.entry("float32", FLOAT32),
            Map.entry("float64", FLOAT64),
            Map.entry("bool", BOOLEAN),
            Map.entry("any", ANY),
            Map.entry("void", VOID),
            Map.entry("string", STRING)
    );

    static class SlangCompilerError extends RuntimeException { }

    static class NoSuchTypeError extends SlangCompilerError { }

    static class OpParsingError extends SlangCompilerError { }

    public static Type getTypeFromString(String str) {
        if (!stringTypeMap.containsKey(str))
            throw new NoSuchTypeError();
        return stringTypeMap.get(str);
    }

    private static final Pattern functionPattern
            = Pattern.compile("fn +(.+?) *\\((.*?)\\) *(.+?) *-> *(.+?) *\\{([^}]+)}",
            Pattern.MULTILINE | Pattern.DOTALL);

    private static final Pattern externalPattern
            = Pattern.compile("external\\s+([^ ]+)\\s+(.+)->(.*):\\s*([^ ]+)\\s*;",
            Pattern.MULTILINE);


    private static final Pattern intPattern
            = Pattern.compile("\\d+");

    private static final Pattern floatPattern
            = Pattern.compile("\\d+.\\d+");

    private static List<String> splitNStrip(String line, String regex) {
        List<String> result = new ArrayList<>();
        String[] arr = line.split(regex);
        for (String word : arr) {
            result.add(word.strip());
        }
        return result;
    }

    static class NoSuchExternalMethod extends RuntimeException { }

    private static List<Operation> parseOperations(String body, Map<String, ExternalCall> externalCallMap) {
        List<Operation> operations = new ArrayList<>();
        String[] ops = body.split(";");
        for (String op : ops) {
            op = op.strip();
            if (op.equals("")) continue;
            if (op.equals("drop")) operations.add(new DropOp());
            else if (op.charAt(op.length() - 1) == '!') {
                String externalName = op.substring(0, op.length() - 1);
                ExternalCall externalCall = externalCallMap.get(externalName);
                if (externalCall == null) throw new NoSuchExternalMethod();
                operations.add(externalCall);
            }
            else {
                if (intPattern.matcher(op).matches())
                    operations.add(new PushOp(new Variable(INT64, Integer.parseInt(op))));
                else if (floatPattern.matcher(op).matches())
                    operations.add(new PushOp(new Variable(FLOAT64, Double.parseDouble(op))));
                else if (op.equals("true") || op.equals("false"))
                    operations.add(new PushOp(new Variable(BOOLEAN, Boolean.parseBoolean(op))));
                else throw new OpParsingError();
            }
        }
        return operations;
    }

    public static Map<String, Function> parseFunctions(String src, Map<String, ExternalCall> externalCallMap) {
        Map<String, Function> functionMap = new HashMap<>();
        Matcher matcher = functionPattern.matcher(src);
        while (matcher.find()) {
            String name = matcher.group(1);
            List<String> namespaces = splitNStrip(matcher.group(2), ",");
            List<Type> inTypes = new ArrayList<>();
            for (String typename : splitNStrip(matcher.group(3), ",")) {
                inTypes.add(getTypeFromString(typename));
            }
            List<Type> retTypes = new ArrayList<>();
            for (String typename : splitNStrip(matcher.group(4), ",")) {
                retTypes.add(getTypeFromString(typename));
            }
            List<Operation> body = parseOperations(matcher.group(5), externalCallMap);
            functionMap.put(name, new Function(name, namespaces, inTypes, retTypes, body));
        }
        return functionMap;
    }

    public static Map<String, ExternalCall> parseExternals(String src) {
        Map<String, ExternalCall> externalCallMap = new HashMap<>();
        Matcher matcher = externalPattern.matcher(src);
        while (matcher.find()) {
            String name = matcher.group(1);
            String[] inTypes = matcher.group(2).split(",");
            List<Type> inTypesList = new ArrayList<>();
            for (String inType : inTypes) {
                Type type = stringTypeMap.get(inType.strip());
                if (type == null) throw new NoSuchTypeError();
                inTypesList.add(type);
            }
            String outTypes = matcher.group(3); // currently unused
            String pathToExe = matcher.group(4);
            externalCallMap.put(
                    name,
                    new ExternalCall(
                            LIBS_PATH + "/" + pathToExe,
                            inTypesList,
                            List.of(VOID))
            );
        }
        return externalCallMap;
    }

}
