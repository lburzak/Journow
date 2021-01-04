package com.github.polydome.journow.data.event;

public class DataEvent {
    private final Type type;
    private final long idStart;
    private final long idStop;

    private DataEvent(Type type, long idStart, long idStop) {
        this.type = type;
        this.idStart = idStart;
        this.idStop = idStop;
    }

    public Type getType() {
        return type;
    }

    public long getIdStart() {
        return idStart;
    }

    public long getIdStop() {
        return idStop;
    }

    public static DataEvent insertOne(long id) {
        return new DataEvent(Type.INSERT, id, id);
    }

    public enum Type {
        INSERT,
        REMOVE,
        CHANGE
    }
}
