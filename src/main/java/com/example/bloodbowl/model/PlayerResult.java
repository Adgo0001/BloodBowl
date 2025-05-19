package com.example.bloodbowl.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PlayerResult {

    @Id
    private String nafNick;

    private int totalScore;
    private int touchdowns;
    private int casualties;
    private int stuntyScore;
    private int tourneys;


    public String getNafNick() {
        return nafNick;
    }

    public void setNafNick(String nafNick) {
        this.nafNick = nafNick;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getTouchdowns() {
        return touchdowns;
    }

    public void setTouchdowns(int touchdowns) {
        this.touchdowns = touchdowns;
    }

    public int getCasualties() {
        return casualties;
    }

    public void setCasualties(int casualties) {
        this.casualties = casualties;
    }

    public int getStuntyScore() {
        return stuntyScore;
    }

    public void setStuntyScore(int stuntyScore) {
        this.stuntyScore = stuntyScore;
    }

    public int getTourneys() {
        return tourneys;
    }

    public void setTourneys(int tourneys) {
        this.tourneys = tourneys;
    }
}
