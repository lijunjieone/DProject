package com.a.dproject.dagger;

import dagger.Component;

@Component
public interface CarComponent {
    void inject(Car car);
}