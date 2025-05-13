package com.example.bloodbowl.service;

import com.example.bloodbowl.model.PlayerResult;
import com.example.bloodbowl.repository.PlayerResultRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerResultService {

    private final PlayerResultRepository repository;

    public PlayerResultService(PlayerResultRepository repository) {
        this.repository = repository;
    }

    public List<PlayerResult> getAllResults() {
        return repository.findAll();
    }

    public PlayerResult saveResult(PlayerResult result) {
        return repository.save(result);
    }

    public void deleteResult(Long id) {
        repository.deleteById(id);
    }
}
