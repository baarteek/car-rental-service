package com.example.car.rental.model;

public enum Role {
    USER,
    ADMIN;

    public static Role getRoleIgnoreCase(String roleStr) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No constant with text " + roleStr + " found");
    }
}
