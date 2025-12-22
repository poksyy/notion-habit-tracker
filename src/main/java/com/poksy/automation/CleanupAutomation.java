package com.poksy.automation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.poksy.client.NotionClient;
import com.poksy.config.NotionConfig;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CleanupAutomation {

    private final NotionClient client;
    private final int monthsToKeep;

    public CleanupAutomation(NotionClient client, int monthsToKeep) {
        this.client = client;
        this.monthsToKeep = monthsToKeep;
    }

    public CleanupAutomation(NotionClient client) {
        this(client, 1);
    }

    public void run() {
        System.out.println("Starting Cleanup Automation...\n");

        LocalDate cutoffDate = LocalDate.now().minusMonths(monthsToKeep);
        System.out.println("Archiving entries older than: " + cutoffDate + "\n");

        System.out.println("Cleaning up Days database...");
        int daysArchived = cleanupDatabase(
                NotionConfig.DAYS_DATABASE_ID,
                "Date",
                cutoffDate
        );

        System.out.println("\nCleaning up Daily Log database...");
        int habitsArchived = cleanupDatabase(
                NotionConfig.DAILY_LOG_DATABASE_ID,
                "Date",
                cutoffDate
        );

        printSummary(daysArchived, habitsArchived);
    }

    private int cleanupDatabase(String databaseId, String dateProperty, LocalDate cutoffDate) {
        List<String> pageIds = queryOldPages(databaseId, dateProperty, cutoffDate);

        if (pageIds.isEmpty()) {
            System.out.println("  No old entries found.");
            return 0;
        }

        System.out.println("  Found " + pageIds.size() + " entries to archive...");

        int archived = 0;
        for (String pageId : pageIds) {
            if (client.archivePage(pageId)) {
                archived++;
            }
        }

        System.out.println("  Archived: " + archived + "/" + pageIds.size());
        return archived;
    }

    private List<String> queryOldPages(String databaseId, String dateProperty, LocalDate cutoffDate) {
        List<String> pageIds = new ArrayList<>();

        String filterJson = """
                {
                    "filter": {
                        "property": "%s",
                        "date": {
                            "before": "%s"
                        }
                    },
                    "page_size": 100
                }
                """.formatted(dateProperty, cutoffDate.format(DateTimeFormatter.ISO_DATE));

        var response = client.queryDatabase(databaseId, filterJson);

        if (response.isPresent()) {
            JsonObject json = JsonParser.parseString(response.get()).getAsJsonObject();
            JsonArray results = json.getAsJsonArray("results");

            for (JsonElement element : results) {
                JsonObject page = element.getAsJsonObject();
                pageIds.add(page.get("id").getAsString());
            }
        }

        return pageIds;
    }

    private void printSummary(int daysArchived, int habitsArchived) {
        int total = daysArchived + habitsArchived;

        System.out.println("\n" + "-".repeat(40));
        System.out.println("Summary:");
        System.out.println("  Days archived:   " + daysArchived);
        System.out.println("  Habits archived: " + habitsArchived);
        System.out.println("  Total:           " + total);
        System.out.println("-".repeat(40));

        if (total > 0) {
            System.out.println("\nCleanup complete.");
        } else {
            System.out.println("\nNothing to clean up.");
        }
    }
}
