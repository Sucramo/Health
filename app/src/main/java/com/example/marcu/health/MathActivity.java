package com.example.marcu.health;

import java.util.ArrayList;

public class MathActivity {


    private static int day;
    private static int tempHR;
    private static int tempRPE;
    private static int tempMinutes;
    private static int workLoadCurrentDay;
    private static int workLoadPastDays;
    private static int acuteWorkload;
    private static int chronicWorkload;
    private static double ACWR;

    public static void main(String[] args) {

        ArrayList<Integer> al = new ArrayList<>();

        for (day = 1; day <= 50; day++) {
            tempHR = (int) (Math.random() * ((200 - 60) + 1)) + 60;
            tempRPE = tempHR / 10;
            tempMinutes = (int) (Math.random() * ((120 - 10) + 1)) + 10;
            workLoadCurrentDay = tempRPE * tempMinutes;
            if (al.size() >= 28) {
                al.add(workLoadCurrentDay);
                al.remove(0);

            } else {
                al.add(workLoadCurrentDay);

            }
            System.out.println(al);


            if (al.size() >= 7) {
                for (int i = al.size() - 1; i >= al.size() - 7; i--) {
                    workLoadPastDays = workLoadPastDays + al.get(i);
                }
                acuteWorkload = workLoadPastDays / 7;
                System.out.println("Acute Workload: " + acuteWorkload);
            }
            workLoadPastDays = 0;

            if (al.size() >= 28) {
                for (int i = 0; i <= 27; i++) {
                    workLoadPastDays = workLoadPastDays + al.get(i);
                }
                chronicWorkload = workLoadPastDays / 28;
                System.out.println("Chronic Workload: " + chronicWorkload);
                ACWR = (double) acuteWorkload / (double) chronicWorkload;
                System.out.println("ACWR: " + ACWR);
            }

            workLoadPastDays = 0;
            System.out.println();

        }


    }
}
