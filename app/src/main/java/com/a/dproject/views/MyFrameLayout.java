package com.a.dproject.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyFrameLayout extends FrameLayout {
    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50,50,100,paint);
//        testFoo();
        testOperater();
    }

    public void testOperater() {
        StringBuffer a= new StringBuffer("A");
        StringBuffer b= new StringBuffer("B");
        operater(a,b);
        Log.e("Test",a+","+b);

    }

    public void operater(StringBuffer x,StringBuffer y) {
        x.append(y);
        y=x;
    }

    public boolean foo(String chr) {
        Log.e("Test",chr);
        return true;
    }

    public void testFoo() {
        int i=0;
        for(foo("A");foo("B")&&(i<2);foo("C")) {
            i++;
            foo("D");
        }
    }
}
