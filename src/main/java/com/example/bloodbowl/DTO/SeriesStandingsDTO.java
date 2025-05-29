package com.example.bloodbowl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesStandingsDTO {
    private Long id;
    private Integer year;
    private String coachName;
    private Integer tournamentsAttended;
    private Integer totalPoints;
    private Integer firstPlaces;
    private Integer secondPlaces;
    private Integer thirdPlaces;
    private Integer totalTouchdowns;
    private Integer tdDifference;
    private Integer totalCasualties;
    private Integer casDifference;
    private Integer pariahTournaments;
    private Integer pariahPoints;
    private Integer pariahRaces;
    private Integer totalMatchesPlayed;
    private Integer totalMatchesWon;
    private Integer totalMatchesDrawn;
    private Integer totalMatchesLost;
    private Double averagePlacement;
    private Integer bestPlacement;
    private Integer worstPlacement;

    // Calculated fields
    private Double winPercentage;
    private Double averageTouchdownsPerTournament;
    private Double averageCasualtiesPerTournament;
    private Double averagePariahPointsPerTournament;
}