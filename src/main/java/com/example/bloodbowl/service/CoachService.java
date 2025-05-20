package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Repository.CoachRepository;
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
