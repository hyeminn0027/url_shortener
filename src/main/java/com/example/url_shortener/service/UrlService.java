package com.example.url_shortener.service;

import com.example.url_shortener.domain.UrlMapping;
import com.example.url_shortener.repository.UrlMappingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlMappingRepository urlMappingRepository;

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    // 1. 긴 URL을 받아서 짧은 URL을 생성하는 메서드
    public String createShortUrl(String originalUrl) {
        // (1) 먼저 원본 URL만 담아서 저장 → id가 자동으로 생성됨
        UrlMapping urlMapping = new UrlMapping(originalUrl);
        UrlMapping saved = urlMappingRepository.save(urlMapping);

        // (2) 생성된 id를 Base62로 인코딩
        String shortCode = encodeBase62(saved.getId());

        // (3) shortCode를 채워서 다시 저장
        saved.assignShortCode(shortCode);
        urlMappingRepository.save(saved);

        return shortCode;
    }

    // 2. 짧은 코드로 원본 URL을 찾는 메서드
    public String getOriginalUrl(String shortCode) {
        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 짧은 URL입니다: " + shortCode));
        return urlMapping.getOriginalUrl();
    }

    // id(숫자)를 Base62 문자열로 변환하는 로직
    private String encodeBase62(Long id) {
        StringBuilder sb = new StringBuilder();
        long num = id;

        if (num == 0) {
            return String.valueOf(BASE62.charAt(0));
        }

        while (num > 0) {
            int remainder = (int) (num % 62);
            sb.append(BASE62.charAt(remainder));
            num = num / 62;
        }

        return sb.reverse().toString();
    }
}