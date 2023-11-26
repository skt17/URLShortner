package com.urlshortner.backend.service;

import com.urlshortner.backend.entity.UrlMapping;
import com.urlshortner.backend.repository.UrlMappingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UrlMappingService {

    @Autowired
    private  UrlMappingRepository urlMappingRepository;

    public UrlMapping saveUrlMapping(UrlMapping urlMapping) {
        return urlMappingRepository.save(urlMapping);
    }

    public UrlMapping findByShortUrl(String shortUrl) {
        return urlMappingRepository.findByShortUrl(shortUrl);
    }
}
