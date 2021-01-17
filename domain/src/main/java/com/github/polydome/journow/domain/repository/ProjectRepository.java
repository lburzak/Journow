package com.github.polydome.journow.domain.repository;

import com.github.polydome.journow.domain.model.Project;

import java.util.List;

public interface ProjectRepository {
    List<Project> findAll();
    Project insert(Project project);
}
