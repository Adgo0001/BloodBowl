package com.example.bloodbowl.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "race")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "Race navn er påkrævet")
    private String name;

    @Column(name = "is_pariah", nullable = false)
    private Boolean isPariah = false;

    @Column(name = "tier", length = 50)
    private String tier; // Tier 1, Tier 2, etc.

    @Column(name = "description", length = 1000)
    private String description;

    @OneToMany(mappedBy = "race", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TournamentResult> tournamentResults = new ArrayList<>();

    @Column(name = "is_active")
    private Boolean isActive = true;
}