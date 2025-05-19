package com.example.bloodbowl.Model;

public class Race {
    private int raceId;
    private String name;
    private boolean isPariah;

    public Race() {}

    public Race(int raceId, String name, boolean isPariah) {
        this.raceId = raceId;
        this.name = name;
        this.isPariah = isPariah;
    }

    // Getters and setters
    public int getRaceId() { return raceId; }
    public void setRaceId(int raceId) { this.raceId = raceId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isPariah() { return isPariah; }
    public void setPariah(boolean pariah) { isPariah = pariah; }

    @Override
    public String toString() {
        return "Race{" +
                "raceId=" + raceId +
                ", name='" + name + '\'' +
                ", isPariah=" + isPariah +
                '}';
    }
}