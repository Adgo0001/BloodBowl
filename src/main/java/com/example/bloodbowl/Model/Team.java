package com.example.bloodbowl.Model;

public class Team {
    private int teamId;
    private String name;
    private int raceId;
    private int coachId;
    private Race race; // Represents the race object associated with this team

    public Team() {}

    public Team(int teamId, String name, int raceId, int coachId) {
        this.teamId = teamId;
        this.name = name;
        this.raceId = raceId;
        this.coachId = coachId;
    }

    // Getters and setters
    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }

    public Race getRace() { return race; }
    public void setRace(Race race) {
        this.race = race;
        this.raceId = race.getRaceId();
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", name='" + name + '\'' +
                ", raceId=" + raceId +
                ", coachId=" + coachId +
                '}';
    }
}