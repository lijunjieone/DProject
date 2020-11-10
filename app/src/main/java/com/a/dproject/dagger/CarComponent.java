package com.a.dproject.dagger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;
import javax.inject.Scope;

import dagger.Component;

@Component(modules = {MarkCarModule.class})
public interface CarComponent {
    void inject(Car car);
}


@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface QualifierA {
}

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@interface QualifierB {
}


@Scope
@Retention(RetentionPolicy.RUNTIME)
@interface CarScope {
}