package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RaceRepository extends JpaRepository<Race, Long> {
    Optional<Race> findByName(String name);
    List<Race> findByIsPariah(boolean isPariah);

    @Query("SELECT r FROM Race r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Race> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT r.name, COUNT(tr) FROM Race r JOIN TournamentResult tr ON r.id = tr.race.id " +
            "JOIN Tournament t ON tr.tournament.id = t.id WHERE YEAR(t.startDate) = :year " +
            "GROUP BY r.name ORDER BY COUNT(tr) DESC")
    List<Object[]> findMostPopularRacesByYear(@Param("year") int year);
}