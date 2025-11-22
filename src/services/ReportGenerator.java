package services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import models.Task;

public class ReportGenerator {
    private TaskManager taskManager;
    private Scheduler scheduler;

    public ReportGenerator(TaskManager tm) {
        this.taskManager = tm;
        this.scheduler = new Scheduler(tm);
    }

    public void printWeeklyPlan(LocalDate startDate) {
        for (int i = 0; i < 7; i++) {
            LocalDate d = startDate.plusDays(i);
            System.out.println("=== " + d + " ===");
            List<Scheduler.ScheduledTask> plan = scheduler.generateDailySchedule(d, LocalTime.of(9, 0));
            if (plan.isEmpty()) {
                System.out.println("No scheduled tasks.");
            } else {
                for (Scheduler.ScheduledTask s : plan) {
                    System.out.println(s);
                }
            }
            System.out.println();
        }
    }

    public void printProgressSummary() {
        List<Task> all = taskManager.getAllTasks();
        if (all.isEmpty()) {
            System.out.println("No tasks to summarize.");
            return;
        }
        int total = all.size();
        int done = 0;
        int pending = 0;
        int inProgress = 0;
        for (Task t : all) {
            String st = t.getStatus().toUpperCase();
            if ("DONE".equals(st)) done++;
            else if ("IN_PROGRESS".equals(st)) inProgress++;
            else pending++;
        }
        System.out.println("--- Progress Summary ---");
        System.out.println("Total tasks: " + total);
        System.out.println("Done: " + done);
        System.out.println("In Progress: " + inProgress);
        System.out.println("Pending: " + pending);
    }
}
