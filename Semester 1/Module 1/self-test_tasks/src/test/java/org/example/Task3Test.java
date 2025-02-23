package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class Task3Test {
    @Test
    void fixString1(){
        //char[] fixing_string = "123456".toCharArray();
        char[] fixing_string = {'1', '2', '3', '4', '5', '6'};
        char[] result = Task3.fixString(fixing_string);
        assertArrayEquals("214365".toCharArray(), result);
    }

    @Test
    void fixString2(){
        char[] fixing_string = "hTsii  s aimex dpus rtni.g".toCharArray();
        char[] result = Task3.fixString(fixing_string);
        assertArrayEquals("This is a mixed up string.".toCharArray(), result);
    }

    @Test
    void fixString3(){
        char[] fixing_string = "badce".toCharArray();
        char[] result = Task3.fixString(fixing_string);
        assertArrayEquals("abcde".toCharArray(), result);
    }

    @Test
    void fixStringVoid() {
        char[] fixing_string = "".toCharArray();
        char[] result = Task3.fixString(fixing_string);
        assertArrayEquals("".toCharArray(), result);
    }
}
