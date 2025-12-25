package com.poksy.model;

/**
 * Represents a habit that can be tracked daily.
 */
public record Habit(
        String name,
        HabitGroup group,
        String icon
) {
    
    /**
     * Defines when the habit should be performed.
     */
    public enum HabitGroup {
        MORNING("Morning"),
        EVENING("Evening");
        
        private final String displayName;
        
        HabitGroup(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
