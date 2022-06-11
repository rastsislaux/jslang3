package engineer.leepsky.jslang;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Preprocessor {

    private static Pattern directivePattern = Pattern.compile("#(.+?) (.+)");

    private static String commentsPatternString
            = "(?://.*)|(/\\*(?:.|[\\n\\r])*?\\*/)";

    private static String apply(String src, String name, String args) {
        switch (name) {
            case "include" -> {
                try {
                    src = src.replace("#%s %s".formatted(name, args), FileUtils.read(args));
                } catch (IOException e) {
                    System.out.println("Error while applying preprocessor directive: " + e.getMessage());
                    System.exit(1);
                }
            }
            case "define" -> {
                src = src.replace("#%s %s".formatted(name, args), "");
                src = src.replace(args.split(" ")[0], args.split(" ")[1]);
            }
        }
        return src;
    }

    public static String removeComments(String src) {
        return src.replaceAll(commentsPatternString, "");
    }

    public static String applyDirectives(String src) {
        boolean changed = false;
        Matcher matcher = directivePattern.matcher(src);
        while (matcher.find()) {
            changed = true;
            String name = matcher.group(1);
            String args = matcher.group(2);
            src = apply(src, name, args);
        }
        if (changed)
            src = applyDirectives(src);
        return src;
    }

}
