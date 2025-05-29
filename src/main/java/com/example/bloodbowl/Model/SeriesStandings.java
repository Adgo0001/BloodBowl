package com.example.bloodbowl.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

import java.beans.Transient;
import java.time.LocalDateTime;

@Entity
@Table(name = "series_standings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"year", "coach_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SeriesStandings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(value = 2020, message = "År skal være mindst 2020")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coach_id", nullable = false)
    @NotNull(message = "Træner er påkrævet")
    private Coach coach;

    @Column(name = "tournaments_attended", nullable = false)
    @Min(value = 0, message = "Antal turneringer kan ikke være negativt")
    private Integer tournamentsAttended = 0;

    @Column(name = "total_points", nullable = false)
    @Min(value = 0, message = "Totale point kan ikke være negativt")
    private Integer totalPoints = 0;

    @Column(name = "first_places", nullable = false)
    @Min(value = 0, message = "Første pladser kan ikke være negativt")
    private Integer firstPlaces = 0;

    @Column(name = "second_places", nullable = false)
    @Min(value = 0, message = "Anden pladser kan ikke være negativt")
    private Integer secondPlaces = 0;

    @Column(name = "third_places", nullable = false)
    @Min(value = 0, message = "Tredje pladser kan ikke være negativt")
    private Integer thirdPlaces = 0;

    @Column(name = "total_touchdowns", nullable = false)
    @Min(value = 0, message = "Totale touchdowns kan ikke være negativt")
    private Integer totalTouchdowns = 0;

    @Column(name = "td_difference", nullable = false)
    private Integer tdDifference = 0;

    @Column(name = "total_casualties", nullable = false)
    @Min(value = 0, message = "Totale skader kan ikke være negativt")
    private Integer totalCasualties = 0;

    @Column(name = "cas_difference", nullable = false)
    private Integer casDifference = 0;

    @Column(name = "pariah_tournaments", nullable = false)
    @Min(value = 0, message = "Pariah turneringer kan ikke være negativt")
    private Integer pariahTournaments = 0;

    @Column(name = "pariah_points", nullable = false)
    @Min(value = 0, message = "Pariah point kan ikke være negativt")
    private Integer pariahPoints = 0;

    @Column(name = "pariah_races", nullable = false)
    @Min(value = 0, message = "Pariah racer kan ikke være negativt")
    private Integer pariahRaces = 0;

    @Column(name = "total_matches_played")
    @Min(value = 0, message = "Totale kampe kan ikke være negativt")
    private Integer totalMatchesPlayed = 0;

    @Column(name = "total_matches_won")
    @Min(value = 0, message = "Totale vundne kampe kan ikke være negativt")
    private Integer totalMatchesWon = 0;

    @Column(name = "total_matches_drawn")
    @Min(value = 0, message = "Totale uafgjorte kan ikke være negativt")
    private Integer totalMatchesDrawn = 0;

    @Column(name = "total_matches_lost")
    @Min(value = 0, message = "Totale tabte kampe kan ikke være negativt")
    private Integer totalMatchesLost = 0;

    @Column(name = "average_placement")
    private Double averagePlacement;

    @Column(name = "best_placement")
    private Integer bestPlacement;

    @Column(name = "worst_placement")
    private Integer worstPlacement;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    // Calculated methods
    @Transient
    public Double getWinPercentage() {
        if (totalMatchesPlayed == null || totalMatchesPlayed == 0) return 0.0;
        return (double) totalMatchesWon / totalMatchesPlayed * 100;
    }

    @Transient
    public Integer getTotalPodiumPlaces() {
        return (firstPlaces != null ? firstPlaces : 0) +
                (secondPlaces != null ? secondPlaces : 0) +
                (thirdPlaces != null ? thirdPlaces : 0);
    }

    @Transient
    public Double getAverageTouchdownsPerTournament() {
        if (tournamentsAttended == null || tournamentsAttended == 0) return 0.0;
        return (double) (totalTouchdowns != null ? totalTouchdowns : 0) / tournamentsAttended;
    }

    @Transient
    public Double getAverageCasualtiesPerTournament() {
        if (tournamentsAttended == null || tournamentsAttended == 0) return 0.0;
        return (double) (totalCasualties != null ? totalCasualties : 0) / tournamentsAttended;
    }

    @Transient
    public Double getAveragePariahPointsPerTournament() {
        if (pariahTournaments == null || pariahTournaments == 0) return 0.0;
        return (double) (pariahPoints != null ? pariahPoints : 0) / pariahTournaments;
    }
}