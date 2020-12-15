package com.github.polydome.journow.ui;

import com.github.polydome.journow.di.DaggerApplicationComponent;

public class Main {
    public static void main(String[] args) {
        new App(DaggerApplicationComponent.create()).run();
    }
}
