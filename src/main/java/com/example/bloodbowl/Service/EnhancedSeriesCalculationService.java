package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.*;
import com.example.bloodbowl.Repository.SeriesStandingsRepository;
import com.example.bloodbowl.Repository.TournamentResultRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bloodbowl.Repository.TournamentRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EnhancedSeriesCalculationService {

    private static final Logger logger = LoggerFactory.getLogger(EnhancedSeriesCalculationService.class);

    @Autowired
    private TournamentResultRepository resultRepository;

    @Autowired
    private SeriesStandingsRepository standingsRepository;

    @Autowired
    private DanishMastersUtilityService utilityService;

    @Autowired
    private TournamentRepository tournamentRepository;

    public void calculateSeriesPointsForTournament(Tournament tournament) {
        logger.info("Beregner seriepoint for turnering: {}", tournament.getName());

        List<TournamentResult> results = resultRepository.findByTournament(tournament);
        int numCoaches = tournament.getNumCoaches();
        int numDays = tournament.getNumDays();

        for (TournamentResult result : results) {
            // Calculate series points using utility service
            int seriesPoints = utilityService.calculateTotalSeriesPoints(
                    result.getPlacement(), numCoaches, numDays);

            result.setSeriesPoints(seriesPoints);
            resultRepository.save(result);

            // Update series standings
            updateSeriesStandings(result);
        }

        logger.info("Seriepoint beregnet for {} resultater", results.size());
    }

    public void updateSeriesStandings(TournamentResult result) {
        int year = utilityService.getTournamentYear(result.getTournament().getStartDate());
        Coach coach = result.getCoach();
        Race race = result.getRace();

        SeriesStandings standings = standingsRepository.findByYearAndCoach(year, coach)
                .orElseGet(() -> {
                    SeriesStandings newStandings = new SeriesStandings();
                    newStandings.setYear(year);
                    newStandings.setCoach(coach);
                    return newStandings;
                });

        // Recalculate all statistics for this coach/year
        recalculateAllStandingsForCoach(standings, year, coach);

        standingsRepository.save(standings);
    }

    private void recalculateAllStandingsForCoach(SeriesStandings standings, int year, Coach coach) {
        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        List<TournamentResult> allResults = resultRepository.findByCoachAndTournamentStartDateBetween(
                coach, startOfYear, endOfYear);

        // Reset all counters
        standings.setTournamentsAttended(allResults.size());
        standings.setFirstPlaces(0);
        standings.setSecondPlaces(0);
        standings.setThirdPlaces(0);
        standings.setTotalTouchdowns(0);
        standings.setTdDifference(0);
        standings.setTotalCasualties(0);
        standings.setCasDifference(0);
        standings.setPariahTournaments(0);
        standings.setPariahRaces(0);
        standings.setTotalMatchesPlayed(0);
        standings.setTotalMatchesWon(0);
        standings.setTotalMatchesDrawn(0);
        standings.setTotalMatchesLost(0);

        // Collect placement data for average calculation
        List<Integer> placements = new ArrayList<>();
        Set<String> pariahRacesPlayed = new HashSet<>();
        List<Integer> pariahMatchPoints = new ArrayList<>();

        // Aggregate statistics
        for (TournamentResult result : allResults) {
            // Basic statistics
            standings.setTotalTouchdowns(standings.getTotalTouchdowns() + result.getTouchdownsFor());
            standings.setTdDifference(standings.getTdDifference() + result.getTouchdownDifference());
            standings.setTotalCasualties(standings.getTotalCasualties() + result.getCasualtiesFor());
            standings.setCasDifference(standings.getCasDifference() + result.getCasualtyDifference());

            standings.setTotalMatchesPlayed(standings.getTotalMatchesPlayed() + result.getMatchesPlayed());
            standings.setTotalMatchesWon(standings.getTotalMatchesWon() + result.getMatchesWon());
            standings.setTotalMatchesDrawn(standings.getTotalMatchesDrawn() + result.getMatchesDrawn());
            standings.setTotalMatchesLost(standings.getTotalMatchesLost() + result.getMatchesLost());

            // Placement statistics
            placements.add(result.getPlacement());
            if (result.getPlacement() == 1) {
                standings.setFirstPlaces(standings.getFirstPlaces() + 1);
            } else if (result.getPlacement() == 2) {
                standings.setSecondPlaces(standings.getSecondPlaces() + 1);
            } else if (result.getPlacement() == 3) {
                standings.setThirdPlaces(standings.getThirdPlaces() + 1);
            }

            // Pariah statistics
            if (result.getRace().getIsPariah()) {
                standings.setPariahTournaments(standings.getPariahTournaments() + 1);
                pariahRacesPlayed.add(result.getRace().getName());
                int pariahPoints = utilityService.calculatePariahPoints(
                        result.getMatchesWon(), result.getMatchesDrawn());
                pariahMatchPoints.add(pariahPoints);
            }
        }

        standings.setPariahRaces(pariahRacesPlayed.size());

        // Calculate placement statistics
        if (!placements.isEmpty()) {
            standings.setAveragePlacement(utilityService.calculateAveragePlacement(placements));
            standings.setBestPlacement(Collections.min(placements));
            standings.setWorstPlacement(Collections.max(placements));
        }

        // Calculate total points (top 5 results)
        List<TournamentResult> topResults = allResults.stream()
                .sorted(Comparator.comparing(TournamentResult::getSeriesPoints).reversed())
                .limit(5)
                .collect(Collectors.toList());

        int totalPoints = topResults.stream()
                .mapToInt(TournamentResult::getSeriesPoints)
                .sum();
        standings.setTotalPoints(totalPoints);

        // Calculate Pariah Cup points (top 4 Pariah results)
        List<Integer> topPariahPoints = pariahMatchPoints.stream()
                .sorted(Comparator.reverseOrder())
                .limit(4)
                .collect(Collectors.toList());

        int totalPariahPoints = topPariahPoints.stream()
                .mapToInt(Integer::intValue)
                .sum();
        standings.setPariahPoints(totalPariahPoints);
    }

    public void recalculateAllStandingsForYear(int year) {
        logger.info("Genberegner alle stillinger for år: {}", year);

        LocalDate startOfYear = LocalDate.of(year, 1, 1);
        LocalDate endOfYear = LocalDate.of(year, 12, 31);

        // Delete existing standings for the year
        standingsRepository.deleteByYear(year);

        // Get all tournaments for the year
        List<Tournament> tournaments = tournamentRepository.findByStartDateBetween(startOfYear, endOfYear);

        // Recalculate each tournament
        for (Tournament tournament : tournaments) {
            calculateSeriesPointsForTournament(tournament);
        }

        logger.info("Genberegning fuldført for {} turneringer i {}", tournaments.size(), year);
    }
}