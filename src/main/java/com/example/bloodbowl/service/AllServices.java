package com.example.bloodbowl.service;

import com.example.bloodbowl.model.Event;
import com.example.bloodbowl.model.News;
import com.example.bloodbowl.repository.EventRepository;
import com.example.bloodbowl.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllServices {
    @Autowired
    private NewsRepository newsRepo;
    @Autowired private EventRepository eventRepo;

    public List<News> getAllNews() {
        return newsRepo.findAll();
    }

    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }
}
