package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Model.TournamentResult;
import com.example.bloodbowl.Repository.TournamentRepository;
import com.example.bloodbowl.Repository.TournamentResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataValidationService {

    private static final Logger logger = LoggerFactory.getLogger(DataValidationService.class);

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentResultRepository resultRepository;

    public List<String> validateTournamentData(Long tournamentId) {
        List<String> issues = new ArrayList<>();

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElse(null);

        if (tournament == null) {
            issues.add("Turnering ikke fundet");
            return issues;
        }

        List<TournamentResult> results = resultRepository.findByTournamentIdOrderByPlacementAsc(tournamentId);

        // Check if we have the expected number of results
        if (results.size() != tournament.getNumCoaches()) {
            issues.add(String.format("Forventet %d resultater, men fandt %d",
                    tournament.getNumCoaches(), results.size()));
        }

        // Check for duplicate placements
        Set<Integer> placements = new HashSet<>();
        for (TournamentResult result : results) {
            if (!placements.add(result.getPlacement())) {
                issues.add("Duplikerede placeringer fundet: " + result.getPlacement());
            }
        }

        // Check for gaps in placements
        for (int i = 1; i <= results.size(); i++) {
            final int placement = i;  // Create a final copy
            boolean found = results.stream()
                    .anyMatch(r -> r.getPlacement().equals(placement));
            if (!found) {
                issues.add("Manglende placering: " + i);
            }
        }

        // Check for duplicate coaches
        Set<String> coaches = new HashSet<>();
        for (TournamentResult result : results) {
            String coachName = result.getCoach().getName();
            if (!coaches.add(coachName)) {
                issues.add("Duplikerede trænere fundet: " + coachName);
            }
        }

        // Validate match statistics
        for (TournamentResult result : results) {
            if (!result.isValidMatchStatistics()) {
                issues.add(String.format("Ugyldige kamp statistikker for %s: %d != %d",
                        result.getCoach().getName(),
                        result.getTotalMatches(),
                        result.getMatchesPlayed()));
            }
        }

        return issues;
    }

    public Map<String, Object> generateTournamentReport(Long tournamentId) {
        Map<String, Object> report = new HashMap<>();

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElse(null);

        if (tournament == null) {
            report.put("error", "Turnering ikke fundet");
            return report;
        }

        List<TournamentResult> results = resultRepository.findByTournamentIdOrderByPlacementAsc(tournamentId);

        // Basic statistics
        report.put("tournamentName", tournament.getName());
        report.put("totalParticipants", results.size());
        report.put("tournamentDays", tournament.getNumDays());

        // Aggregate statistics
        int totalTouchdowns = results.stream().mapToInt(TournamentResult::getTouchdownsFor).sum();
        int totalCasualties = results.stream().mapToInt(TournamentResult::getCasualtiesFor).sum();
        int totalMatches = results.stream().mapToInt(TournamentResult::getMatchesPlayed).sum();

        report.put("totalTouchdowns", totalTouchdowns);
        report.put("totalCasualties", totalCasualties);
        report.put("totalMatches", totalMatches);

        if (!results.isEmpty()) {
            report.put("averageTouchdownsPerCoach", (double) totalTouchdowns / results.size());
            report.put("averageCasualtiesPerCoach", (double) totalCasualties / results.size());
        }

        // Race distribution
        Map<String, Long> raceDistribution = results.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getRace().getName(),
                        Collectors.counting()));
        report.put("raceDistribution", raceDistribution);

        // Pariah statistics
        long pariahCount = results.stream()
                .filter(r -> r.getRace().getIsPariah())
                .count();
        report.put("pariahTeamsCount", pariahCount);

        // Top performers
        Map<String, Object> topPerformers = new HashMap<>();

        if (!results.isEmpty()) {
            TournamentResult winner = results.get(0); // First in ordered list
            topPerformers.put("winner", Map.of(
                    "coach", winner.getCoach().getName(),
                    "race", winner.getRace().getName(),
                    "points", winner.getPoints()
            ));

            TournamentResult topTouchdowns = results.stream()
                    .max(Comparator.comparing(TournamentResult::getTouchdownsFor))
                    .orElse(null);
            if (topTouchdowns != null) {
                topPerformers.put("mostTouchdowns", Map.of(
                        "coach", topTouchdowns.getCoach().getName(),
                        "touchdowns", topTouchdowns.getTouchdownsFor()
                ));
            }

            TournamentResult topCasualties = results.stream()
                    .max(Comparator.comparing(TournamentResult::getCasualtiesFor))
                    .orElse(null);
            if (topCasualties != null) {
                topPerformers.put("mostCasualties", Map.of(
                        "coach", topCasualties.getCoach().getName(),
                        "casualties", topCasualties.getCasualtiesFor()
                ));
            }
        }

        report.put("topPerformers", topPerformers);

        // Validation issues
        List<String> validationIssues = validateTournamentData(tournamentId);
        report.put("validationIssues", validationIssues);
        report.put("isValid", validationIssues.isEmpty());

        return report;
    }
}