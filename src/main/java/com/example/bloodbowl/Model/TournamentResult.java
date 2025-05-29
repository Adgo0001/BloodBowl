package com.example.bloodbowl.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "tournament_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TournamentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    @NotNull(message = "Turnering er påkrævet")
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Træner er påkrævet")
    private Coach coach;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "race_id", nullable = false)
    @NotNull(message = "Race er påkrævet")
    private Race race;

    @Column(name = "team_name", length = 255)
    private String teamName;

    @Column(nullable = false)
    @Min(value = 1, message = "Placering skal være mindst 1")
    private Integer placement;

    @Column(name = "matches_played", nullable = false)
    @Min(value = 0, message = "Antal kampe kan ikke være negativt")
    private Integer matchesPlayed = 0;

    @Column(name = "matches_won", nullable = false)
    @Min(value = 0, message = "Antal vundne kampe kan ikke være negativt")
    private Integer matchesWon = 0;

    @Column(name = "matches_drawn", nullable = false)
    @Min(value = 0, message = "Antal uafgjorte kan ikke være negativt")
    private Integer matchesDrawn = 0;

    @Column(name = "matches_lost", nullable = false)
    @Min(value = 0, message = "Antal tabte kampe kan ikke være negativt")
    private Integer matchesLost = 0;

    @Column(name = "touchdowns_for", nullable = false)
    @Min(value = 0, message = "Touchdowns for kan ikke være negativt")
    private Integer touchdownsFor = 0;

    @Column(name = "touchdowns_against", nullable = false)
    @Min(value = 0, message = "Touchdowns imod kan ikke være negativt")
    private Integer touchdownsAgainst = 0;

    @Column(name = "casualties_for", nullable = false)
    @Min(value = 0, message = "Skader for kan ikke være negativt")
    private Integer casualtiesFor = 0;

    @Column(name = "casualties_against", nullable = false)
    @Min(value = 0, message = "Skader imod kan ikke være negativt")
    private Integer casualtiesAgainst = 0;

    @Column(name = "injuries_for")
    @Min(value = 0, message = "Skader for kan ikke være negativt")
    private Integer injuriesFor = 0;

    @Column(name = "injuries_against")
    @Min(value = 0, message = "Skader imod kan ikke være negativt")
    private Integer injuriesAgainst = 0;

    @Column(name = "completions")
    @Min(value = 0, message = "Fuldføringer kan ikke være negativt")
    private Integer completions = 0;

    @Column(name = "interceptions")
    @Min(value = 0, message = "Afleveringer kan ikke være negativt")
    private Integer interceptions = 0;

    @Column(name = "deaths_for")
    @Min(value = 0, message = "Dødsfald for kan ikke være negativt")
    private Integer deathsFor = 0;

    @Column(name = "deaths_against")
    @Min(value = 0, message = "Dødsfald imod kan ikke være negativt")
    private Integer deathsAgainst = 0;

    @Column(name = "fouls_for")
    @Min(value = 0, message = "Fouls for kan ikke være negativt")
    private Integer foulsFor = 0;

    @Column(name = "fouls_against")
    @Min(value = 0, message = "Fouls imod kan ikke være negativt")
    private Integer foulsAgainst = 0;

    @Column(name = "sent_offs_for")
    @Min(value = 0, message = "Udvisninger for kan ikke være negativt")
    private Integer sentOffsFor = 0;

    @Column(name = "sent_offs_against")
    @Min(value = 0, message = "Udvisninger imod kan ikke være negativt")
    private Integer sentOffsAgainst = 0;

    @Column(name = "points", nullable = false)
    @Min(value = 0, message = "Point kan ikke være negativt")
    private Integer points = 0;

    @Column(name = "series_points", nullable = false)
    @Min(value = 0, message = "Seriepoint kan ikke være negativt")
    private Integer seriesPoints = 0;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    // Calculated fields
    @Transient
    public Integer getTotalMatches() {
        return matchesWon + matchesDrawn + matchesLost;
    }

    @Transient
    public Integer getTouchdownDifference() {
        return touchdownsFor - touchdownsAgainst;
    }

    @Transient
    public Integer getCasualtyDifference() {
        return casualtiesFor - casualtiesAgainst;
    }

    @Transient
    public Double getWinPercentage() {
        if (matchesPlayed == 0) return 0.0;
        return (double) matchesWon / matchesPlayed * 100;
    }

    // Validation method
    @AssertTrue(message = "Kamp statistikker stemmer ikke overens")
    public boolean isValidMatchStatistics() {
        return getTotalMatches().equals(matchesPlayed);
    }
}