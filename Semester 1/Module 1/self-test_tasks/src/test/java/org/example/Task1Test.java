package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task1Test {
    @Test
    void minutesToSeconds1(){
        String str = "01:00";
        int result = Task1.minutesToSeconds(str);
        assertEquals(60, result);
    }

    @Test
    void minutesToSeconds2(){
        String str = "13:56";
        int result = Task1.minutesToSeconds(str);
        assertEquals(836, result);
    }

    @Test
    void minutesToSeconds3(){
        String str = "100:20";
        int result = Task1.minutesToSeconds(str);
        assertEquals(6020, result);
    }

    @Test
    void minutesToSecondsIncorrectInput1(){
        String str = "10:60";
        int result = Task1.minutesToSeconds(str);
        assertEquals(-1, result);
    }

    @Test
    void minutesToSecondsIncorrectInput2(){
        String str = "aa:60";
        int result = Task1.minutesToSeconds(str);
        assertEquals(-1, result);
    }

    @Test
    void minutesToSecondsIncorrectInput3(){
        String str = "10:bb";
        int result = Task1.minutesToSeconds(str);
        assertEquals(-1, result);
    }

    @Test
    void minutesToSecondsNegativeInput1(){
        String str = "10:-20";
        int result = Task1.minutesToSeconds(str);
        assertEquals(-1, result);
    }

    @Test
    void minutesToSecondsNegativeInput2(){
        String str = "-10:20";
        int result = Task1.minutesToSeconds(str);
        assertEquals(-1, result);
    }
}
