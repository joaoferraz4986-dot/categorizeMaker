package com.makernav.categorize.infra.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

@Component
public class AuthenticationRateLimiter {

    private static final int MAX_ATTEMPTS = 10;
    private static final Duration WINDOW = Duration.ofMinutes(5);

    private final Cache<String, Deque<Instant>> attempts = Caffeine.newBuilder()
            .expireAfterWrite( 5, TimeUnit.MINUTES )
            .maximumSize( 30 )
            .build();

    public void validate( String clientIp ) {
        var now = Instant.now();

        var queue = attempts.get( clientIp, key -> new ArrayDeque<>() );

        synchronized ( queue ) {

            while ( !queue.isEmpty() && queue.peekFirst().isBefore(now.minus(WINDOW)) ) {
                queue.pollFirst();
            }

            if ( queue.size() >= MAX_ATTEMPTS ) {
                throw new ResponseStatusException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "Muitas tentativas, aguarde alguns minutos."
                );
            }

            queue.addLast( now );
        }

        attempts.put( clientIp, queue );
    }
}