package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.PlayerResult;
import com.example.bloodbowl.Repository.PlayerResultRepository;
import org.springframework.data.domain.Sort;
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

    public List<PlayerResult> getAllResultsSorted(Sort sort) {
        return repository.findAll(sort);
    }

    public PlayerResult saveResult(PlayerResult result) {
        return repository.save(result);
    }

    public void deleteResult(Long id) {
        repository.deleteById(id);
    }
}
