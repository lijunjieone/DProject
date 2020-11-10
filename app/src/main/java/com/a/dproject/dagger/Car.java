package com.a.dproject.dagger;

import javax.inject.Inject;

public class Car {

    @QualifierA
    @Inject
    Engine engineA;
    @QualifierA
    @Inject
    Engine engineB;

    public Car() {
        DaggerCarComponent.builder().markCarModule(new MarkCarModule())
                .build().inject(this);
    }

    public Engine getEngineA() {
        return this.engineA;
    }

    public Engine getEngineB() {
        return this.engineB;
    }
}