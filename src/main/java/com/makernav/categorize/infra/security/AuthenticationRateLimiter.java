package com.makernav.categorize.infra.security;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AuthenticationRateLimiter {

    private static final int MAX_ATTEMPTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(5);

    private final Map<String, Deque<Instant>> attempts = new ConcurrentHashMap<>();

    public void validate(String clientIp) {
        var now = Instant.now();
        var queue = attempts.computeIfAbsent(clientIp, key -> new ArrayDeque<>());

        synchronized (queue) {
            while (!queue.isEmpty() && queue.peekFirst().isBefore(now.minus(WINDOW))) {
                queue.pollFirst();
            }
            if (queue.size() >= MAX_ATTEMPTS) {
                throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS,
                        "Too many authentication requests. Please wait a few minutes.");
            }
            queue.addLast(now);
        }
    }
}
