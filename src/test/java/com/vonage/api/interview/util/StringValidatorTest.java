package com.vonage.api.interview.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    private final ByteArrayOutputStream errBuffer = new ByteArrayOutputStream();
    private final PrintStream err = new PrintStream(errBuffer, true);

    private final StringValidator stringValidator = new StringValidator(err);

    private static final String ERROR_MESSAGE = "Error: %s cannot be empty." + System.lineSeparator();

    @AfterEach
    public void close() {
        err.close();
    }

    @Test
    void testValidateIsNotEmpty() {
        assertFalse(stringValidator.isEmpty("test", "test"));
        assertTrue(stringValidator.isEmpty("", "test"));
        assertTrue(stringValidator.isEmpty(null, "test"));
    }

    @Test
    void testValidateIsNotEmptyErrorMessage() {
        stringValidator.isEmpty("", "b195yqR");
        assertEquals(ERROR_MESSAGE.formatted("b195yqR"), errBuffer.toString());
    }


}