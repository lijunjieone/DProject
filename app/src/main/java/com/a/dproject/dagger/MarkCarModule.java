package com.a.dproject.dagger;

import dagger.Module;
import dagger.Provides;

@Module
public class MarkCarModule {

    public MarkCarModule() {
    }

    @Provides
    Engine provideEngine() {
        return new Engine("gear");
    }
}