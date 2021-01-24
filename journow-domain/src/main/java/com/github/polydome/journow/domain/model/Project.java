package com.github.polydome.journow.domain.model;

import java.util.Objects;

public class Project {
    private final long id;
    private final String name;

    public Project(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id == project.id &&
                name.equals(project.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
