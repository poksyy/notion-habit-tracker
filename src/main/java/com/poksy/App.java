package com.poksy;

import com.poksy.automation.CleanupAutomation;
import com.poksy.automation.DailyHabitAutomation;
import com.poksy.client.NotionClient;
import com.poksy.config.NotionConfig;

/**
 * Main entry point for Notion automations.
 * 
 * Usage:
 *   java -jar notion-automations.jar [command]
 * 
 * Commands:
 *   habits  - Run the daily habit automation (default)
 *   help    - Show this help message
 */
public class App {

    public static void main(String[] args) {
        String command = args.length > 0 ? args[0] : "habits";

        switch (command.toLowerCase()) {
            case "habits" -> runHabitsAutomation();
            case "cleanup" -> runCleanupAutomation(args);
            case "help" -> printHelp();
            default -> {
                System.err.println("Unknown command: " + command);
                printHelp();
                System.exit(1);
            }
        }
    }

    private static void runHabitsAutomation() {
        try {
            NotionConfig.validate();
            NotionClient client = new NotionClient();
            DailyHabitAutomation automation = new DailyHabitAutomation(client);
            automation.run();
        } catch (IllegalStateException e) {
            System.err.println("❌ Configuration error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void runCleanupAutomation(String[] args) {
        try {
            NotionConfig.validate();

            int months = 1;
            if (args.length > 1) {
                try {
                    months = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid months value: " + args[1]);
                    System.exit(1);
                }
            }

            NotionClient client = new NotionClient();
            CleanupAutomation automation = new CleanupAutomation(client, months);
            automation.run();

        } catch (IllegalStateException e) {
            System.err.println("❌ Configuration error: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printHelp() {
        System.out.println("""
                
                Notion Automations
                ==================
                
                Commands:
                  habits           - Create daily habits (default)
                  cleanup [months] - Archive entries older than N months (default: 1)
                  help             - Show this help
                
                Examples:
                  java -jar notion-automations.jar habits
                  java -jar notion-automations.jar cleanup
                  java -jar notion-automations.jar cleanup 2
                """);
    }
}
