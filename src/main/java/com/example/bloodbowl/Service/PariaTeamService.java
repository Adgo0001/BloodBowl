package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Race;
import com.example.bloodbowl.Repository.RaceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PariaTeamService {

    @Autowired
    private RaceRepository raceRepository;

    @PostConstruct
    public void initPariahTeams() {
        // List of Pariah teams for 2025
        List<String> pariahTeams2025 = Arrays.asList(
                "Goblins", "Halflings", "Snotlings", "Gnomes", "Ogres",
                "Nurgle", "Black Orcs", "Chaos Dwarfs", "Slann"
        );

        // Set Pariah status
        for (String raceName : pariahTeams2025) {
            Race race = raceRepository.findByName(raceName)
                    .orElseGet(() -> {
                        Race newRace = new Race();
                        newRace.setName(raceName);
                        return newRace;
                    });

            race.setIsPariah(true);
            raceRepository.save(race);
        }

        // Reset Pariah status for races not in the list
        List<Race> currentPariahRaces = raceRepository.findByIsPariah(true);
        for (Race race : currentPariahRaces) {
            if (!pariahTeams2025.contains(race.getName())) {
                race.setIsPariah(false);
                raceRepository.save(race);
            }
        }
    }
}