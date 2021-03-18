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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
//        testOperater();
//        Log.e("Test",removeContinuousDuplicate("aabbcceeffssgag"));
//        testPrint();
        testStack();
    }

    public void testOperater() {
        StringBuffer a= new StringBuffer("A");
        StringBuffer b= new StringBuffer("B");
        operater(a,b);
        Log.e("Test",a+","+b);

    }


    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        while(!stack1.empty()){
            stack2.push(stack1.pop());
        }
        stack1.push(node);
        while(!stack2.empty()){
            stack1.push(stack2.pop());
        }
    }

    public int pop() {
        return stack1.pop();
    }


    public void testStack() {
        for(int i=0;i<10;i++) {
            push(i);
        }

        for(int i=0;i<stack1.size();i++) {
            Log.e("Test","num:"+pop());
        }

    }


    Lock lock = new ReentrantLock();

    class ThreadA1 extends  Thread {
        String label="";
        ThreadA1(String label) {
            this.label = label;
        }
        @Override
        public void run() {
            for(int i = 0;i<10;i++) {
                lock.lock();
                Log.e("Test","Label:"+label);
                lock.unlock();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void testPrint() {
        ThreadA1 a = new ThreadA1("1");
        ThreadA1 b = new ThreadA1("2");

        a.start();
        b.start();


    }
    String removeContinuousDuplicate(String input) {
        StringBuffer a = new StringBuffer();
        String lastChar = "";
        for(int i = 0;i<input.length();i++) {
             String currentChar = input.substring(i,i+1);
             if(!lastChar.equals(currentChar)) {
                 a.append(currentChar);
                 lastChar = currentChar;
             }
        }

        return a.toString();
    }

    public static void deleteStringGt(List<String> input, int n) {
        List result = new ArrayList();
        for(int i=0;i<input.size();i++) {
            String t = input.get(i);
            if(t.length()<n) {
                result.add(t);
            }

        }
        input.clear();
        input.addAll(result);
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
