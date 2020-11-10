package com.a.dproject.dagger;

import dagger.Component;

@Component(modules = {MarkCarModule.class})
public interface CarComponent {
    void inject(Car car);
}