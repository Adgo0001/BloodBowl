package com.example.bloodbowl.repository;

import com.example.bloodbowl.model.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, Long> {
}
