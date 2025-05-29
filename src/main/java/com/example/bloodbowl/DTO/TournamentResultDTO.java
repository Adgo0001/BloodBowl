package com.example.bloodbowl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResultDTO {
    private Long id;
    private String tournamentName;
    private String coachName;
    private String raceName;
    private String teamName;
    private int placement;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesDrawn;
    private int matchesLost;
    private int touchdownsFor;
    private int touchdownsAgainst;
    private int casualtiesFor;
    private int casualtiesAgainst;
    private int points;
    private int seriesPoints;
    private boolean isPariah;
}