package com.logicea.logiceacardsproject.util;

import com.logicea.logiceacardsproject.enums.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public class CommonUtil {

    public static boolean hasAdminRole(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return (userDetails.getAuthorities().stream().findFirst().get().toString().equalsIgnoreCase(Role.ADMIN.name()));
    }
}
