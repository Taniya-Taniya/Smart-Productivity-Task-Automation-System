package services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import models.Task;

public class Scheduler {
    private TaskManager taskManager;

    public Scheduler(TaskManager tm) {
        this.taskManager = tm;
    }

    // A simple ScheduledTask container
    public static class ScheduledTask {
        private Task task;
        private LocalTime start;
        private LocalTime end;

        public ScheduledTask(Task task, LocalTime start) {
            this.task = task;
            this.start = start;
            this.end = start.plusMinutes(task.getDuration());
        }

        @Override
        public String toString() {
            DateTimeFormatter f = DateTimeFormatter.ofPattern("HH:mm");
            return "[" + start.format(f) + " - " + end.format(f) + "] ID:" + task.getId()
                    + " | " + task.getTitle()
                    + " | Duration: " + task.getDuration() + " min"
                    + " | Priority: " + task.getPriority()
                    + (task.getDeadline() != null ? " | Deadline:" + task.getDeadline() : "");
        }

        public LocalTime getEnd() { return end; }
    }

    /**
     * Generate a simple sequential schedule for the given day.
     * It picks pending tasks with deadline == date first, then tasks without deadline.
     * It sorts by priority (1 high) and earliest deadline.
     */
    public List<ScheduledTask> generateDailySchedule(LocalDate date, LocalTime startTime) {
        List<Task> candidates = new ArrayList<>();
        for (Task t : taskManager.getPendingTasks()) {
            // include all tasks if deadline == date or deadline is null or deadline after date (optional)
            if (t.getDeadline() == null || t.getDeadline().isEqual(date) || t.getDeadline().isBefore(date) || t.getDeadline().isAfter(date)) {
                candidates.add(t);
            }
        }

        // sort: first by whether deadline == date (true first), then by priority asc, then by deadline nearest
        candidates.sort(Comparator.comparing((Task t) -> t.getDeadline() == null ? 1 : (t.getDeadline().isEqual(date) ? 0 : 1))
                .thenComparingInt(Task::getPriority)
                .thenComparing(t -> t.getDeadline() == null ? LocalDate.MAX : t.getDeadline()));

        List<ScheduledTask> plan = new ArrayList<>();
        LocalTime cursor = startTime;

        for (Task t : candidates) {
            // skip tasks marked DONE
            if ("DONE".equalsIgnoreCase(t.getStatus())) continue;
            ScheduledTask st = new ScheduledTask(t, cursor);
            plan.add(st);
            cursor = st.getEnd();
            // do not worry about day boundaries in this simple version
        }
        return plan;
    }
}
