package com.github.polydome.journow.domain.service;

import com.github.polydome.journow.domain.model.TrackerData;

import java.util.Optional;

public interface TrackerDataStorage {
    void save(TrackerData data);
    Optional<TrackerData> read();
    TrackerData clear();
}
