package com.github.polydome.journow.domain.model;

import java.util.Objects;

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

    @Override
    public String toString() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                title.equals(task.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id);
    }
}
