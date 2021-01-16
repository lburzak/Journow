package com.github.polydome.journow.test;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;

public class TaskFactory {
    public static Task createTask(long id, String title) {
        return new Task(id, title, new Project(12, "test project"));
    }
}
