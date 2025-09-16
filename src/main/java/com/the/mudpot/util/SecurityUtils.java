package com.the.mudpot.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public final class SecurityUtils {
    private SecurityUtils() {}

    public static Optional<String> currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return Optional.empty();

        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) {
            Object sub = jwt.getClaims().get("sub");
            return Optional.ofNullable(sub).map(Object::toString);
        }

        // Fallback: if your setup stores name as principal
        if (principal != null) return Optional.of(principal.toString());
        return Optional.empty();
    }
}
