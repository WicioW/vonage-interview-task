package com.vonage.api.interview.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    private final StringValidator testObj = new StringValidator();

    @Test
    void isEmpty_shouldReturnTrue_whenStringIsEmptyOrNull() {
        //when-then
        assertTrue(testObj.isEmpty(""));
        assertTrue(testObj.isEmpty(null));
    }

    @Test
    void isEmpty_shouldReturnFalse_whenStringHasContent(){
        //when-then
        assertFalse(testObj.isEmpty("test"));
    }

}