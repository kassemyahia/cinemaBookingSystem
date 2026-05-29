package com.example.cinemabookingsystem.services;

import com.example.cinemabookingsystem.entity.UserRole;

public final class SecurityRoleUtils {
    private SecurityRoleUtils() {
    }

    public static String authority(UserRole role) {
        return "ROLE_" + role.name().toUpperCase();
    }

    public static String tokenRole(UserRole role) {
        return role.name().toUpperCase();
    }

    public static String responseRole(UserRole role) {
        return role.name();
    }
}
