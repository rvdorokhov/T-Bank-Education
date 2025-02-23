package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Task4Test {
    @Test
    void isPalindromeDescendantTrue1(){
        int check = 11211230;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantTrue2(){
        int check = 13001120;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantTrue3(){
        int check = 23336014;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantTrue4(){
        int check = 11;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantTrue5(){
        int check = 12321;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantTrue6(){
        int check = 13413222;
        boolean result = Task4.isPalindromeDescendant(check);
        assertTrue(result);
    }

    @Test
    void isPalindromeDescendantFalse1(){
        int check = 13211230;
        boolean result = Task4.isPalindromeDescendant(check);
        assertFalse(result);
    }

    @Test
    void isPalindromeDescendantFalse2(){
        int check = 12;
        boolean result = Task4.isPalindromeDescendant(check);
        assertFalse(result);
    }
}
