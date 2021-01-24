package com.github.polydome.journow.domain.exception;

public class NoSuchTaskException extends RuntimeException {
    public NoSuchTaskException(long taskId) {
        super(String.format("Task identified with [id=%d] does not exist", taskId));
    }
}
