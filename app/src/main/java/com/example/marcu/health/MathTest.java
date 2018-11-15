package com.example.marcu.health;

public class MathTest {


    public static void main(String[] args) {
        MathActivity mathActivity = new MathActivity();

        for (int day = 1; day <= 50; day++) {
            double ACWR = mathActivity.getACWR(getRandomMinutes(), getRandomHR());
        }
    }

    private static int getRandomMinutes(){

        return (int) (Math.random() * ((120 - 10) + 1)) + 10;
    }

    private static int getRandomHR() {

        return (int) (Math.random() * ((200 - 60) + 1)) + 60;
    }


}
