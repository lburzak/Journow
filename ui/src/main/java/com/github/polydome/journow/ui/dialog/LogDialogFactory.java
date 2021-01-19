package com.github.polydome.journow.ui.dialog;

import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.SessionRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.domain.usecase.LogSessionUseCase;
import com.github.polydome.journow.ui.listmodel.ProjectListModel;

import javax.inject.Inject;
import javax.inject.Provider;

public class LogDialogFactory {
    private final Provider<ProjectRepository> projectRepositoryProvider;
    private final Provider<TaskRepository> taskRepositoryProvider;
    private final Provider<LogSessionUseCase> logSessionUseCaseProvider;
    private final Provider<ProjectListModel> projectListModelProvider;

    @Inject
    public LogDialogFactory(Provider<ProjectRepository> projectRepositoryProvider,
                            Provider<TaskRepository> taskRepositoryProvider,
                            Provider<LogSessionUseCase> logSessionUseCaseProvider,
                            Provider<ProjectListModel> projectListModelProvider) {
        this.projectRepositoryProvider = projectRepositoryProvider;
        this.taskRepositoryProvider = taskRepositoryProvider;
        this.logSessionUseCaseProvider = logSessionUseCaseProvider;
        this.projectListModelProvider = projectListModelProvider;
    }

    public LogDialog createLinked(Task task) {
        return new LogDialog(
                projectRepositoryProvider.get(),
                taskRepositoryProvider.get(),
                logSessionUseCaseProvider.get(),
                projectListModelProvider.get(),
                task
        );
    }

    public LogDialog createBlank() {
        return new LogDialog(
                projectRepositoryProvider.get(),
                taskRepositoryProvider.get(),
                logSessionUseCaseProvider.get(),
                projectListModelProvider.get()
        );
    }
}
