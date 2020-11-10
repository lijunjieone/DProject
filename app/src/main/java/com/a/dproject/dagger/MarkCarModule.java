package com.a.dproject.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class MarkCarModule {

    public MarkCarModule() {
    }

    @QualifierA
    @Provides
    Engine provideEngineA() {
        return new Engine("gearA");
    }

    @QualifierB
    @Provides
    Engine provideEngineB() {
        return new Engine("gearB");
    }
}