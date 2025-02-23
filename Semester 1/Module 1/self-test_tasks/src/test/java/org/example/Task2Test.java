package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class Task2Test {
    @Test
    void isNestableTrue1(){
        int[] nesting = {1, 2, 3, 4};
        int[] mass = {0, 6};
        boolean result = Task2.isNestable(nesting, mass);
        assertTrue(result);
    }

    @Test
    void isNestableTrue2(){
        int[] nesting = {3, 1};
        int[] mass = {4, 0};
        boolean result = Task2.isNestable(nesting, mass);
        assertTrue(result);
    }

    @Test
    void isNestableFalse1(){
        int[] nesting = {9, 9, 8};
        int[] mass = {8, 9};
        boolean result = Task2.isNestable(nesting, mass);
        assertFalse(result);
    }

    @Test
    void isNestableFalse2(){
        int[] nesting = {1, 2, 3, 4};
        int[] mass = {2, 3};
        boolean result = Task2.isNestable(nesting, mass);
        assertFalse(result);
    }

    @Test
    void isNestableVoid1(){
        int[] nesting = {};
        int[] mass = {2, 3};
        boolean result = Task2.isNestable(nesting, mass);
        assertTrue(result);
    }

    @Test
    void isNestableVoid2(){
        int[] nesting = {1, 2, 3, 4};
        int[] mass = {};
        boolean result = Task2.isNestable(nesting, mass);
        assertFalse(result);
    }

    @Test
    void isNestableVoid3(){
        int[] nesting = {};
        int[] mass = {};
        boolean result = Task2.isNestable(nesting, mass);
        assertFalse(result);
    }
}
