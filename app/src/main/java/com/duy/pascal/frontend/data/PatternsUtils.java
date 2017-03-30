package com.duy.pascal.frontend.data;

import java.util.regex.Pattern;

/**
 * Created by Duy on 11-Feb-17.
 */

public class PatternsUtils {


    //Words
    public static final Pattern line = Pattern.compile(".*\\n");
    public static final Pattern numbers = Pattern.compile(
            "\\b(\\d*[.]?\\d+)\\b");
    public static final Pattern keywords = Pattern.compile(
            "\\b(uses|const|do|for|while|if|else|in|case|" +
                    "and|array|begin|case|div|" +
                    "downto|function|to|mod|not|of|or|" +
                    "procedure|program|repeat|until|shl|shr|string|" +
                    "then|type|var|while|end|" +
                    "function|var|case|array|shl|shr|" +
                    "true|false|boolean)\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern functions = Pattern.compile("\\b(sin|cos|sqrt|abs|floor|ceil|length|new|random|round|" +
            "exp|tan)\\b");

    public static final Pattern comments = Pattern.compile("(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)" +
            "|(\\{(?:.|[\\n\\r])*?\\})" +
            "|((\\(\\*)(?:.|[\\n\\r])*?(\\*\\)))");
    public static final Pattern symbols = Pattern.compile("[+\\-*=<>/:)(\\]\\[;]");
    public static final Pattern trailingWhiteSpace = Pattern.compile("[\\t ]+$", Pattern.MULTILINE);

    public static final Pattern uses = Pattern.compile(
            "(uses)(.*?);", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern readln = Pattern.compile(
            "(readln)\\s+;|(readln);", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern waitEnter = Pattern.compile(
            "\\b(waitEnter)\\b", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    public static final Pattern general_strings = Pattern.compile("'(.*?)'");


}
