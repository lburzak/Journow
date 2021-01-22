package com.github.polydome.journow.data.preferences;

import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.service.TrackerDataStorage;

import java.time.Instant;
import java.util.Optional;
import java.util.prefs.Preferences;

public class PreferencesTrackerDataStorage implements TrackerDataStorage {
    private final Preferences storage = Preferences.userRoot().node("/Tracker");

    private final String STORAGE_KEY_CURRENT_TASK = "CurrentTask";
    private final String STORAGE_KEY_START_DATE = "EpochStart";

    @Override
    public void save(TrackerData data) {
        storage.putLong(STORAGE_KEY_CURRENT_TASK, data.getTaskId());
        storage.putLong(STORAGE_KEY_START_DATE, data.getStartTime().toEpochMilli());
    }

    @Override
    public Optional<TrackerData> read() {
        long currentTask = storage.getLong(STORAGE_KEY_CURRENT_TASK, 0);
        long startDate = storage.getLong(STORAGE_KEY_START_DATE, 0);
        if (currentTask == 0)
            return Optional.empty();
        else {
            return Optional.of(new TrackerData(currentTask, Instant.ofEpochMilli(startDate)));
        }
    }

    @Override
    public void clear() {
        storage.remove(STORAGE_KEY_CURRENT_TASK);
        storage.remove(STORAGE_KEY_START_DATE);
    }
}
