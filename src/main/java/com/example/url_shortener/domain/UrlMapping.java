package com.example.url_shortener.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UrlMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2048)
    private String originalUrl;

    @Column(unique = true, length = 10)
    private String shortCode;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public UrlMapping(String originalUrl) {
        this.originalUrl = originalUrl;
        this.createdAt = LocalDateTime.now();
    }

    public void assignShortCode(String shortCode) {
        this.shortCode = shortCode;
    }
}