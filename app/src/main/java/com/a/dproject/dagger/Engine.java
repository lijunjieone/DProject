package com.a.dproject.dagger;

import android.widget.Toast;

import com.a.dproject.DApp;

import javax.inject.Inject;

public class Engine {

    String gear = "";


    @Inject
    Engine(String gear) {
        this.gear = gear;
    }

    public void run() {
//        System.out.println("引擎转起来了~~~");
        Toast.makeText(DApp.appContext, gear + "引擎转起来了~~~", Toast.LENGTH_SHORT).show();
    }
}