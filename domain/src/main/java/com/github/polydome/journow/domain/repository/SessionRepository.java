package com.github.polydome.journow.domain.repository;

import com.github.polydome.journow.domain.model.Session;

import java.util.List;

public interface SessionRepository {
    void insert(Session session);
    List<Session> findAll();
}
