package com.example.bloodbowl.Service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class DanishMastersUtilityService {

    // Calculate placement points based on Danish Masters rules
    public int calculatePlacementPoints(int placement, int totalCoaches) {
        if (placement > 10) {
            return 0; // No points for places below 10th
        }

        int basePoints = 11 - placement; // 1st = 10, 2nd = 9, etc.

        // Adjust for tournaments with fewer than 10 coaches
        if (totalCoaches < 10) {
            int reduction = 10 - totalCoaches;
            basePoints = Math.max(0, basePoints - reduction);
        }

        return basePoints;
    }

    // Calculate bonus points for tournament size
    public int calculateSizeBonus(int placement, int totalCoaches) {
        int coachesBelow = totalCoaches - placement;
        return coachesBelow / 10; // +1 point per 10 coaches below
    }

    // Calculate total series points for a tournament result
    public int calculateTotalSeriesPoints(int placement, int totalCoaches, int tournamentDays) {
        int placementPoints = calculatePlacementPoints(placement, totalCoaches);
        int sizeBonus = calculateSizeBonus(placement, totalCoaches);
        int participationBonus = tournamentDays;

        return placementPoints + sizeBonus + participationBonus;
    }

    // Calculate Pariah Cup points (wins + draws)
    public int calculatePariahPoints(int wins, int draws) {
        return wins + draws;
    }

    // Validate tournament date range
    public boolean isValidTournamentDateRange(LocalDate startDate, LocalDate endDate) {
        return endDate != null && startDate != null && !endDate.isBefore(startDate);
    }

    // Get tournament year from start date
    public int getTournamentYear(LocalDate startDate) {
        return startDate.getYear();
    }

    // Check if race is Pariah for given year
    public boolean isPariahRace(String raceName, int year) {
        // For 2025, hardcoded list - could be made database-driven
        Set<String> pariahTeams2025 = Set.of(
                "Goblins", "Halflings", "Snotlings", "Gnomes", "Ogres",
                "Nurgle", "Black Orcs", "Chaos Dwarfs", "Slann"
        );

        if (year == 2025) {
            return pariahTeams2025.contains(raceName);
        }

        // For other years, you could implement database lookup
        return false;
    }

    // Format percentage for display
    public String formatPercentage(double percentage) {
        return String.format("%.1f%%", percentage);
    }

    // Calculate average placement with null safety
    public Double calculateAveragePlacement(List<Integer> placements) {
        if (placements == null || placements.isEmpty()) {
            return null;
        }

        return placements.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}