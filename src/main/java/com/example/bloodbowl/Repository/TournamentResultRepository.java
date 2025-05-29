package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Model.Race;
import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Model.TournamentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentResultRepository extends JpaRepository<TournamentResult, Long> {
    List<TournamentResult> findByTournament(Tournament tournament);
    List<TournamentResult> findByTournamentIdOrderByPlacementAsc(Long tournamentId);

    List<TournamentResult> findByCoachAndTournamentStartDateBetween(
            Coach coach, LocalDate startDate, LocalDate endDate);

    boolean existsByCoachAndRaceAndTournamentStartDateBetween(
            Coach coach, Race race, LocalDate startDate, LocalDate endDate);

    @Modifying
    @Query("DELETE FROM TournamentResult tr WHERE tr.tournament.id = :tournamentId")
    void deleteByTournamentId(@Param("tournamentId") Long tournamentId);

    @Query("SELECT tr FROM TournamentResult tr JOIN tr.tournament t " +
            "WHERE tr.coach = :coach AND YEAR(t.startDate) = :year " +
            "ORDER BY tr.seriesPoints DESC")
    List<TournamentResult> findTopResultsByCoachAndYear(@Param("coach") Coach coach, @Param("year") int year);

    @Query("SELECT tr FROM TournamentResult tr JOIN tr.tournament t " +
            "WHERE tr.race.isPariah = true AND tr.coach = :coach AND YEAR(t.startDate) = :year " +
            "ORDER BY (tr.matchesWon + tr.matchesDrawn) DESC")
    List<TournamentResult> findTopPariahResultsByCoachAndYear(@Param("coach") Coach coach, @Param("year") int year);

    @Query("SELECT SUM(tr.touchdownsFor), SUM(tr.touchdownsAgainst), SUM(tr.casualtiesFor), SUM(tr.casualtiesAgainst) " +
            "FROM TournamentResult tr JOIN tr.tournament t WHERE YEAR(t.startDate) = :year")
    Object[] getTotalStatsByYear(@Param("year") int year);

    @Query("SELECT tr.coach.name, SUM(tr.touchdownsFor) FROM TournamentResult tr JOIN tr.tournament t " +
            "WHERE YEAR(t.startDate) = :year GROUP BY tr.coach.name ORDER BY SUM(tr.touchdownsFor) DESC")
    List<Object[]> getTopTouchdownScorersByYear(@Param("year") int year);

    @Query("SELECT tr.coach.name, SUM(tr.casualtiesFor) FROM TournamentResult tr JOIN tr.tournament t " +
            "WHERE YEAR(t.startDate) = :year GROUP BY tr.coach.name ORDER BY SUM(tr.casualtiesFor) DESC")
    List<Object[]> getTopCasualtyScorersByYear(@Param("year") int year);

    @Query("SELECT tr FROM TournamentResult tr " +
            "JOIN FETCH tr.coach " +
            "JOIN FETCH tr.race " +
            "JOIN FETCH tr.tournament " +
            "WHERE tr.tournament.id = :tournamentId " +
            "ORDER BY tr.placement ASC")
    List<TournamentResult> findByTournamentIdWithDetailsOrderByPlacementAsc(@Param("tournamentId") Long tournamentId);
}