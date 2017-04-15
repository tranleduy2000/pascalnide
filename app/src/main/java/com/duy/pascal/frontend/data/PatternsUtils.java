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
            "\\b(uses|const|do|for|while|if|else|in|case|and|array|begin|div" +
                    "|downto|to|mod|of" +
                    "|procedure|program|repeat|until|shl|shr" +
                    "|then|type|var|end|function|" +
                    "true|false|" +
                    "|and|or|xor|not|break|exit" +
                    "|integer|byte|word|shortint|smallint|cardinal" +
                    "|string|ansistring" +
                    "|single|real|extended|comp|curreny" +
                    "|longint|int64|qword|longword|dword" +
                    "|boolean" +
                    "|char|text)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern functions = Pattern.compile(
            "\\b(sin|cos|sqrt|length" +
                    "|exp|tan|keyPressed|readKey|delay|random|randomize|inc|dec" +
                    "|ceil|trunc|frac|floor|abs|round|sqr|pred|succ|ln|arctan" +
                    "|odd|int|halt|odd)\\b");

    public static final Pattern comments = Pattern.compile(
            "(//.*)|(/\\*(?:.|[\\n\\r])*?\\*/)" +
                    "|(\\{(?:.|[\\n\\r])*?\\})" +
                    "|((\\(\\*)(?:.|[\\n\\r])*?(\\*\\)))");

    public static final Pattern symbols = Pattern.compile("[+\\-'*=<>/:)(\\]\\[;]");

    public static final Pattern trailingWhiteSpace = Pattern.compile("[\\t ]+$", Pattern.MULTILINE);

    public static final Pattern strings = Pattern.compile("('(.*)')");


}
