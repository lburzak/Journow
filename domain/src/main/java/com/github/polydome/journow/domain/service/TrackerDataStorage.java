package com.github.polydome.journow.domain.service;

import com.github.polydome.journow.domain.model.TrackerData;

public interface TrackerDataStorage {
    void save(TrackerData data);
    TrackerData read();
}
