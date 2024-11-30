package ru.nsu.vozhzhov.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Context {

    private final List<Workspace> workspaces;

    @Getter
    private Workspace currentWorkspace;

    public Context() {
        workspaces = new ArrayList<>();
        currentWorkspace = null;
    }

    public void loadWorkspaces(FileManager fm) {
        fm.loadWorkDir(workspaces);
    }

    public boolean addWorkspace(String name) {
        for (Workspace workspace : workspaces) {
            if (workspace.getName().equals(name)) {
                return false;
            }
        }
        workspaces.add(new Workspace(name));
        return true;
    }

    public boolean setCurrentWorkspace(String name) {
        for (Workspace workspace : workspaces) {
            if (workspace.getName().equals(name)) {
                currentWorkspace = workspace;
                return true;
            }
        }

        return false;
    }

    public List<String> getWorkspacesNames() {
        return workspaces.stream().map(Workspace::getName).collect(Collectors.toList());
    }

    public boolean deleteCurrentWorkspace() {
        if (currentWorkspace == null) {
            return false;
        }
        return workspaces.remove(currentWorkspace);
    }
}
