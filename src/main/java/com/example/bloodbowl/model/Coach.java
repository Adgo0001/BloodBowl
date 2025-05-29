package com.example.bloodbowl.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "coach")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "Trænernavn er påkrævet")
    private String name;

    @Column(name = "naf_number")
    private String nafNumber;

    @Column(name = "email", length = 255)
    @Email(message = "Ugyldig email adresse")
    private String email;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "preferred_contact", length = 50)
    private String preferredContact; // Discord, Email, etc.

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // ADD THIS LINE
    private List<TournamentResult> tournamentResults = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // ADD THIS LINE
    private List<SeriesStandings> seriesStandings = new ArrayList<>();

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "is_active")
    private Boolean isActive = true;
}