package com.github.polydome.journow.domain.usecase;

import com.github.polydome.journow.domain.model.TrackerData;
import com.github.polydome.journow.domain.service.TrackerDataStorage;

import java.util.Optional;

public class MemoryTrackerDataStorage implements TrackerDataStorage {
    private TrackerData data;

    @Override
    public void save(TrackerData data) {
        this.data = data;
    }

    @Override
    public Optional<TrackerData> read() {
        if (data == null)
            return Optional.empty();
        else
            return Optional.of(data);
    }

    @Override
    public void clear() {
        data = null;
    }
}
