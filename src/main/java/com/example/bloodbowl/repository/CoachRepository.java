package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findAllByOrderByWinPercentageDesc();
}
