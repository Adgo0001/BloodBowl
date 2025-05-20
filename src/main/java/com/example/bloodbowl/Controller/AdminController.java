package com.example.bloodbowl.Controller;

import com.example.bloodbowl.Model.News;
import com.example.bloodbowl.Repository.NewsRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/news")
public class AdminController {

    private final NewsRepository newsRepository;

    public AdminController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping
    public String listNews(Model model) {
        model.addAttribute("newsList", newsRepository.findAll());
        return "news_list";
    }

    @GetMapping("/create")
    public String createNewsForm(Model model) {
        model.addAttribute("news", new News());
        return "news_form";
    }

    @PostMapping("/save")
    public String saveNews(@ModelAttribute News news) {
        newsRepository.save(news);
        return "redirect:/admin/news";
    }

    @GetMapping("/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        News news = newsRepository.findById(id).orElseThrow();
        model.addAttribute("news", news);
        return "news_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
        return "redirect:/admin/news";
    }
}
