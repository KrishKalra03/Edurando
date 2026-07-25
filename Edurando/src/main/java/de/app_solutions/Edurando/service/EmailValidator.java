package de.app_solutions.Edurando.service;

import de.app_solutions.Edurando.repository.UserProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Slf4j
public class EmailValidator {

    private static final String HIPOLABS_URL = "http://universities.hipolabs.com/search?domain=";

    /** Domains explicitly allowed regardless of academic check (for development/testing). */
    private static final List<String> ALLOWED_DOMAINS = List.of(
            "gmail.com", "outlook.com", "hotmail.com"
    );

    /**
     * Academic domain patterns used as fallback when the Hipolabs API returns no result.
     * Each pattern is checked against both the original domain and its parent domain
     * (e.g. for stud.th-luebeck.de we also check th-luebeck.de).
     */
    private static final List<Pattern> ACADEMIC_PATTERNS = List.of(
        // German prefix-based academic domains: th-*, fh-*, hs-*, uni-*, haw-*, htw-*, etc.
        Pattern.compile(
            "^(uni|fh|th|hs|haw|htw|hbk|hfm|hfk|hmt|hfg|hfh|ph|eah|ehs|eh|oth|bht|rwu|" +
            "h-da|h-brs|hsbi|thws|thga|thu|thf|jade|leuphana|burg|btk|hawk|dhge|dhfpg|" +
            "mh|hfwu|hkom|hcu|hgb|muthesius|hszg|hmtm|hbksaar|palucca|hfbk|hbk|akkon|" +
            "khsb|ksh|khkt|khm|khb|kh|hspv|hska|hshl|hsf|hsba|hf|hdb|hdm|hdwm|hchp|" +
            "hcom|hc)[-.].*\\.de$",
            Pattern.CASE_INSENSITIVE),
        // Student subdomains: stud.th-luebeck.de, stud.uni-xyz.de
        Pattern.compile("^stud\\..*\\.de$", Pattern.CASE_INSENSITIVE),
        // US and most international: .edu TLD
        Pattern.compile(".+\\.edu$", Pattern.CASE_INSENSITIVE),
        // UK: .ac.uk
        Pattern.compile(".+\\.ac\\.uk$", Pattern.CASE_INSENSITIVE),
        // Austria: .ac.at
        Pattern.compile(".+\\.ac\\.at$", Pattern.CASE_INSENSITIVE),
        // Australia: .edu.au
        Pattern.compile(".+\\.edu\\.au$", Pattern.CASE_INSENSITIVE),
        // Japan: .ac.jp
        Pattern.compile(".+\\.ac\\.jp$", Pattern.CASE_INSENSITIVE),
        // New Zealand: .ac.nz
        Pattern.compile(".+\\.ac\\.nz$", Pattern.CASE_INSENSITIVE),
        // South Africa: .ac.za
        Pattern.compile(".+\\.ac\\.za$", Pattern.CASE_INSENSITIVE),
        // Brazil: .edu.br
        Pattern.compile(".+\\.edu\\.br$", Pattern.CASE_INSENSITIVE),
        // Netherlands: .nl academic institutions
        Pattern.compile(".+\\.(?:uva|vu|tue|utwente|rug|uu|leidenuniv|tudelft|ru|um)\\.nl$",
                Pattern.CASE_INSENSITIVE),
        // Switzerland: ETH, EPFL, Uni Zurich
        Pattern.compile(".+\\.(?:ethz|epfl|uzh|unibas|unige|unisg|unibe|unifr|unil)\\.ch$",
                Pattern.CASE_INSENSITIVE)
    );

    // Permanently cache domain lookups for the lifetime of the process
    private final Map<String, Boolean> domainCache = new ConcurrentHashMap<>();

    private final UserProfileRepository userProfileRepository;
    private final RestTemplate restTemplate;

    public EmailValidator(UserProfileRepository userProfileRepository,
                          RestTemplateBuilder restTemplateBuilder) {
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    public Pair<Boolean, String> testMail(String email) {
        boolean valid = true;
        StringBuilder message = new StringBuilder();

        String[] parts = email.split("@");
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
            return Pair.of(false, "Invalid email format.");
        }
        String domain = parts[1].toLowerCase();

        // Uniqueness check — single targeted query instead of loading all users
        if (userProfileRepository.findUserProfileByUsername(email).isPresent()) {
            message.append("Email is already registered.");
            valid = false;
        }

        // Academic domain check (skip for explicitly allowed domains)
        if (!ALLOWED_DOMAINS.contains(domain) && !isAcademicDomain(domain)) {
            message.append("Please use a university or college email address.");
            valid = false;
        }

        return Pair.of(valid, message.toString());
    }

    // ── Domain resolution ──────────────────────────────────────────────────────

    private boolean isAcademicDomain(String domain) {
        Boolean cached = domainCache.get(domain);
        if (cached != null) return cached;

        // Check the domain itself, then its immediate parent (handles stud.th-luebeck.de → th-luebeck.de)
        List<String> candidates = buildCandidates(domain);

        for (String candidate : candidates) {
            if (queryHipolabs(candidate) || matchesAcademicPattern(candidate)) {
                domainCache.put(domain, true);
                return true;
            }
        }

        domainCache.put(domain, false);
        return false;
    }

    /** Queries the Hipolabs API. Returns true when at least one institution matches. */
    private boolean queryHipolabs(String domain) {
        try {
            Object[] result = restTemplate.getForObject(HIPOLABS_URL + domain, Object[].class);
            return result != null && result.length > 0;
        } catch (Exception e) {
            log.warn("Hipolabs API unavailable for domain '{}': {}", domain, e.getMessage());
            return false;
        }
    }

    /** Checks the domain against the compiled academic patterns. */
    private boolean matchesAcademicPattern(String domain) {
        return ACADEMIC_PATTERNS.stream().anyMatch(p -> p.matcher(domain).matches());
    }

    /**
     * Returns the domain itself plus its parent domain (one label stripped from the left).
     * Example: stud.th-luebeck.de → [stud.th-luebeck.de, th-luebeck.de]
     * A top-level domain like lmu.de → [lmu.de]  (no single-label parent to add)
     */
    private static List<String> buildCandidates(String domain) {
        String[] labels = domain.split("\\.");
        if (labels.length <= 2) return List.of(domain);
        String parent = domain.substring(domain.indexOf('.') + 1);
        return List.of(domain, parent);
    }
}
