package com.example.bloodbowl.service;

import com.example.bloodbowl.model.Coach;
import com.example.bloodbowl.repository.CoachRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {
    private final CoachRepository coachRepository;

    public CoachService(CoachRepository coachRepository) {
        this.coachRepository = coachRepository;
    }

    public List<Coach> findAllCoaches() {
        return coachRepository.findAll();
    }
}
