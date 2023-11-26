package com.urlshortner.backend.controller;

import com.urlshortner.backend.entity.UrlMapping;
import com.urlshortner.backend.pojo.UrlShort;
import com.urlshortner.backend.service.UrlMappingService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpHeaders;

@RestController
@Slf4j
public class Controller {
    @GetMapping("/ping")
    public String getHello(){
       // log.info("hello");
        return "hello";
    }

    @Autowired
    private UrlMappingService service;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);


    @PostMapping("/shorten")
    public String shortenUrl(@RequestBody UrlShort urlShort) {
        String originalUrl=urlShort.getUrl();
        logger.info("Received request to shorten URL: {}", originalUrl);

        // Extract the path part of the URL (not including the domain)
        String path = extractPath(originalUrl);

        // Generate a short key based on the extracted path
        String shortKey = generateShortKey(path);

        // Save the mapping in the database
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setShortUrl(shortKey);
        service.saveUrlMapping(urlMapping);

        logger.info("Shortened URL. Short key: {}, Original URL: {}", shortKey, originalUrl);
        return shortKey;

    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortKey) {
        logger.info("Received request to redirect to original URL for short key: {}", shortKey);

        // get the original URL based on the short key
        UrlMapping urlMapping = service.findByShortUrl(shortKey);

        if (urlMapping != null) {
            String originalUrl = urlMapping.getOriginalUrl();
            logger.info("Redirecting to original URL: {}", originalUrl);
          return   ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();

        } else {
            logger.warn("Short URL not found for short key: {}", shortKey);
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private String extractPath(String url) {
        // Extract the path part of the URL (excluding the domain)
        // Assuming the path starts after the first "/"
        int domainIndex = url.indexOf("/", url.indexOf("//") + 2);
        return (domainIndex != -1) ? url.substring(domainIndex) : url;
    }

    private String generateShortKey(String path) {
        //TODO: use any other robust method apart form this randomUUID()
        return java.util.UUID.randomUUID().toString().substring(0, 8);
    }
}
