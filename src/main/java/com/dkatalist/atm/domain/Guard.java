package com.dkatalist.atm.domain;

public class Guard {
    private Guard() {
    }

    public static void validateArgNotNull(Object arg, String argName) {
        if (arg == null)
            throw new IllegalArgumentException(String.format("%s can not be null", argName));
    }

    public static void validateArgNotNullOrEmpty(String arg, String argName) {
        if (arg == null || arg.trim().length() == 0)
            throw new IllegalArgumentException(String.format("%s is required", argName));
    }

    public static void validateArgMustBeGreaterThan(int arg, int reff, String argName) {
        if (arg <= reff)
            throw new IllegalArgumentException(String.format("%s must be greater than %d", argName, reff));
    }
    
}