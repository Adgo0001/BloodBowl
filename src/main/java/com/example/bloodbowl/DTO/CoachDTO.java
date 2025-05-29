package com.example.bloodbowl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoachDTO {
    private Long id;
    private String name;
    private String nafNumber;
    private String email;
    private String country;
    private String city;
    private int totalTournaments;
    private int totalPoints;
    private LocalDateTime createdDate;
}