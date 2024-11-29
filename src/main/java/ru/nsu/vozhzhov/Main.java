package ru.nsu.vozhzhov;

import ru.nsu.vozhzhov.model.ModelContext;

public class Main {
    public static void main(String[] args) {
        ModelContext mc = new ModelContext();
        mc.addWorkspace("LAB");
        mc.setCurrentWorkspace("LAB");
//        mc.addSubject("sys");
        mc.addSubject("os");
//        mc.addSubjectTask("os", "lab1");
        mc.deleteSubjectTask("os", "lab1");
//        mc.deleteSubject("os");
//        mc.changeWorkspaceName("WORK");

//        mc.addSubject("ooad");
//        mc.addSubjectTask("ooad", "class");
//        mc.deleteCurrentWorkspace();

//        System.out.println(mc.getSubjectTasks("ooad"));
    }
}