package com.example.bloodbowl.Model;
import com.example.bloodbowl.model.Coach;

public class TournamentResult {
    private int resultId;
    private int tournamentId;
    private int coachId;
    private int teamId;
    private int placement;
    private int matchesPlayed;
    private int matchesWon;
    private int matchesDrawn;
    private int matchesLost;
    private int touchdownsFor;
    private int touchdownsAgainst;
    private int casualtiesFor;
    private int casualtiesAgainst;

    // Navigation properties
    private Tournament tournament;
    private Coach coach;
    private Team team;

    public TournamentResult() {}

    public TournamentResult(int resultId, int tournamentId, int coachId, int teamId, int placement,
                            int matchesPlayed, int matchesWon, int matchesDrawn, int matchesLost,
                            int touchdownsFor, int touchdownsAgainst, int casualtiesFor, int casualtiesAgainst) {
        this.resultId = resultId;
        this.tournamentId = tournamentId;
        this.coachId = coachId;
        this.teamId = teamId;
        this.placement = placement;
        this.matchesPlayed = matchesPlayed;
        this.matchesWon = matchesWon;
        this.matchesDrawn = matchesDrawn;
        this.matchesLost = matchesLost;
        this.touchdownsFor = touchdownsFor;
        this.touchdownsAgainst = touchdownsAgainst;
        this.casualtiesFor = casualtiesFor;
        this.casualtiesAgainst = casualtiesAgainst;
    }

    // Getters and setters
    public int getResultId() { return resultId; }
    public void setResultId(int resultId) { this.resultId = resultId; }

    public int getTournamentId() { return tournamentId; }
    public void setTournamentId(int tournamentId) { this.tournamentId = tournamentId; }

    public int getCoachId() { return coachId; }
    public void setCoachId(int coachId) { this.coachId = coachId; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public int getPlacement() { return placement; }
    public void setPlacement(int placement) { this.placement = placement; }

    public int getMatchesPlayed() { return matchesPlayed; }
    public void setMatchesPlayed(int matchesPlayed) { this.matchesPlayed = matchesPlayed; }

    public int getMatchesWon() { return matchesWon; }
    public void setMatchesWon(int matchesWon) { this.matchesWon = matchesWon; }

    public int getMatchesDrawn() { return matchesDrawn; }
    public void setMatchesDrawn(int matchesDrawn) { this.matchesDrawn = matchesDrawn; }

    public int getMatchesLost() { return matchesLost; }
    public void setMatchesLost(int matchesLost) { this.matchesLost = matchesLost; }

    public int getTouchdownsFor() { return touchdownsFor; }
    public void setTouchdownsFor(int touchdownsFor) { this.touchdownsFor = touchdownsFor; }

    public int getTouchdownsAgainst() { return touchdownsAgainst; }
    public void setTouchdownsAgainst(int touchdownsAgainst) { this.touchdownsAgainst = touchdownsAgainst; }

    public int getCasualtiesFor() { return casualtiesFor; }
    public void setCasualtiesFor(int casualtiesFor) { this.casualtiesFor = casualtiesFor; }

    public int getCasualtiesAgainst() { return casualtiesAgainst; }
    public void setCasualtiesAgainst(int casualtiesAgainst) { this.casualtiesAgainst = casualtiesAgainst; }

    // Navigation property getters and setters
    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
        this.tournamentId = tournament.getTournamentId();
    }

    public Coach getCoach() { return coach; }
    public void setCoach(com.example.bloodbowl.model.Coach coach) {
        this.coach = coach;
        this.coachId = coach.getId();
    }

    public Team getTeam() { return team; }
    public void setTeam(Team team) {
        this.team = team;
        this.teamId = team.getTeamId();
    }

    // Calculated properties
    public int getTouchdownDifference() {
        return touchdownsFor - touchdownsAgainst;
    }

    public int getCasualtyDifference() {
        return casualtiesFor - casualtiesAgainst;
    }

    @Override
    public String toString() {
        return "TournamentResult{" +
                "resultId=" + resultId +
                ", tournamentId=" + tournamentId +
                ", coachId=" + coachId +
                ", teamId=" + teamId +
                ", placement=" + placement +
                ", matchesPlayed=" + matchesPlayed +
                ", matchesWon=" + matchesWon +
                ", matchesDrawn=" + matchesDrawn +
                ", matchesLost=" + matchesLost +
                ", touchdownsFor=" + touchdownsFor +
                ", touchdownsAgainst=" + touchdownsAgainst +
                ", casualtiesFor=" + casualtiesFor +
                ", casualtiesAgainst=" + casualtiesAgainst +
                '}';
    }
}