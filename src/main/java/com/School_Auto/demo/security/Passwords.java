package com.School_Auto.demo.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Pattern;

/**
 * Central place for password hashing.
 * <p>
 * Passwords are stored as BCrypt hashes. Rows created before hashing was introduced may still
 * contain plain text; {@link #matches} accepts those once so the caller can upgrade them to a hash
 * on the next successful login (see {@link #needsUpgrade}).
 */
public final class Passwords {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    private static final Pattern BCRYPT = Pattern.compile("^\\$2[aby]?\\$\\d{2}\\$[./A-Za-z0-9]{53}$");

    private Passwords() {
    }

    public static boolean isHashed(String value) {
        return value != null && BCRYPT.matcher(value).matches();
    }

    public static String hash(String rawPassword) {
        return ENCODER.encode(rawPassword);
    }

    /** Hashes the value unless it is already a BCrypt hash (e.g. an unchanged value re-submitted by a form). */
    public static String hashIfNeeded(String value) {
        if (value == null || value.isBlank() || isHashed(value)) {
            return value;
        }
        return hash(value);
    }

    public static boolean matches(String rawPassword, String storedPassword) {
        if (rawPassword == null || storedPassword == null) {
            return false;
        }
        if (isHashed(storedPassword)) {
            return ENCODER.matches(rawPassword, storedPassword);
        }
        // Legacy plain-text row: constant-time comparison, then the caller upgrades it.
        return MessageDigest.isEqual(
                rawPassword.getBytes(StandardCharsets.UTF_8),
                storedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean needsUpgrade(String storedPassword) {
        return storedPassword != null && !isHashed(storedPassword);
    }
}
