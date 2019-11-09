package com.soecode.lyf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPattern {
    public static void main(String[] args) {
        String classPath = "java.util.List<com.soecode.lyf.entity.Book>";
        Pattern pattern = Pattern.compile("^[a-zA-Z\\.]+<(.+)>$");
        Matcher matcher = pattern.matcher(classPath);
        //matcher.find();
        System.out.println(matcher.group(1));
    }
}
