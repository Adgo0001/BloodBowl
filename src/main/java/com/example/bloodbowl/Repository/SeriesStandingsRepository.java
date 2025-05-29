package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Model.SeriesStandings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesStandingsRepository extends JpaRepository<SeriesStandings, Long> {
    Optional<SeriesStandings> findByYearAndCoach(int year, Coach coach);
    List<SeriesStandings> findByYear(int year);
    List<SeriesStandings> findByYearAndPariahTournamentsGreaterThan(int year, int minTournaments);

    @Query("SELECT ss FROM SeriesStandings ss WHERE ss.year = :year AND ss.totalPoints > 0 " +
            "ORDER BY ss.totalPoints DESC, ss.tournamentsAttended ASC, ss.firstPlaces DESC")
    List<SeriesStandings> findMainStandingsByYear(@Param("year") int year);

    @Query("SELECT ss FROM SeriesStandings ss WHERE ss.year = :year AND ss.totalCasualties > 0 " +
            "ORDER BY ss.totalCasualties DESC, ss.casDifference DESC")
    List<SeriesStandings> findCasualtiesStandingsByYear(@Param("year") int year);

    @Query("SELECT ss FROM SeriesStandings ss WHERE ss.year = :year AND ss.totalTouchdowns > 0 " +
            "ORDER BY ss.totalTouchdowns DESC, ss.tdDifference DESC")
    List<SeriesStandings> findTouchdownsStandingsByYear(@Param("year") int year);

    @Query("SELECT ss FROM SeriesStandings ss WHERE ss.year = :year AND ss.pariahTournaments > 0 " +
            "ORDER BY ss.pariahPoints DESC, ss.pariahTournaments ASC, ss.pariahRaces DESC")
    List<SeriesStandings> findPariahStandingsByYear(@Param("year") int year);

    @Modifying
    @Query("DELETE FROM SeriesStandings ss WHERE ss.year = :year")
    void deleteByYear(@Param("year") int year);
}