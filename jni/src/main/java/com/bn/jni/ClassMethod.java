package com.bn.jni;

public class ClassMethod {

    public static String callStaticMethod(String str, int i) {
        return "callStaticMethod";
    }

    public String callInstanceMethod(String str, int i) {
        return "callInstanceMethod";
    }
}