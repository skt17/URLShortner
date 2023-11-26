package com.urlshortner.backend.repository;

import com.urlshortner.backend.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingRepository  extends JpaRepository<UrlMapping, Long> {

    //method to get the original URL
    UrlMapping findByShortUrl(String shortUrl);
}