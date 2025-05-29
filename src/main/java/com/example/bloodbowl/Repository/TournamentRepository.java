package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByStartDateBetween(LocalDate start, LocalDate end);
    List<Tournament> findAllByOrderByStartDateDesc();
    List<Tournament> findByStartDateBetweenOrderByStartDateDesc(LocalDate start, LocalDate end);

    @Query("SELECT t FROM Tournament t WHERE YEAR(t.startDate) = :year ORDER BY t.startDate DESC")
    List<Tournament> findByYearOrderByStartDateDesc(@Param("year") int year);
}