package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Task5Test {
    @Test
    void countK1(){
        int number = 3524;
        int result = Task5.countK(number);
        assertEquals(3, result);
    }

    @Test
    void countK2(){
        int number = 6621;
        int result = Task5.countK(number);
        assertEquals(5, result);
    }

    @Test
    void countK3(){
        int number = 6554;
        int result = Task5.countK(number);
        assertEquals(4, result);
    }

    @Test
    void countK4(){
        int number = 1234;
        int result = Task5.countK(number);
        assertEquals(3, result);
    }

    @Test
    void countK5(){
        int number = 6174;
        int result = Task5.countK(number);
        assertEquals(1, result);
    }
}
