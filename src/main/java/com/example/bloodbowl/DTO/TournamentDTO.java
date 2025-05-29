package com.example.bloodbowl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TournamentDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int numCoaches;
    private int numDays;
    private String description;
    private String location;
    private String organizer;
    private int totalResults;
    private LocalDateTime createdDate;
}