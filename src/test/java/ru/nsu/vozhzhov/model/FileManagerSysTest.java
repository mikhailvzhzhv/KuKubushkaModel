package ru.nsu.vozhzhov.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class FileManagerSysTest {

    private Path tempDir;
    private FileManager fm;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        this.tempDir = tempDir;
        fm = new FileManagerSys(tempDir.toString());
    }

    @Test
    void testConstructorCreatesWorkDirectory() {
        File workDirectory = tempDir.toFile();

        assertTrue(workDirectory.exists(), "Work directory should be created by the constructor");
        assertTrue(workDirectory.isDirectory(), "Work directory should be a directory");
    }

    @Test
    void saveWorkspace() {
        String workspaceName = "testWorkspace";

        fm.saveWorkspace(workspaceName);
        File wsDirectory = new File(tempDir.toFile(), workspaceName);

        assertTrue(wsDirectory.isDirectory(), "Workspace directory should be created by saveWorkspace");
        assertTrue(wsDirectory.exists(), "Workspace directory should be a directory");
    }

    @Test
    void saveSubject() {
        String workspaceName = "testWorkspace";
        String subjectName = "testSubject";

        fm.saveWorkspace(workspaceName);
        fm.saveSubject(workspaceName, subjectName);

        File wsDirectory = new File(tempDir.toFile(), workspaceName);
        File sjDirectory = new File(wsDirectory.getPath(), subjectName);

        assertTrue(sjDirectory.isDirectory(), "Subject directory should be created by saveSubject");
        assertTrue(sjDirectory.exists(), "Subject directory should be a directory");
    }

    @Test
    void saveTask() {
        String workspaceName = "testWorkspace";
        String subjectName = "testSubject";
        String taskName = "testTask";

        fm.saveWorkspace(workspaceName);
        fm.saveSubject(workspaceName, subjectName);
        fm.saveTask(workspaceName, subjectName, taskName);

        File wsDirectory = new File(tempDir.toFile(), workspaceName);
        File sjDirectory = new File(wsDirectory.getPath(), subjectName);
        File tsFile = new File(sjDirectory.getPath(), taskName);

        assertTrue(tsFile.isFile(), "Task file should be created by saveTask");
        assertTrue(tsFile.exists(), "Task fiel should be a file");
    }

    @Test
    void changeWorkspaceName() {
        String oldWorkspaceName = "oldWorkspaceName";
        String newWorkspaceName = "newWorkspaceName";
        String testSubject = "testSubject";

        fm.saveWorkspace(oldWorkspaceName);
        fm.saveSubject(oldWorkspaceName, testSubject);
        fm.changeWorkspaceName(oldWorkspaceName, newWorkspaceName);

        File oldDirectory = new File(tempDir.toFile(), oldWorkspaceName);
        File newDirectory = new File(tempDir.toFile(), newWorkspaceName);
        File oldSubjectDirectory = new File(oldDirectory.getPath(), testSubject);
        File newSubjectDirectory = new File(newDirectory.getPath(), testSubject);

        assertFalse(oldDirectory.exists(), "Old directory should not exist");
        assertTrue(newDirectory.exists(), "New directory should exist");
        assertFalse(oldSubjectDirectory.exists(), "Subject directory should not exist by old path");
        assertTrue(newSubjectDirectory.exists(), "New directory should exist by new path");
    }

    @Test
    void deleteWorkspace() {
        String testWorkspace = "testWorkspace";

        fm.saveWorkspace(testWorkspace);
        File testDir = new File(tempDir.toFile(), testWorkspace);

        assertTrue(testDir.exists(), "Test directory should exist");

        fm.deleteWorkspace(testWorkspace);
        assertFalse(testDir.exists(), "Test directory should not exist");
    }

    @Test
    void deleteSubject() {
        String testWorkspace = "testWorkspace";
        String testSubject = "testSubject";
        String testTask = "testTask";

        fm.saveWorkspace(testWorkspace);
        fm.saveSubject(testWorkspace, testSubject);
        fm.saveTask(testWorkspace, testSubject, testTask);

        File workspaceDir = new File(tempDir.toFile(), testWorkspace);
        File subjectDir = new File(workspaceDir.getPath(), testSubject);
        File taskFile = new File(subjectDir.getPath(), testTask);

        assertTrue(subjectDir.exists(), "Subject directory should exist");
        assertTrue(taskFile.exists(), "Task file should exist");

        fm.deleteSubject(testWorkspace, testSubject);

        assertFalse(subjectDir.exists(), "Subject directory should not exist");
        assertFalse(taskFile.exists(), "Task file should not exist");
    }

    @Test
    void deleteSubjectTask() {
        String testWorkspace = "testWorkspace";
        String testSubject = "testSubject";
        String testTask = "testTask";
        String testTask1 = "testTask1";

        fm.saveWorkspace(testWorkspace);
        fm.saveSubject(testWorkspace, testSubject);
        fm.saveTask(testWorkspace, testSubject, testTask);
        fm.saveTask(testWorkspace, testSubject, testTask1);

        File workspaceDir = new File(tempDir.toFile(), testWorkspace);
        File subjectDir = new File(workspaceDir.getPath(), testSubject);
        File taskFile = new File(subjectDir.getPath(), testTask);
        File taskFile1 = new File(subjectDir.getPath(), testTask1);

        assertTrue(taskFile.exists(), "Task file should exist");
        assertTrue(taskFile1.exists(), "Task1 file should exist");

        fm.deleteSubjectTask(testWorkspace, testSubject, testTask);

        assertTrue(subjectDir.exists(), "Subject directory should not exist");
        assertFalse(taskFile.exists(), "Task file should not exist");
        assertTrue(taskFile1.exists(), "Task1 file should exist");
    }

    @Test
    void loadWorkDir() {
        String testWorkspaceName = "testWorkspace";
        String testSubjectName = "testSubject";
        String testTaskName = "testTask";

        Task task = new Task(testTaskName);
        Subject subject = new Subject(testSubjectName);
        Workspace workspace = new Workspace(testWorkspaceName);

        fm.saveWorkspace(testWorkspaceName);
        fm.saveSubject(testWorkspaceName, testSubjectName);
        fm.saveTask(testWorkspaceName, testSubjectName, testTaskName);

        List<Workspace> testWorkspaces = new ArrayList<>();
        fm.loadWorkDir(testWorkspaces);

        Workspace testWorkspace = assertDoesNotThrow(() -> testWorkspaces.get(0));
        assertEquals(workspace.getName(), testWorkspace.getName());

        Subject testSubject = assertDoesNotThrow(() -> testWorkspace.getSubjects().get(0));
        assertEquals(testSubject.getName(), subject.getName());

        Task testTask = assertDoesNotThrow(() -> testSubject.getTasks().get(0));
        assertEquals(testTask.getName(), task.getName());
    }

    @Test
    void changeSubjectTaskStatus() {
        String testWorkspaceName = "testWorkspace";
        String testSubjectName = "testSubject";
        String testTaskName = "testTask";

        Task task = new Task(testTaskName);
        Subject subject = new Subject(testSubjectName);
        Workspace workspace = new Workspace(testWorkspaceName);
        workspace.addSubject(subject);
        subject.addTask(task);

        fm.saveWorkspace(testWorkspaceName);
        fm.saveSubject(testWorkspaceName, testSubjectName);
        fm.saveTask(testWorkspaceName, testSubjectName, testTaskName);

        workspace.changeSubjectTaskStatus(testSubjectName, testTaskName, Status.PASS);
        fm.changeSubjectTaskStatus(testWorkspaceName, testSubjectName, testTaskName, Status.PASS);

        List<Workspace> testWorkspaces = new ArrayList<>();
        fm.loadWorkDir(testWorkspaces);

        Workspace testWorkspace = testWorkspaces.get(0);
        Subject testSubject = testWorkspace.getSubjects().get(0);
        Task testTask = testSubject.getTasks().get(0);

        System.out.println(task.getStatus());
        System.out.println(testTask.getStatus());
        assertEquals(task.getStatus().getValue(), testTask.getStatus().getValue());
    }
}