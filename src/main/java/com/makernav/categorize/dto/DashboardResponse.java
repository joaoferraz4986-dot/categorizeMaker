package com.makernav.categorize.dto;

import java.time.LocalDate;
import java.util.List;

public record DashboardResponse(
        String currentMonth,
        MonthSummary current,
        MonthSummary previous,
        List<MonthSummary> months,
        List<ActiveProject> activeProjects,
        List<Event> events,
        ItemSummary items
) {
    public record MonthSummary(
            String label,
            String key,
            int projectsCreated,
            int projectsTotal,
            int totalQuantity,
            int availableQuantity,
            int usedQuantity,
            int brokenQuantity
    ) {}

    public record ActiveProject(
            String name,
            String category,
            String description,
            LocalDate startDate
    ) {}

    public record Event(
            String type,
            String title,
            String description,
            LocalDate date
    ) {}

    public record ItemSummary(
            int totalQuantity,
            int availableQuantity,
            int usedQuantity,
            int brokenQuantity
    ) {}
}
