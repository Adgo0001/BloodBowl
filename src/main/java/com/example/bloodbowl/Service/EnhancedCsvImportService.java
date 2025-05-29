package com.example.bloodbowl.Service;

import com.opencsv.exceptions.CsvValidationException;
import com.opencsv.CSVReader;
import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Model.Race;
import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Model.TournamentResult;
import com.example.bloodbowl.Repository.CoachRepository;
import com.example.bloodbowl.Repository.RaceRepository;
import com.example.bloodbowl.Repository.TournamentRepository;
import com.example.bloodbowl.Repository.TournamentResultRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

@Service
@Transactional
public class EnhancedCsvImportService {

    private static final Logger logger = LoggerFactory.getLogger(EnhancedCsvImportService.class);

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private RaceRepository raceRepository;

    @Autowired
    private TournamentResultRepository resultRepository;

    @Autowired
    private EnhancedSeriesCalculationService seriesService;

    // Pariah teams for 2025 - could be made configurable
    private final Set<String> PARIAH_TEAMS_2025 = Set.of(
            "Goblins", "Halflings", "Snotlings", "Gnomes", "Ogres",
            "Nurgle", "Black Orcs", "Chaos Dwarfs", "Slann"
    );

    public void importTournamentResults(String filePath, Long tournamentId) throws IOException, CsvValidationException {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new EntityNotFoundException("Turnering ikke fundet"));

        logger.info("Starter import af turneringsresultater for: {}", tournament.getName());

        List<TournamentResult> results = new ArrayList<>();
        int rowCount = 0;

        // Convert semicolon-delimited file to comma-delimited
        String csvContent = convertSemicolonToComma(filePath);

        try (Reader reader = new StringReader(csvContent)) {
            CSVReader csvReader = new CSVReader(reader);

            // Read and validate header
            String[] header = csvReader.readNext();
            Map<String, Integer> columnMap = validateAndMapCsvHeader(header);

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                rowCount++;

                // Skip empty rows
                if (isEmptyRow(line)) {
                    continue;
                }

                try {
                    TournamentResult result = processCsvLine(line, tournament, columnMap);
                    if (result != null) {
                        results.add(result);
                    }
                } catch (Exception e) {
                    logger.error("Fejl ved behandling af række {}: {}", rowCount, e.getMessage());
                    throw new RuntimeException("Fejl i række " + rowCount + ": " + e.getMessage());
                }
            }
        }

        if (results.isEmpty()) {
            throw new RuntimeException("Ingen gyldige resultater fundet i CSV filen");
        }

        // Validate tournament size matches
        if (results.size() != tournament.getNumCoaches()) {
            logger.warn("Antal resultater ({}) matcher ikke forventet antal trænere ({})",
                    results.size(), tournament.getNumCoaches());
        }

        // Delete existing results for this tournament
        resultRepository.deleteByTournamentId(tournamentId);

        // Save all results
        resultRepository.saveAll(results);

        logger.info("Importerede {} resultater for turnering: {}", results.size(), tournament.getName());

        // Calculate series points
        seriesService.calculateSeriesPointsForTournament(tournament);

        logger.info("Seriepoint beregnet for turnering: {}", tournament.getName());
    }

    private String convertSemicolonToComma(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader fileReader = new FileReader(filePath)) {
            Scanner scanner = new Scanner(fileReader);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // Simply replace semicolons with commas
                String convertedLine = line.replace(';', ',');
                content.append(convertedLine).append("\n");
            }

            scanner.close();
        }

        return content.toString();
    }

    private Map<String, Integer> validateAndMapCsvHeader(String[] header) {
        if (header == null || header.length < 20) {
            throw new RuntimeException("Ugyldig CSV header - forventede mindst 20 kolonner, fandt: " + (header != null ? header.length : 0));
        }

        // Create a map of column names to their indices (case-insensitive)
        Map<String, Integer> columnMap = new HashMap<>();
        for (int i = 0; i < header.length; i++) {
            String cleanHeader = cleanString(header[i]).toLowerCase();
            columnMap.put(cleanHeader, i);
        }

        // Map the expected columns to actual CSV columns
        Map<String, Integer> mappedColumns = new HashMap<>();

        // Required columns mapping
        mappedColumns.put("rank", findColumn(columnMap, "rank"));
        mappedColumns.put("coach", findColumn(columnMap, "coach"));
        mappedColumns.put("teamrace", findColumn(columnMap, "teamrace"));
        mappedColumns.put("teamname", findColumn(columnMap, "teamname"));
        mappedColumns.put("points", findColumn(columnMap, "points"));
        mappedColumns.put("played", findColumn(columnMap, "played"));
        mappedColumns.put("wins", findColumn(columnMap, "wins"));
        mappedColumns.put("draws", findColumn(columnMap, "draws"));
        mappedColumns.put("losses", findColumn(columnMap, "losses"));
        mappedColumns.put("tdfor", findColumn(columnMap, "tdfor"));
        mappedColumns.put("tdagainst", findColumn(columnMap, "tdagainst"));
        mappedColumns.put("casualtiesfor", findColumn(columnMap, "casualtiesfor"));
        mappedColumns.put("casualtiesagainst", findColumn(columnMap, "casualtiesagainst"));

        // Optional columns
        mappedColumns.put("injuriesfor", findColumnOptional(columnMap, "injuriesfor"));
        mappedColumns.put("injuriesagainst", findColumnOptional(columnMap, "injuriesagainst"));
        mappedColumns.put("completions", findColumnOptional(columnMap, "completions"));
        mappedColumns.put("interceptions", findColumnOptional(columnMap, "interceptions"));

        return mappedColumns;
    }

    private Integer findColumn(Map<String, Integer> columnMap, String columnName) {
        Integer index = columnMap.get(columnName);
        if (index == null) {
            throw new RuntimeException("Manglende påkrævet kolonne: " + columnName + ". Tilgængelige kolonner: " + columnMap.keySet());
        }
        return index;
    }

    private Integer findColumnOptional(Map<String, Integer> columnMap, String columnName) {
        return columnMap.get(columnName);
    }

    private boolean isEmptyRow(String[] line) {
        if (line == null || line.length == 0) return true;

        for (String cell : line) {
            if (cleanString(cell).length() > 0) {
                return false;
            }
        }
        return true;
    }

    private TournamentResult processCsvLine(String[] line, Tournament tournament, Map<String, Integer> columnMap) {
        if (line == null || line.length < 20) {
            throw new RuntimeException("Ugyldig række - for få kolonner: " + (line != null ? line.length : 0));
        }

        try {
            // Extract data from CSV line using column mapping
            String coachName = cleanString(getColumnValue(line, columnMap, "coach"));
            String raceName = cleanString(getColumnValue(line, columnMap, "teamrace"));
            String teamName = cleanString(getColumnValue(line, columnMap, "teamname"));

            if (coachName.isEmpty()) {
                throw new RuntimeException("Træner navn kan ikke være tomt");
            }

            // Normalize race name
            final String normalizedRaceName = normalizeRaceName(raceName);

            // Find or create coach
            Coach coach = coachRepository.findByName(coachName)
                    .orElseGet(() -> {
                        Coach newCoach = new Coach();
                        newCoach.setName(coachName);
                        return coachRepository.save(newCoach);
                    });

            // Find or create race
            Race race = raceRepository.findByName(normalizedRaceName)
                    .orElseGet(() -> {
                        Race newRace = new Race();
                        newRace.setName(normalizedRaceName);
                        newRace.setIsPariah(PARIAH_TEAMS_2025.contains(normalizedRaceName));
                        return raceRepository.save(newRace);
                    });

            // Create tournament result
            TournamentResult result = new TournamentResult();
            result.setTournament(tournament);
            result.setCoach(coach);
            result.setRace(race);
            result.setTeamName(teamName);

            // Parse numeric fields
            result.setPlacement(parseInteger(getColumnValue(line, columnMap, "rank"), "Placering"));
            result.setMatchesPlayed(parseInteger(getColumnValue(line, columnMap, "played"), "Kampe spillet"));
            result.setMatchesWon(parseInteger(getColumnValue(line, columnMap, "wins"), "Kampe vundet"));
            result.setMatchesDrawn(parseInteger(getColumnValue(line, columnMap, "draws"), "Uafgjorte"));
            result.setMatchesLost(parseInteger(getColumnValue(line, columnMap, "losses"), "Kampe tabt"));
            result.setTouchdownsFor(parseInteger(getColumnValue(line, columnMap, "tdfor"), "Touchdowns for"));
            result.setTouchdownsAgainst(parseInteger(getColumnValue(line, columnMap, "tdagainst"), "Touchdowns imod"));
            result.setCasualtiesFor(parseInteger(getColumnValue(line, columnMap, "casualtiesfor"), "Skader for"));
            result.setCasualtiesAgainst(parseInteger(getColumnValue(line, columnMap, "casualtiesagainst"), "Skader imod"));
            result.setPoints(parseInteger(getColumnValue(line, columnMap, "points"), "Point"));

            // Parse optional fields
            result.setInjuriesFor(parseIntegerSafe(getColumnValueSafe(line, columnMap, "injuriesfor"), 0));
            result.setInjuriesAgainst(parseIntegerSafe(getColumnValueSafe(line, columnMap, "injuriesagainst"), 0));
            result.setCompletions(parseIntegerSafe(getColumnValueSafe(line, columnMap, "completions"), 0));
            result.setInterceptions(parseIntegerSafe(getColumnValueSafe(line, columnMap, "interceptions"), 0));

            // Validate data
            validateResultData(result);

            return result;

        } catch (Exception e) {
            throw new RuntimeException("Fejl ved behandling af CSV række: " + e.getMessage());
        }
    }

    private String getColumnValue(String[] line, Map<String, Integer> columnMap, String columnName) {
        Integer index = columnMap.get(columnName);
        if (index == null) {
            throw new RuntimeException("Kolonne ikke fundet: " + columnName);
        }
        if (index >= line.length) {
            throw new RuntimeException("Kolonne index uden for række: " + columnName);
        }
        return line[index];
    }

    private String getColumnValueSafe(String[] line, Map<String, Integer> columnMap, String columnName) {
        Integer index = columnMap.get(columnName);
        if (index == null || index >= line.length) {
            return "";
        }
        return line[index];
    }

    private String normalizeRaceName(String raceName) {
        switch (raceName.toLowerCase().trim()) {
            case "chaos dwarf":
                return "Chaos Dwarfs";
            case "necromantic horror":
                return "Necromantic";
            default:
                return raceName.trim();
        }
    }

    private void validateResultData(TournamentResult result) {
        if (result.getPlacement() <= 0) {
            throw new RuntimeException("Placering skal være større end 0");
        }

        if (result.getTouchdownsFor() < 0 || result.getTouchdownsAgainst() < 0) {
            throw new RuntimeException("Touchdowns kan ikke være negative");
        }

        if (result.getCasualtiesFor() < 0 || result.getCasualtiesAgainst() < 0) {
            throw new RuntimeException("Skader kan ikke være negative");
        }
    }

    private String cleanString(String str) {
        return str != null ? str.trim() : "";
    }

    private int parseInteger(String str, String fieldName) {
        try {
            return Integer.parseInt(cleanString(str));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Ugyldigt tal for " + fieldName + ": " + str);
        }
    }

    private int parseIntegerSafe(String str, int defaultValue) {
        try {
            String cleaned = cleanString(str);
            return cleaned.isEmpty() ? defaultValue : Integer.parseInt(cleaned);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Method to update Pariah teams for a new year
    public void updatePariahTeams(int year, Set<String> pariahTeamNames) {
        logger.info("Opdaterer Pariah teams for år: {}", year);

        List<Race> allRaces = raceRepository.findAll();
        for (Race race : allRaces) {
            race.setIsPariah(false);
        }
        raceRepository.saveAll(allRaces);

        for (String raceName : pariahTeamNames) {
            Race race = raceRepository.findByName(raceName)
                    .orElseGet(() -> {
                        Race newRace = new Race();
                        newRace.setName(raceName);
                        return newRace;
                    });
            race.setIsPariah(true);
            raceRepository.save(race);
        }

        logger.info("Opdaterede {} Pariah teams", pariahTeamNames.size());
    }

    // Method to validate CSV file before import
    public Map<String, Object> validateCsvFile(String filePath) throws IOException {
        Map<String, Object> validation = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try {
            String csvContent = convertSemicolonToComma(filePath);

            try (Reader reader = new StringReader(csvContent)) {
                CSVReader csvReader = new CSVReader(reader);

                String[] header = csvReader.readNext();
                try {
                    validateAndMapCsvHeader(header);
                } catch (RuntimeException e) {
                    errors.add("Header fejl: " + e.getMessage());
                }

                int rowCount = 0;
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    rowCount++;

                    if (isEmptyRow(line)) {
                        continue;
                    }

                    if (line.length < 20) {
                        errors.add("Række " + rowCount + ": For få kolonner");
                    }
                }

                validation.put("totalRows", rowCount);
                validation.put("errors", errors);
                validation.put("warnings", warnings);
                validation.put("valid", errors.isEmpty());
            }

        } catch (Exception e) {
            errors.add("Fil læsnings fejl: " + e.getMessage());
            validation.put("valid", false);
            validation.put("errors", errors);
        }

        return validation;
    }
}