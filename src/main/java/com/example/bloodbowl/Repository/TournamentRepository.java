package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
