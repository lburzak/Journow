package com.github.polydome.journow.domain.model;

import java.util.Objects;

public class Task {
    private final String title;
    private final long id;
    private final Project project;

    public Task(long id, String title, Project project) {
        this.title = title;
        this.id = id;
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public Project getProject() {
        return project;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String title;
        private Project project;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder project(Project project) {
            this.project = project;
            return this;
        }

        public Task build() {
            return new Task(id, title, project);
        }
    }
}
