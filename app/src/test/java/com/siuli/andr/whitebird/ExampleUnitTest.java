package com.siuli.andr.whitebird;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void createUniqID(){
        System.out.println("id : " + UUID.randomUUID().toString());
    }
}