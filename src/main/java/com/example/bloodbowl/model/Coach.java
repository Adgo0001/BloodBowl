package com.example.bloodbowl.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nafNick;
    private Integer nafId;
    private Double coachRating;
    private Double yearHigh;
    private String country;
    private Double allTimeHigh;
    private Integer currentStreakWithoutLoss;
    private Integer longestStreakWithoutLoss;
    private Integer nafGamesPlayed;
    private Double winPercentage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNafNick() {
        return nafNick;
    }

    public void setNafNick(String nafNick) {
        this.nafNick = nafNick;
    }

    public Integer getNafId() {
        return nafId;
    }

    public void setNafId(Integer nafId) {
        this.nafId = nafId;
    }

    public Double getCoachRating() {
        return coachRating;
    }

    public void setCoachRating(Double coachRating) {
        this.coachRating = coachRating;
    }

    public Double getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(Double yearHigh) {
        this.yearHigh = yearHigh;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Double getAllTimeHigh() {
        return allTimeHigh;
    }

    public void setAllTimeHigh(Double allTimeHigh) {
        this.allTimeHigh = allTimeHigh;
    }

    public Integer getCurrentStreakWithoutLoss() {
        return currentStreakWithoutLoss;
    }

    public void setCurrentStreakWithoutLoss(Integer currentStreakWithoutLoss) {
        this.currentStreakWithoutLoss = currentStreakWithoutLoss;
    }

    public Integer getLongestStreakWithoutLoss() {
        return longestStreakWithoutLoss;
    }

    public void setLongestStreakWithoutLoss(Integer longestStreakWithoutLoss) {
        this.longestStreakWithoutLoss = longestStreakWithoutLoss;
    }

    public Integer getNafGamesPlayed() {
        return nafGamesPlayed;
    }

    public void setNafGamesPlayed(Integer nafGamesPlayed) {
        this.nafGamesPlayed = nafGamesPlayed;
    }

    public Double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(Double winPercentage) {
        this.winPercentage = winPercentage;
    }
}
