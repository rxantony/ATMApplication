package com.bank.atm.domain.common;

public class Guard {
	private Guard() {
	}

	public static void validateArgNotNull(Object arg, String argName) {
		if (arg == null) {
			if (argName == null)
				argName = "arg";
			throw new IllegalArgumentException(String.format("%s can not be null", argName));
		}
	}

	public static void validateArgNotNullOrEmpty(String arg, String argName) {
		if (arg == null || arg.trim().length() == 0) {
			if (argName == null)
				argName = "arg";
			throw new IllegalArgumentException(String.format("%s is required", argName));
		}
	}

	public static void validateArgMustBeGreaterThan(int arg, int reff, String argName) {
		if (arg <= reff) {
			if (argName == null)
				argName = "arg";
			throw new IllegalArgumentException(String.format("%s must be greater than %d", argName, reff));
		}
	}

}