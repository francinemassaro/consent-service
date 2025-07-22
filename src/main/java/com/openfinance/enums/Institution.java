package com.openfinance.enums;

public enum Institution {
    DIGIO,
    BRADESCO,
    ITAU,
    NUBANK;

    public static boolean isValid(String name) {
        try {
            Institution.valueOf(name.toUpperCase());
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
