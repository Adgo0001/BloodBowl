package com.example.bloodbowl.Service;

import com.example.bloodbowl.Model.Event;
import com.example.bloodbowl.Model.News;
import com.example.bloodbowl.Repository.EventRepository;
import com.example.bloodbowl.Repository.NewsRepository;
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
