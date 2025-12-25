package com.poksy.service;

import com.poksy.client.NotionClient;
import com.poksy.config.NotionConfig;
import com.poksy.model.Day;
import com.poksy.model.Habit;

import java.util.Optional;

/**
 * Service for managing Habit entries in Notion.
 */
public class HabitService {

    private final NotionClient client;

    public HabitService(NotionClient client) {
        this.client = client;
    }

    /**
     * Creates a habit entry in the Daily Log database, linked to a specific day.
     *
     * @param habit The habit to create
     * @param day   The day to link this habit to
     * @return The created page ID, or empty if failed
     */
    public Optional<String> createForDay(Habit habit, Day day) {
        if (day.id() == null) {
            throw new IllegalArgumentException("Day must have an ID to link habits to it");
        }

        String json = """
                {
                    "parent": { "database_id": "%s" },
                    "properties": {
                        "Habit": {
                            "title": [{ "text": { "content": "%s" } }]
                        },
                        "Date": {
                            "date": { "start": "%s" }
                        },
                        "Done": {
                            "checkbox": false
                        },
                        "Group": {
                            "select": { "name": "%s" }
                        },
                        "Day": {
                            "relation": [{ "id": "%s" }]
                        }
                    },
                    "icon": { "type": "emoji", "emoji": "%s" }
                }
                """.formatted(NotionConfig.DAILY_LOG_DATABASE_ID, habit.name(), day.date().toString(), habit.group().getDisplayName(), day.id(), habit.icon());

        return client.createPage(json);
    }
}
