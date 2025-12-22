# Notion Automations

Automation scripts for my Notion workspace.

## Features

- **Daily Habits**: Creates day entry and habits, fully linked
- **Monthly Cleanup**: Archives entries older than N months

## Setup

### 1. Notion Credentials

1. Create integration at [notion.so/profile/integrations](https://notion.so/profile/integrations)
2. Copy the integration secret
3. Connect integration to your databases

### 2. Database IDs

Get IDs from database URLs:
```
https://notion.so/workspace/1234567890abcdef...?v=...
                            └─── Database ID ───┘
```

### 3. Environment
```bash
cp .env.example .env
# Fill in: NOTION_TOKEN, DAYS_DATABASE_ID, DAILY_LOG_DATABASE_ID
```

### 4. Run
```bash
mvn clean package

# Daily habits
java -jar target/notion-automations-1.0.0.jar habits

# Cleanup (default: 1 month)
java -jar target/notion-automations-1.0.0.jar cleanup

# Cleanup with custom months
java -jar target/notion-automations-1.0.0.jar cleanup 2
```

## GitHub Actions

Add secrets in **Settings → Secrets → Actions**:
- `NOTION_TOKEN`
- `DAYS_DATABASE_ID`
- `DAILY_LOG_DATABASE_ID`

Schedules:
- **Daily habits**: 06:00 UTC+1
- **Monthly cleanup**: 1st of each month at 06:00 UTC+1

## Project Structure
```
src/main/java/com/poksy/
├── App.java
├── automation/
│   ├── DailyHabitAutomation.java
│   └── CleanupAutomation.java
├── client/
│   └── NotionClient.java
├── config/
│   └── NotionConfig.java
├── model/
│   ├── Day.java
│   └── Habit.java
└── service/
    ├── DayService.java
    └── HabitService.java
```

## License

MIT