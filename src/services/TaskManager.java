package services;

import java.util.ArrayList;
import java.util.List;
import models.Task;
import utils.FileHandler;

public class TaskManager {
    private List<Task> tasks;
    private FileHandler fileHandler;

    public TaskManager() {
        tasks = new ArrayList<>();
        fileHandler = new FileHandler("src/data/tasks.txt");
    }

    public void addTask(Task t) {
        tasks.add(t);
    }

    public boolean deleteTask(int id) {
        Task t = getTaskById(id);
        if (t == null) return false;
        tasks.remove(t);
        return true;
    }

    public void updateTask(Task t) {
        // in-memory update - tasks hold object reference, so nothing else needed
    }

    public Task getTaskById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getPendingTasks() {
        List<Task> out = new ArrayList<>();
        for (Task t : tasks) {
            if (!"DONE".equalsIgnoreCase(t.getStatus())) out.add(t);
        }
        return out;
    }

    public void saveTasks() {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(t.toDataLine());
        }
        fileHandler.saveLines(lines);
    }

    public void loadTasks() {
        List<String> lines = fileHandler.loadLines();
        if (lines == null) return;
        for (String l : lines) {
            Task t = Task.fromDataLine(l);
            if (t != null) tasks.add(t);
        }
    }
}
