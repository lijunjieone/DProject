package com.bn.jni;

import android.content.Context;
import android.widget.Toast;

public class ClassMethod {

    public static String callStaticMethod(String str, int i) {
        return "callStaticMethod";
    }

    public static String callStaticMethod2(Context context, String str, int i) {
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
        return "callStaticMethod2";
    }

    public String callInstanceMethod(String str, int i) {
        return "callInstanceMethod";
    }
}