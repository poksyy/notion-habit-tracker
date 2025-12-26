package com.poksy.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poksy.model.Habit;
import com.poksy.model.Habit.HabitGroup;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HabitConfig {

    private static final String CONFIG_FILE = "habits.json";

    public static List<Habit> loadHabits() {
        List<Habit> habits = new ArrayList<>();

        try {
            String content = Files.readString(Path.of(CONFIG_FILE));
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            JsonArray habitsArray = json.getAsJsonArray("habits");

            for (var element : habitsArray) {
                JsonObject obj = element.getAsJsonObject();
                String name = obj.get("name").getAsString();
                HabitGroup group = HabitGroup.valueOf(obj.get("group").getAsString());
                String icon = obj.get("icon").getAsString();
                int order = obj.get("order").getAsInt();

                habits.add(new Habit(name, group, icon, order));
            }

            habits.sort(Comparator.comparingInt(Habit::order));

        } catch (IOException e) {
            System.err.println("Error loading habits.json: " + e.getMessage());
            System.err.println("Make sure habits.json exists in the project root.");
            System.exit(1);
        }

        return habits;
    }
}