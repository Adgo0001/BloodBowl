package com.example.bloodbowl.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournament")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Turneringsnavn er påkrævet")
    private String name;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Startdato er påkrævet")
    @JsonProperty("startDate")  // Maps JavaScript startDate to start_date
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "Slutdato er påkrævet")
    @JsonProperty("endDate")    // Maps JavaScript endDate to end_date
    private LocalDate endDate;

    @Column(name = "num_coaches", nullable = false)
    @Min(value = 1, message = "Antal trænere skal være mindst 1")
    @JsonProperty("numCoaches") // Maps JavaScript numCoaches to num_coaches
    private int numCoaches;

    @Column(name = "num_days", nullable = false)
    @Min(value = 1, message = "Antal dage skal være mindst 1")
    @JsonProperty("numDays")    // Maps JavaScript numDays to num_days
    private int numDays = 1;

    @Column(length = 1000)
    private String description;

    @Column(length = 255)
    private String location;

    @Column(name = "organizer", length = 255)
    private String organizer;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore  // Keep this one with @JsonIgnore
    private List<TournamentResult> results = new ArrayList<>();

    @Column(name = "created_date")
    @JsonProperty("createdDate") // Maps JavaScript createdDate to created_date
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    @JsonProperty("modifiedDate") // Maps JavaScript modifiedDate to modified_date
    private LocalDateTime modifiedDate;

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = LocalDateTime.now();
    }

    // Custom validation method
    @AssertTrue(message = "Slutdato skal være efter eller samme som startdato")
    public boolean isValidDateRange() {
        return endDate == null || startDate == null || !endDate.isBefore(startDate);
    }
}