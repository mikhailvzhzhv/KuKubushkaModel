package ru.nsu.vozhzhov.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {

    private String name;

    private Status status;

    public Task(String name) {
        this.name = name;
        status = Status.NOT_DONE;
    }

    @Override
    public String toString() {
        return name + " " + status.name();
    }
}