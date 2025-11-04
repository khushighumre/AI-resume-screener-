package com.resumescreener.model;

public abstract class Person {
    protected String name;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public abstract String getName();
}
