package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private static int nextId = 1;

    private int id;
    private String title;
    private String description;
    private int priority; // 1 (high) to 5 (low)
    private LocalDate deadline; // nullable
    private int durationMinutes; // estimated duration
    private String status; // PENDING, DONE, IN_PROGRESS

    public Task(String title, String description, int priority, LocalDate deadline, int durationMinutes) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.durationMinutes = durationMinutes;
        this.status = "PENDING";
    }

    // Constructor used when loading from file (with explicit id)
    public Task(int id, String title, String description, int priority, LocalDate deadline, int durationMinutes, String status) {
        this.id = id;
        if (id >= nextId) nextId = id + 1;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.durationMinutes = durationMinutes;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public int getDuration() { return durationMinutes; }
    public void setDuration(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dl = (deadline == null) ? "None" : deadline.format(f);
        return "ID: " + id +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nPriority: " + priority +
                "\nDeadline: " + dl +
                "\nDuration (min): " + durationMinutes +
                "\nStatus: " + status;
    }

    // CSV line for saving: id|title|description|priority|deadline|duration|status
    public String toDataLine() {
        String dl = (deadline == null) ? "" : deadline.toString();
        String cleanTitle = title.replace("|", " ");
        String cleanDesc = description.replace("|", " ");
        return id + "|" + cleanTitle + "|" + cleanDesc + "|" + priority + "|" + dl + "|" + durationMinutes + "|" + status;
    }

    public static Task fromDataLine(String line) {
        // expected format: id|title|description|priority|deadline|duration|status
        try {
            String[] parts = line.split("\\|", -1);
            if (parts.length < 7) return null;
            int id = Integer.parseInt(parts[0]);
            String title = parts[1];
            String desc = parts[2];
            int priority = Integer.parseInt(parts[3]);
            LocalDate dl = parts[4].isEmpty() ? null : LocalDate.parse(parts[4]);
            int duration = Integer.parseInt(parts[5]);
            String status = parts[6];
            return new Task(id, title, desc, priority, dl, duration, status);
        } catch (Exception e) {
            return null;
        }
    }
}
