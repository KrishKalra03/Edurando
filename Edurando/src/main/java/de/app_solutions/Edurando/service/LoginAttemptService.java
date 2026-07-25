package de.app_solutions.Edurando.service;

import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Tracks failed login attempts per IP address.
 * Blocks an IP after MAX_ATTEMPTS failures within WINDOW_MINUTES.
 * The block expires after BLOCK_MINUTES from the first failure in the window.
 */
@Service
public class LoginAttemptService {

    private static final int  MAX_ATTEMPTS   = 5;
    private static final long WINDOW_MINUTES = 15;
    private static final long BLOCK_MINUTES  = 15;

    private final ConcurrentHashMap<String, ArrayDeque<Long>> failures = new ConcurrentHashMap<>();

    /** Returns true when the IP has exceeded the allowed failure threshold. */
    public boolean isBlocked(String ip) {
        purgeExpired(ip);
        ArrayDeque<Long> ts = failures.get(ip);
        return ts != null && ts.size() >= MAX_ATTEMPTS;
    }

    /** Seconds remaining until the block expires (0 if not blocked). */
    public long secondsUntilUnblocked(String ip) {
        ArrayDeque<Long> ts = failures.get(ip);
        if (ts == null || ts.isEmpty()) return 0;
        long unblockAt = ts.peekFirst() + TimeUnit.MINUTES.toMillis(BLOCK_MINUTES);
        long remaining = unblockAt - System.currentTimeMillis();
        return Math.max(0L, remaining / 1000);
    }

    /** Record a failed attempt for the given IP. */
    public void registerFailure(String ip) {
        failures.computeIfAbsent(ip, k -> new ArrayDeque<>())
                .addLast(System.currentTimeMillis());
    }

    /** Clear all recorded failures for the given IP (call on successful login). */
    public void resetFailures(String ip) {
        failures.remove(ip);
    }

    /** Remove timestamps that have fallen outside the sliding window. */
    private void purgeExpired(String ip) {
        failures.computeIfPresent(ip, (k, ts) -> {
            long cutoff = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(WINDOW_MINUTES);
            while (!ts.isEmpty() && ts.peekFirst() < cutoff) {
                ts.pollFirst();
            }
            return ts.isEmpty() ? null : ts;
        });
    }
}
