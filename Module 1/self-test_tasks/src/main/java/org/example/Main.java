package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String test = scanner.nextLine();
        int seconds = Task1.minutesToSeconds(test);
        System.out.println(seconds);
        scanner.close();
    }
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