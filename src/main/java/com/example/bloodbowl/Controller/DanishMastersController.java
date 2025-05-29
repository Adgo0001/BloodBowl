package com.example.bloodbowl.Controller;

import com.example.bloodbowl.DTO.SeriesStandingsDTO;
import com.example.bloodbowl.DTO.TournamentResultDTO;
import com.example.bloodbowl.Model.SeriesStandings;
import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Model.TournamentResult;
import com.example.bloodbowl.Repository.SeriesStandingsRepository;
import com.example.bloodbowl.Repository.TournamentRepository;
import com.example.bloodbowl.Repository.TournamentResultRepository;
import com.example.bloodbowl.Service.EnhancedCsvImportService;
import com.example.bloodbowl.Service.EnhancedSeriesCalculationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


// Updated Spring Boot Controller for Danish Masters
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class DanishMastersController {

    @Autowired
    private SeriesStandingsRepository standingsRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private TournamentResultRepository resultRepository;

    @Autowired
    private EnhancedCsvImportService csvImportService;

    @Autowired
    private EnhancedSeriesCalculationService seriesService;

    // Danish Masters Series endpoints
    @GetMapping("/series/{year}")
    @Transactional
    public ResponseEntity<Map<String, Object>> getSeriesStandings(@PathVariable int year) {
        try {
            // Get main standings (sorted by total points, then tiebreakers)
            List<SeriesStandings> mainStandingsEntities = standingsRepository.findByYear(year);
            List<SeriesStandingsDTO> mainStandings = mainStandingsEntities.stream()
                    .map(this::convertSeriesStandingsToDTO)
                    .sorted(Comparator.comparing(SeriesStandingsDTO::getTotalPoints).reversed()
                            .thenComparing(SeriesStandingsDTO::getTournamentsAttended)
                            .thenComparing(SeriesStandingsDTO::getFirstPlaces, Comparator.reverseOrder())
                            .thenComparing(SeriesStandingsDTO::getSecondPlaces, Comparator.reverseOrder())
                            .thenComparing(SeriesStandingsDTO::getThirdPlaces, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            // Get casualties standings
            List<SeriesStandingsDTO> casualtiesStandings = mainStandingsEntities.stream()
                    .map(this::convertSeriesStandingsToDTO)
                    .filter(s -> s.getTotalCasualties() > 0)
                    .sorted(Comparator.comparing(SeriesStandingsDTO::getTotalCasualties).reversed()
                            .thenComparing(SeriesStandingsDTO::getCasDifference, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            // Get touchdowns standings
            List<SeriesStandingsDTO> touchdownsStandings = mainStandingsEntities.stream()
                    .map(this::convertSeriesStandingsToDTO)
                    .filter(s -> s.getTotalTouchdowns() > 0)
                    .sorted(Comparator.comparing(SeriesStandingsDTO::getTotalTouchdowns).reversed()
                            .thenComparing(SeriesStandingsDTO::getTdDifference, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            // Get Pariah Cup standings
            List<SeriesStandings> pariahEntities = standingsRepository.findByYearAndPariahTournamentsGreaterThan(year, 0);
            List<SeriesStandingsDTO> pariahStandings = pariahEntities.stream()
                    .map(this::convertSeriesStandingsToDTO)
                    .sorted(Comparator.comparing(SeriesStandingsDTO::getPariahPoints).reversed()
                            .thenComparing(SeriesStandingsDTO::getPariahTournaments)
                            .thenComparing(SeriesStandingsDTO::getPariahRaces, Comparator.reverseOrder()))
                    .collect(Collectors.toList());

            Map<String, Object> result = new HashMap<>();
            result.put("mainStandings", mainStandings);
            result.put("casualtiesStandings", casualtiesStandings);
            result.put("touchdownsStandings", touchdownsStandings);
            result.put("pariahStandings", pariahStandings);
            result.put("year", year);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("❌ Error in getSeriesStandings: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved hentning af serie-stillinger: " + e.getMessage()));
        }
    }

    private SeriesStandingsDTO convertSeriesStandingsToDTO(SeriesStandings standings) {
        SeriesStandingsDTO dto = new SeriesStandingsDTO();

        dto.setId(standings.getId());
        dto.setYear(standings.getYear());
        dto.setCoachName(standings.getCoach().getName());
        dto.setTournamentsAttended(standings.getTournamentsAttended());
        dto.setTotalPoints(standings.getTotalPoints());
        dto.setFirstPlaces(standings.getFirstPlaces());
        dto.setSecondPlaces(standings.getSecondPlaces());
        dto.setThirdPlaces(standings.getThirdPlaces());
        dto.setTotalTouchdowns(standings.getTotalTouchdowns());
        dto.setTdDifference(standings.getTdDifference());
        dto.setTotalCasualties(standings.getTotalCasualties());
        dto.setCasDifference(standings.getCasDifference());
        dto.setPariahTournaments(standings.getPariahTournaments());
        dto.setPariahPoints(standings.getPariahPoints());
        dto.setPariahRaces(standings.getPariahRaces());
        dto.setTotalMatchesPlayed(standings.getTotalMatchesPlayed());
        dto.setTotalMatchesWon(standings.getTotalMatchesWon());
        dto.setTotalMatchesDrawn(standings.getTotalMatchesDrawn());
        dto.setTotalMatchesLost(standings.getTotalMatchesLost());
        dto.setAveragePlacement(standings.getAveragePlacement());
        dto.setBestPlacement(standings.getBestPlacement());
        dto.setWorstPlacement(standings.getWorstPlacement());

        // Calculate derived fields
        dto.setWinPercentage(standings.getWinPercentage());
        dto.setAverageTouchdownsPerTournament(standings.getAverageTouchdownsPerTournament());
        dto.setAverageCasualtiesPerTournament(standings.getAverageCasualtiesPerTournament());

        return dto;
    }

    // Tournament management endpoints
    @GetMapping("/tournaments")
    public ResponseEntity<List<Tournament>> getAllTournaments() {
        try {
            List<Tournament> tournaments = tournamentRepository.findAllByOrderByStartDateDesc();
            return ResponseEntity.ok(tournaments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/tournaments/{year}")
    public ResponseEntity<List<Tournament>> getTournamentsByYear(@PathVariable int year) {
        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);

            List<Tournament> tournaments = tournamentRepository.findByStartDateBetweenOrderByStartDateDesc(
                    startOfYear, endOfYear);
            return ResponseEntity.ok(tournaments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/tournaments/details/{id}")  // Changed from /tournaments/{id}
    public ResponseEntity<Tournament> getTournament(@PathVariable Long id) {
        return tournamentRepository.findById(id)
                .map(tournament -> ResponseEntity.ok(tournament))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tournaments/{id}/results")
    @Transactional
    public ResponseEntity<List<TournamentResultDTO>> getTournamentResults(@PathVariable Long id) {
        try {
            System.out.println("🔍 API called for tournament ID: " + id);

            List<TournamentResult> results = resultRepository.findByTournamentIdOrderByPlacementAsc(id);

            System.out.println("📊 Found " + results.size() + " results in database");

            List<TournamentResultDTO> dtos = results.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            System.out.println("📋 Converted to " + dtos.size() + " DTOs");

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            System.out.println("❌ Error in getTournamentResults: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private TournamentResultDTO convertToDTO(TournamentResult result) {
        TournamentResultDTO dto = new TournamentResultDTO();

        dto.setId(result.getId());
        dto.setTournamentName(result.getTournament().getName());
        dto.setCoachName(result.getCoach().getName());
        dto.setRaceName(result.getRace().getName());
        dto.setTeamName(result.getTeamName());
        dto.setPlacement(result.getPlacement());
        dto.setMatchesPlayed(result.getMatchesPlayed());
        dto.setMatchesWon(result.getMatchesWon());
        dto.setMatchesDrawn(result.getMatchesDrawn());
        dto.setMatchesLost(result.getMatchesLost());
        dto.setTouchdownsFor(result.getTouchdownsFor());
        dto.setTouchdownsAgainst(result.getTouchdownsAgainst());
        dto.setCasualtiesFor(result.getCasualtiesFor());
        dto.setCasualtiesAgainst(result.getCasualtiesAgainst());
        dto.setPoints(result.getPoints());
        dto.setSeriesPoints(result.getSeriesPoints());
        dto.setPariah(result.getRace().getIsPariah());

        return dto;
    }

    @PostMapping("/tournaments")
    public ResponseEntity<?> createTournament(@RequestBody Tournament tournament) {
        try {
            // Validate tournament data
            if (tournament.getName() == null || tournament.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Turneringsnavn er påkrævet"));
            }

            if (tournament.getStartDate() == null || tournament.getEndDate() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Start- og slutdato er påkrævet"));
            }

            if (tournament.getStartDate().isAfter(tournament.getEndDate())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Startdato kan ikke være efter slutdato"));
            }

            if (tournament.getNumCoaches() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Antal trænere skal være større end 0"));
            }

            if (tournament.getNumDays() <= 0) {
                tournament.setNumDays(1); // Default to 1 day
            }

            Tournament savedTournament = tournamentRepository.save(tournament);
            return ResponseEntity.ok(savedTournament);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved oprettelse af turnering: " + e.getMessage()));
        }
    }

    @PostMapping("/tournaments/{id}/import")
    public ResponseEntity<?> importTournamentResults(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ingen fil uploadet"));
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Kun CSV filer er tilladt"));
        }

        try {
            // Verify tournament exists
            Tournament tournament = tournamentRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Turnering ikke fundet"));

            // Save uploaded CSV to temporary file
            Path tempFile = Files.createTempFile("tournament-", ".csv");
            file.transferTo(tempFile.toFile());

            // Import data
            csvImportService.importTournamentResults(tempFile.toString(), id);

            // Clean up temporary file
            Files.delete(tempFile);

            return ResponseEntity.ok(Map.of("message", "Turneringsresultater importeret succesfuldt"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved import: " + e.getMessage()));
        }
    }

    @DeleteMapping("/tournaments/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Long id) {
        try {
            if (!tournamentRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            // Delete tournament results first (foreign key constraint)
            resultRepository.deleteByTournamentId(id);

            // Delete tournament
            tournamentRepository.deleteById(id);

            return ResponseEntity.ok(Map.of("message", "Turnering slettet succesfuldt"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved sletning af turnering: " + e.getMessage()));
        }
    }

    // Statistics and reporting endpoints
    @GetMapping("/statistics/{year}")
    public ResponseEntity<Map<String, Object>> getYearlyStatistics(@PathVariable int year) {
        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);

            // Get tournaments for the year
            List<Tournament> tournaments = tournamentRepository.findByStartDateBetween(startOfYear, endOfYear);

            // Get all results for the year
            List<TournamentResult> allResults = new ArrayList<>();
            for (Tournament tournament : tournaments) {
                allResults.addAll(resultRepository.findByTournament(tournament));
            }

            // Calculate statistics
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalTournaments", tournaments.size());
            stats.put("totalParticipants", allResults.stream()
                    .map(r -> r.getCoach().getId())
                    .collect(Collectors.toSet()).size());
            stats.put("totalMatches", allResults.stream()
                    .mapToInt(TournamentResult::getMatchesPlayed).sum());
            stats.put("totalTouchdowns", allResults.stream()
                    .mapToInt(TournamentResult::getTouchdownsFor).sum());
            stats.put("totalCasualties", allResults.stream()
                    .mapToInt(TournamentResult::getCasualtiesFor).sum());

            // Top performing coaches
            Map<String, Integer> coachPoints = new HashMap<>();
            for (TournamentResult result : allResults) {
                String coachName = result.getCoach().getName();
                coachPoints.put(coachName,
                        coachPoints.getOrDefault(coachName, 0) + result.getSeriesPoints());
            }

            List<Map.Entry<String, Integer>> topCoaches = coachPoints.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(10)
                    .collect(Collectors.toList());

            stats.put("topCoaches", topCoaches);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved hentning af statistikker: " + e.getMessage()));
        }
    }

    // Utility endpoint to recalculate all standings for a year
    @PostMapping("/recalculate/{year}")
    public ResponseEntity<?> recalculateStandings(@PathVariable int year) {
        try {
            LocalDate startOfYear = LocalDate.of(year, 1, 1);
            LocalDate endOfYear = LocalDate.of(year, 12, 31);

            List<Tournament> tournaments = tournamentRepository.findByStartDateBetween(startOfYear, endOfYear);

            for (Tournament tournament : tournaments) {
                seriesService.calculateSeriesPointsForTournament(tournament);
            }

            return ResponseEntity.ok(Map.of("message", "Stillinger genberegnet for " + year));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Fejl ved genberegning: " + e.getMessage()));
        }
    }
    @GetMapping("/debug")
    public ResponseEntity<String> debug() {
        return ResponseEntity.ok("DanishMastersController is loaded!");
    }

}