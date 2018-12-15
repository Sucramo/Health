package com.example.marcu.health;

import java.util.ArrayList;

public class MathTest {

    private static ArrayList<Integer> al = new ArrayList<>();


    public static void main(String[] args) {
        MathActivity mathActivity = new MathActivity();

        for (int day = 1; day <= 50; day++) {
            //double ACWR = mathActivity.getACWR(getRandomMinutes(), getRandomHR(), al);
        }
    }

    private static int getRandomMinutes(){

        return (int) (Math.random() * ((120 - 10) + 1)) + 10;
    }

    private static int getRandomHR() {

        return (int) (Math.random() * ((200 - 60) + 1)) + 60;
    }


}
