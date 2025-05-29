package com.example.bloodbowl.Service;

import com.example.bloodbowl.DTO.CoachDTO;
import com.example.bloodbowl.DTO.SeriesStandingsDTO;
import com.example.bloodbowl.DTO.TournamentDTO;
import com.example.bloodbowl.DTO.TournamentResultDTO;
import com.example.bloodbowl.Model.Coach;
import com.example.bloodbowl.Model.SeriesStandings;
import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Model.TournamentResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DTOConversionService {

    @Autowired
    private ModelMapper modelMapper;

    public TournamentDTO convertToDTO(Tournament tournament) {
        TournamentDTO dto = modelMapper.map(tournament, TournamentDTO.class);
        dto.setTotalResults(tournament.getResults().size());
        return dto;
    }

    public CoachDTO convertToDTO(Coach coach) {
        CoachDTO dto = modelMapper.map(coach, CoachDTO.class);
        dto.setTotalTournaments(coach.getTournamentResults().size());
        dto.setTotalPoints(coach.getSeriesStandings().stream()
                .mapToInt(SeriesStandings::getTotalPoints).sum());
        return dto;
    }

    public TournamentResultDTO convertToDTO(TournamentResult result) {
        TournamentResultDTO dto = modelMapper.map(result, TournamentResultDTO.class);
        dto.setTournamentName(result.getTournament().getName());
        dto.setCoachName(result.getCoach().getName());
        dto.setRaceName(result.getRace().getName());
        dto.setPariah(result.getRace().getIsPariah());
        return dto;
    }

    public SeriesStandingsDTO convertToDTO(SeriesStandings standings) {
        SeriesStandingsDTO dto = modelMapper.map(standings, SeriesStandingsDTO.class);
        dto.setCoachName(standings.getCoach().getName());
        dto.setWinPercentage(standings.getWinPercentage());
        dto.setAverageTouchdownsPerTournament(standings.getAverageTouchdownsPerTournament());
        dto.setAverageCasualtiesPerTournament(standings.getAverageCasualtiesPerTournament());
        dto.setAveragePariahPointsPerTournament(standings.getAveragePariahPointsPerTournament());
        return dto;
    }
}