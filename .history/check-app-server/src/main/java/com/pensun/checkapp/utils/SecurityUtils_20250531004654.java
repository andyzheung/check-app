package com.pensun.checkapp.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.pensun.checkapp.entity.User; 

public class SecurityUtils {
    
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            User user = (User) authentication.getPrincipal();
            return user.getId();
        }
        return null;
    }
} 