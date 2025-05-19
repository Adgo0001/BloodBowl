package com.example.bloodbowl.Model;

import java.util.Date;

public class Tournament {
    private int tournamentId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String location;
    private int participantCount;
    private int tournamentDays;

    public Tournament() {}

    public Tournament(int tournamentId, String name, Date startDate, Date endDate,
                      String location, int participantCount, int tournamentDays) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.participantCount = participantCount;
        this.tournamentDays = tournamentDays;
    }

    // Getters and setters
    public int getTournamentId() { return tournamentId; }
    public void setTournamentId(int tournamentId) { this.tournamentId = tournamentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getParticipantCount() { return participantCount; }
    public void setParticipantCount(int participantCount) { this.participantCount = participantCount; }

    public int getTournamentDays() { return tournamentDays; }
    public void setTournamentDays(int tournamentDays) { this.tournamentDays = tournamentDays; }

    @Override
    public String toString() {
        return "Tournament{" +
                "tournamentId=" + tournamentId +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", location='" + location + '\'' +
                ", participantCount=" + participantCount +
                ", tournamentDays=" + tournamentDays +
                '}';
    }
}