package com.example.marcu.health;

import java.util.Scanner;

public class MathActivity {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int acuteWorkload = 0;
        int workLoadPastDays = 0;
        int workLoadCurrentDay;
        int tempRPE;
        int tempMinutes;
        int day;
        int week;
        int workLoadPastWeeks = 0;
        int chronicWorkload;
        double ACWR;

        for(week = 1; week <= 4; week++) {
            for (day = 1; day <= 7; day++) {
                System.out.println("Set RPE for day " + day + " week " + week + ":");
                tempRPE = input.nextInt();
                while (tempRPE < 0 || tempRPE > 10) {
                    System.out.println("RPE must be between 0 and 10. Enter RPE again:");
                    tempRPE = input.nextInt();
                }

                System.out.println("Set duration in minutes for day " + day + " week " + week + ":");
                tempMinutes = input.nextInt();

                workLoadCurrentDay = tempRPE * tempMinutes;
                workLoadPastDays = workLoadPastDays + workLoadCurrentDay;

                System.out.println("Workload day " + day + " week " + week + ": " + workLoadCurrentDay);
            }
            acuteWorkload = workLoadPastDays/7;

            System.out.println("Your acute work load is: " + acuteWorkload);

            workLoadPastWeeks = workLoadPastWeeks + acuteWorkload;
        }

        chronicWorkload = workLoadPastWeeks/4;

        System.out.println("Your chronic work load is: " + chronicWorkload);

        ACWR = acuteWorkload/(double)chronicWorkload;

        System.out.println("Your Acute:Chronic Workload Ratio is: " + ACWR);




       /*
        ACWR acwr = null;
        acwr.setAcuteWorkload(acuteWorkload);
        System.out.println(acwr.getAcuteWorkload());
        */

    }

}
