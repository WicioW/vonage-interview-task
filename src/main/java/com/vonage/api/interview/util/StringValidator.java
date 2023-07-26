package com.vonage.api.interview.util;

import java.io.PrintStream;

public class StringValidator {

    private final PrintStream err;

    public StringValidator(PrintStream err) {
        this.err = err;
    }

    public boolean validateIsNotEmpty(String string, String valueName) {
        if (string == null || string.isEmpty()) {
            err.println("Error: " + valueName + " cannot be empty.");
            return false;
        }
        return true;
    }
}
