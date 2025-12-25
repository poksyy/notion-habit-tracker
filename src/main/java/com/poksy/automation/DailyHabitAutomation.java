package com.poksy.automation;

import com.poksy.client.NotionClient;
import com.poksy.model.Day;
import com.poksy.model.Habit;
import com.poksy.model.Habit.HabitGroup;
import com.poksy.service.DayService;
import com.poksy.service.HabitService;

import java.util.List;

public class DailyHabitAutomation {

    private final DayService dayService;
    private final HabitService habitService;

    private static final List<Habit> HABITS = List.of(new Habit("Meditate", HabitGroup.MORNING, "🧘"), new Habit("Stretching", HabitGroup.MORNING, "🤸‍♂️"), new Habit("Work", HabitGroup.MORNING, "💻"), new Habit("Morning routine", HabitGroup.MORNING, "☀️"), new Habit("Exercise", HabitGroup.EVENING, "🏋️"), new Habit("Learn", HabitGroup.EVENING, "📖"), new Habit("Night routine", HabitGroup.EVENING, "🌑"));

    public DailyHabitAutomation(NotionClient client) {
        this.dayService = new DayService(client);
        this.habitService = new HabitService(client);
    }

    public void run() {
        System.out.println("Starting Daily Habit Automation...\n");

        Day today = Day.forToday();
        System.out.println("Creating day: " + today.name());

        var createdDay = dayService.create(today);

        if (createdDay.isEmpty()) {
            System.err.println("Failed to create day. Aborting.");
            return;
        }

        Day day = createdDay.get();
        System.out.println("Day created with ID: " + day.id() + "\n");

        System.out.println("Creating habits...");

        int successCount = 0;
        int failCount = 0;

        for (Habit habit : HABITS) {
            var result = habitService.createForDay(habit, day);

            if (result.isPresent()) {
                System.out.println("  + " + habit.name());
                successCount++;
            } else {
                System.out.println("  - " + habit.name() + " (failed)");
                failCount++;
            }
        }

        printSummary(successCount, failCount);
    }

    private void printSummary(int successCount, int failCount) {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("Summary:");
        System.out.println("  Created: " + successCount + " habits");
        if (failCount > 0) {
            System.out.println("  Failed:  " + failCount + " habits");
        }
        System.out.println("-".repeat(40));

        if (failCount == 0) {
            System.out.println("\nAll done.");
        } else {
            System.out.println("\nSome habits failed. Check errors above.");
        }
    }

    public static List<Habit> getHabits() {
        return HABITS;
    }
}