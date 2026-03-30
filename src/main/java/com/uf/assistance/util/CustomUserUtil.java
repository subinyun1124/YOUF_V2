package com.uf.assistance.util;

import com.uf.assistance.config.jwt.CustomUserDetails;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Log4j2
public class CustomUserUtil {

    private CustomUserUtil() {}

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public static String getCurrentUserLoginId() {
        return getCurrentUser().getUserId();
    }
}