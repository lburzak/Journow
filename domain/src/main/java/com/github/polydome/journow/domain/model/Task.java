package com.github.polydome.journow.domain.model;

public class Task {
    private final String title;
    private final long id;

    public Task(long id, String title) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }
}
