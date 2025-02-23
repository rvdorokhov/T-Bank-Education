package org.example;

import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Main {
    public static void main(String[] args) {
    }
}

class GlobalValues {
    public static final int INF = 999999999;
}

class Task1{
    public static int minutesToSeconds(String string) {
        int minutes; int seconds;
        String[] split = string.split(":");
        try {
            minutes = Integer.parseInt(split[0]);
            seconds = Integer.parseInt(split[1]);
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("Incorrect input. Correct input should be look like mm:ss");
            return -1;
        } catch(NumberFormatException e) {
            System.out.println("Incorrect input. Waited for numbers, got something else");
            return -1;
        }

        if(seconds >= 60 || seconds < 0 || minutes < 0){
            System.out.println("The seconds input is incorrect");
            return -1;
        }

        return minutes * 60 + seconds;
    }
}

class Task2{
    public static boolean isNestable(int[] nesting, int[] mass) {
        int max_mass, min_mass, max_nesting, min_nesting;
        max_mass = max_nesting = -GlobalValues.INF;
        min_mass = min_nesting = GlobalValues.INF;

        for(int i = 0; i < mass.length; i++){
            max_mass = (max_mass < mass[i]) ? mass[i] : max_mass;
            min_mass = (min_mass > mass[i]) ? mass[i] : min_mass;
        }

        for(int j = 0; j < nesting.length; j++){
            max_nesting = (max_nesting < nesting[j]) ? nesting[j] : max_nesting;
            min_nesting = (min_nesting > nesting[j]) ? nesting[j] : min_nesting;
        }

        return (min_nesting > min_mass)&&(max_nesting < max_mass);
    }
}

class Task3{
    public static char[] fixString(char[] fixing_string){
        char[] result = Arrays.copyOf(fixing_string, fixing_string.length); // копия строки, чтобы не изменять исходную строку
        char memory;
        for(int i = 0; i + 1 < result.length; i+=2){
            memory = result[i];
            result[i] = result[i + 1];
            result[i + 1] = memory;
        }

        return result;
    }
}

class Task4{
    private static boolean isPalindrome(String checking_number){
        boolean result = true;
        final int length = checking_number.length();
        for(int i = 0; (i < length - i - 1)&&result; i++){
            result = checking_number.charAt(i) == checking_number.charAt(length - i - 1);
        }

        return result;
    }

    public static boolean isPalindromeDescendant(int checking_number){
        String checking_char = Integer.toString(checking_number);
        String memory;
        boolean result = false;

        while((checking_char.length() > 1)&&(!result)){
            result = isPalindrome(checking_char);

            memory = "";
            for(int i = 0; i + 1 < checking_char.length(); i+=2){
                int num1 = Character.getNumericValue(checking_char.charAt(i));
                int num2 = Character.getNumericValue(checking_char.charAt(i + 1));
                String inserting_num = Integer.toString(num1 + num2);

                memory = memory + inserting_num;
            }

            checking_char = memory;
        }

        return result;
    }
}

class Task5{
    final static Set<Integer> ban_numbers = Set.of(1111, 2222, 3333, 4444, 5555, 6666, 7777, 8888, 9999);
    static int counter = 0;
    static int memory;

    public static int countK(int number){
        if(ban_numbers.contains(number)||(number <= 1000)||(number >= 10000)){
            System.out.println("Incorrect input");
            counter = 0;
            return -1;
        }

        counter += 1;

        char[] str_input = Integer.toString(number).toCharArray();
        Arrays.sort(str_input);
        String str_inc = ""; String str_dec = "";
        for(int i = 0; i < str_input.length; i++){
            str_inc += str_input[i];
            str_dec += str_input[str_input.length - i - 1];
        }

        int num_inc = Integer.parseInt(str_inc);
        int num_dec = Integer.parseInt(str_dec);
        int num_result = num_dec - num_inc;

        if (num_result == 6174) {
            memory = counter;
            counter = 0;
            return memory;
        }

        return countK(num_result);
    }
}























