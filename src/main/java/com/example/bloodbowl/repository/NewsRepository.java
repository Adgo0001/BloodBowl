package com.example.bloodbowl.Repository;

import com.example.bloodbowl.Model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {}

