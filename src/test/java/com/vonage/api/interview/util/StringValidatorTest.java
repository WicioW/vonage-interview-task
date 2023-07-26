package com.vonage.api.interview.util;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
    private final PrintStream err = new PrintStream(errBuffer, true);

    private final StringValidator stringValidator = new StringValidator(err);

    private static final String ERROR_MESSAGE = "Error: %s cannot be empty." + System.lineSeparator();

    @Test
    void testValidateIsNotEmpty() {
        assertTrue(stringValidator.validateIsNotEmpty("test", "test"));
        assertFalse(stringValidator.validateIsNotEmpty("", "test"));
        assertFalse(stringValidator.validateIsNotEmpty(null, "test"));
    }

    @Test
    void testValidateIsNotEmptyErrorMessage() {
        stringValidator.validateIsNotEmpty("", "b195yqR");
        assertEquals(ERROR_MESSAGE.formatted("b195yqR"), errBuffer.toString());
    }


}