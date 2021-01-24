package com.github.polydome.journow.viewmodel;

import com.github.polydome.journow.domain.controller.Tracker;
import com.github.polydome.journow.domain.model.Project;
import com.github.polydome.journow.domain.model.Task;
import com.github.polydome.journow.domain.repository.ProjectRepository;
import com.github.polydome.journow.domain.repository.TaskRepository;
import com.github.polydome.journow.common.FormatUtils;
import io.reactivex.rxjava3.core.Observable;

import javax.inject.Inject;
import javax.inject.Named;

public class TrackerViewModel {

    private final Tracker tracker;
    private final Observable<Long> updateInterval;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Inject
    public TrackerViewModel(Tracker tracker, @Named("TimerUpdateInterval") Observable<Long> updateInterval, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.tracker = tracker;
        this.updateInterval = updateInterval;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Observable<Boolean> hasOngoingSession() {
        return tracker.isRunning();
    }

    public Observable<String> getTimer() {
        return hasOngoingSession().switchMap(ongoingSession -> {
            if (ongoingSession)
                return tracker.timeElapsed(updateInterval);
            else
                return Observable.just(0L);
        }).map(FormatUtils::millisToReadableDuration);
    }

    public Observable<String> getTaskTitle() {
        return tracker.currentTask().map(task -> {
            if (task.getProject() != null)
                return String.format("<html><center>%s<br><i>%s</i></center></html>", task.getTitle(), task.getProject().getName());
            else {
                return task.getTitle();
            }
        });
    }

    public void startSession(String title, Project project, boolean isNewProject) {
        if (project != null && isNewProject)
            project = projectRepository.insert(project);

        Task task = taskRepository.insert(new Task(0, title, project));

        tracker.start(task.getId());
    }

    public void endSession() {
        tracker.stop();
    }
}
