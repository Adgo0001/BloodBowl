package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    Optional<Coach> findByName(String name);

    @Query("SELECT c FROM Coach c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Coach> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT COUNT(DISTINCT c) FROM Coach c JOIN TournamentResult tr ON c.id = tr.coach.id " +
            "JOIN Tournament t ON tr.tournament.id = t.id WHERE YEAR(t.startDate) = :year")
    long countActiveCoachesByYear(@Param("year") int year);
}