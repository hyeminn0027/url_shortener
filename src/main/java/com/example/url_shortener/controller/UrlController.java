package com.example.url_shortener.controller;

import com.example.url_shortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    // 1. 긴 URL을 받아서 짧은 URL을 만들어주는 API
    @PostMapping("/api/urls")
    public ResponseEntity<Map<String, String>> createShortUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("originalUrl");

        String shortCode = urlService.createShortUrl(originalUrl);
        String shortUrl = "http://localhost:8080/" + shortCode;

        return ResponseEntity.ok(Map.of("shortUrl", shortUrl));
    }

    // 2. 짧은 URL로 접속하면 원래 URL로 리다이렉트해주는 API
    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String originalUrl = urlService.getOriginalUrl(shortCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 리다이렉트
    }
}