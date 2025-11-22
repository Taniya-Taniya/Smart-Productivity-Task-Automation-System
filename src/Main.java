import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;
import models.Task;
import services.TaskManager;
import services.Scheduler;
import services.ReportGenerator;
import utils.InputValidator;

public class Main {
    private static TaskManager taskManager = new TaskManager();
    private static Scheduler scheduler = new Scheduler(taskManager);
    private static ReportGenerator reportGenerator = new ReportGenerator(taskManager);

    public static void main(String[] args) {
        // load tasks from file
        taskManager.loadTasks();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            showMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTaskFlow(scanner);
                    break;
                case "2":
                    viewTasksFlow();
                    break;
                case "3":
                    generateTodayScheduleFlow(scanner);
                    break;
                case "4":
                    generateWeeklyPlanFlow(scanner);
                    break;
                case "5":
                    deleteTaskFlow(scanner);
                    break;
                case "6":
                    updateTaskFlow(scanner);
                    break;
                case "7":
                    reportGenerator.printProgressSummary();
                    break;
                case "8":
                    taskManager.saveTasks();
                    System.out.println("Tasks saved. Exiting.");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Choose 1-8.");
            }
            System.out.println();
        }

        scanner.close();
    }

    private static void showMenu() {
        System.out.println("=== Smart Productivity System ===");
        System.out.println("1. Add Task");
        System.out.println("2. View Tasks");
        System.out.println("3. Generate Today's Schedule");
        System.out.println("4. Generate Weekly Plan");
        System.out.println("5. Delete Task");
        System.out.println("6. Update Task");
        System.out.println("7. Progress Summary");
        System.out.println("8. Save & Exit");
        System.out.print("Choose an option: ");
    }

    private static void addTaskFlow(Scanner scanner) {
        System.out.println("--- Add Task ---");
        System.out.print("Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        int priority = 3;
        while (true) {
            System.out.print("Priority (1-high ... 5-low): ");
            String p = scanner.nextLine().trim();
            if (InputValidator.isValidIntegerInRange(p, 1, 5)) {
                priority = Integer.parseInt(p);
                break;
            } else {
                System.out.println("Enter integer 1 to 5.");
            }
        }

        LocalDate deadline = null;
        while (true) {
            System.out.print("Deadline (yyyy-MM-dd), or blank for none: ");
            String d = scanner.nextLine().trim();
            if (d.isEmpty()) {
                break;
            }
            deadline = InputValidator.parseDate(d);
            if (deadline != null) break;
            System.out.println("Invalid date format.");
        }

        int duration = 60;
        while (true) {
            System.out.print("Duration in minutes (e.g. 60): ");
            String dur = scanner.nextLine().trim();
            if (InputValidator.isValidPositiveInt(dur)) {
                duration = Integer.parseInt(dur);
                break;
            } else {
                System.out.println("Enter positive integer.");
            }
        }

        Task t = new Task(title, description, priority, deadline, duration);
        taskManager.addTask(t);
        System.out.println("Task added (ID: " + t.getId() + ").");
    }

    private static void viewTasksFlow() {
        System.out.println("--- All Tasks ---");
        List<Task> tasks = taskManager.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
            return;
        }
        for (Task t : tasks) {
            System.out.println(t);
            System.out.println("--------------------");
        }
    }

    private static void generateTodayScheduleFlow(Scanner scanner) {
        LocalDate today = LocalDate.now();
        System.out.println("--- Generate Schedule for " + today + " ---");
        LocalTime startTime = LocalTime.of(9, 0);
        while (true) {
            System.out.print("Start time (HH:mm) default 09:00: ");
            String s = scanner.nextLine().trim();
            if (s.isEmpty()) break;
            LocalTime t = InputValidator.parseTime(s);
            if (t != null) {
                startTime = t;
                break;
            } else {
                System.out.println("Invalid time format. Use HH:mm");
            }
        }

        List<Scheduler.ScheduledTask> plan = scheduler.generateDailySchedule(today, startTime);
        if (plan.isEmpty()) {
            System.out.println("No tasks scheduled for today.");
            return;
        }
        System.out.println("Today's Schedule:");
        for (Scheduler.ScheduledTask st : plan) {
            System.out.println(st);
        }
    }

    private static void generateWeeklyPlanFlow(Scanner scanner) {
        LocalDate start = LocalDate.now();
        System.out.println("--- Weekly Plan starting " + start + " ---");
        reportGenerator.printWeeklyPlan(start);
    }

    private static void deleteTaskFlow(Scanner scanner) {
        System.out.print("Enter Task ID to delete: ");
        String s = scanner.nextLine().trim();
        if (!InputValidator.isValidInteger(s)) {
            System.out.println("Invalid ID.");
            return;
        }
        int id = Integer.parseInt(s);
        boolean ok = taskManager.deleteTask(id);
        if (ok) System.out.println("Task deleted.");
        else System.out.println("Task not found.");
    }

    private static void updateTaskFlow(Scanner scanner) {
        System.out.print("Enter Task ID to update: ");
        String s = scanner.nextLine().trim();
        if (!InputValidator.isValidInteger(s)) {
            System.out.println("Invalid ID.");
            return;
        }
        int id = Integer.parseInt(s);
        Task t = taskManager.getTaskById(id);
        if (t == null) {
            System.out.println("Task not found.");
            return;
        }
        System.out.println("Current: " + t);
        System.out.print("New title (blank to keep): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) t.setTitle(title);

        System.out.print("New description (blank to keep): ");
        String desc = scanner.nextLine().trim();
        if (!desc.isEmpty()) t.setDescription(desc);

        System.out.print("New priority (1-5, blank to keep): ");
        String pr = scanner.nextLine().trim();
        if (!pr.isEmpty() && InputValidator.isValidIntegerInRange(pr, 1, 5)) {
            t.setPriority(Integer.parseInt(pr));
        }

        System.out.print("New deadline (yyyy-MM-dd, blank to keep/remove): ");
        String dl = scanner.nextLine().trim();
        if (!dl.isEmpty()) {
            LocalDate date = InputValidator.parseDate(dl);
            if (date != null) t.setDeadline(date);
            else System.out.println("Invalid date, keeping old.");
        }

        System.out.print("New duration in minutes (blank to keep): ");
        String dur = scanner.nextLine().trim();
        if (!dur.isEmpty() && InputValidator.isValidPositiveInt(dur)) {
            t.setDuration(Integer.parseInt(dur));
        }

        System.out.print("New status (PENDING, DONE, IN_PROGRESS) blank to keep: ");
        String st = scanner.nextLine().trim();
        if (!st.isEmpty()) t.setStatus(st.toUpperCase());

        taskManager.updateTask(t);
        System.out.println("Task updated.");
    }
}
