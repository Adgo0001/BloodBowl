package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.Tournament;
import com.example.bloodbowl.Repository.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/tournaments")
public class TournamentController {

    @Autowired
    private TournamentRepository tournamentRepository;

    @GetMapping
    public String listTournaments(Model model) {
        model.addAttribute("tournaments", tournamentRepository.findAll());
        return "admin/tournaments/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "admin/tournaments/form";
    }

    @PostMapping("/save")
    public String saveTournament(@ModelAttribute Tournament tournament) {
        tournamentRepository.save(tournament);
        return "redirect:/admin/tournaments";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Tournament tournament = tournamentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("tournament", tournament);
        return "admin/tournaments/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteTournament(@PathVariable Long id) {
        tournamentRepository.deleteById(id);
        return "redirect:/admin/tournaments";
    }
}
