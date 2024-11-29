package ru.nsu.vozhzhov.model;

public class Task {

    private String name;

    private Status status;

    public Task(String name) {
        this.name = name;
        status = Status.NOT_DONE;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return name + " " + status.name();
    }
}