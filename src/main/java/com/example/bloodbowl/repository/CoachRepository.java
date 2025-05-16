package com.example.bloodbowl.repository;

import com.example.bloodbowl.model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findAllByOrderByWinPercentageDesc();
}
