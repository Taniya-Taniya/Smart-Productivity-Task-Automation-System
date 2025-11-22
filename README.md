# Smart Productivity & Task Automation System

A Java-based console application that helps users manage tasks, generate daily and weekly schedules, and track productivity. This project was built as part of the Java course requirement using core Object-Oriented Programming principles.

## Features
### ✔ Task Management
- Add new tasks
- Update existing tasks
- View all tasks
- Delete tasks
### ✔ Automated Scheduling
- Generate a daily schedule based on priority, deadline, and duration
- Create a weekly plan
- View pending and completed tasks
### ✔ Reporting
- View progress summary
- Check workload distribution
- Display structured schedules in the console
### ✔ File-Based Storage
- All tasks are stored in tasks.txt
- Data remains available even after program is closed

## Project Structure
```
SmartProductivitySystem/
│
├── src/
│   ├── Main.java
│   │
│   ├── models/
│   │     └── Task.java
│   │
│   ├── services/
│   │     ├── TaskManager.java
│   │     ├── Scheduler.java
│   │     └── ReportGenerator.java
│   │
│   ├── utils/
│   │     ├── FileHandler.java
│   │     └── InputValidator.java
│   │
│   └── data/
│         └── tasks.txt
│
└── screenshots/
```
## How to Run the Project
1. Clone the repository
```git clone <your-repo-link>```

2. Open in VS Code
- Install the Java Extension Pack
- Make sure Java is installed (java --version)

3. Run the application

Navigate to the src folder:
```
javac Main.java
java Main
```
(or use the “Run” button in VS Code)

## Technologies Used
- Java
- OOP (Classes, Objects, Encapsulation, Aggregation)
- File Handling
- Collections Framework
- Exception Handling

## Screenshots
   ```
   add_task.png
   view_tasks.png
   update_task.png
   delete_task.png
   daily_schedule.png
   weekly_plan.png
   progress_summary.png
```
## Future Enhancements
- GUI using JavaFX
- Database integration
- Notifications & reminders
- Cloud sync
- Calendar integration
- Export schedules to PDF

## Author

Taniya
Integrated M.Tech in Artificial Intelligence
VIT Bhopal University
