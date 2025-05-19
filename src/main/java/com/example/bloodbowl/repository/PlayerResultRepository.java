package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.PlayerResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerResultRepository extends JpaRepository<PlayerResult, Long> {
}
