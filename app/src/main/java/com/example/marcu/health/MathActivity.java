package com.example.marcu.health;

import java.util.ArrayList;
import java.util.Date;

public class MathActivity {

    private static int workLoadPastDays;
    private static int acuteWorkload;
    private static double ACWR;
    private static Date newDate;
    private static int daysBetween;


    public double getACWR(int tempMinutes, int tempHR, ArrayList<Integer> al) {

        //getting the days between today's training and last training.
        if (al.size() == 0) {
            newDate = getNewDate();
            System.out.println("first date: " + newDate);
        } else {
            Date oldDate = newDate;
            newDate = getNewDate();

            System.out.println("old date: " + oldDate);
            System.out.println("new date: " + newDate);

            daysBetween = (int) getDaysBetween(oldDate, newDate);
            System.out.println("days between old and new date: " + daysBetween);
        }


        int tempRPE = tempHR / 10;
        int workLoadCurrentDay = tempRPE * tempMinutes;

        //if it's the first time doing a workout with the app
        if (al.size() == 0) {
            al.add(workLoadCurrentDay);
            System.out.println(al);
        }

        //if the workout is on the same day as the previous one
        else if (al.size() > 0 && daysBetween == 0) {
            int AUCurrentDay = al.get(al.size() - 1) + workLoadCurrentDay;
            al.remove(al.get(al.size() - 1));
            System.out.println("Adding " + workLoadCurrentDay + "AU to current day");
            al.add(AUCurrentDay);
            System.out.println(al);
        }

        //if the workout is another day than the previous workout
        else if (al.size() > 0 && daysBetween > 0) {
            for (int i = 1; i <= daysBetween - 1; i++) {
                al.add(0);
            }

            //checking if if the arraylist is exceeding 28 of size. If it is; remove index 0.
            if (al.size() >= 28) {
                al.add(workLoadCurrentDay);
                al.remove(0);

            } else {
                al.add(workLoadCurrentDay);

            }
            System.out.println(al);

        }

        //calculating acute workload
        if (al.size() >= 7) {
            for (int i = al.size() - 1; i >= al.size() - 7; i--) {
                workLoadPastDays = workLoadPastDays + al.get(i);
            }
            acuteWorkload = workLoadPastDays / 7;
            System.out.println("Acute Workload: " + acuteWorkload);
        }
        workLoadPastDays = 0;

        //calculating chronic workload and ACWR
        if (al.size() >= 28) {
            for (int i = 0; i <= 27; i++) {
                workLoadPastDays = workLoadPastDays + al.get(i);
            }
            int chronicWorkload = workLoadPastDays / 28;
            System.out.println("Chronic Workload: " + chronicWorkload);
            ACWR = (double) acuteWorkload / (double) chronicWorkload;
            System.out.println("ACWR: " + ACWR);
        }

        workLoadPastDays = 0;
        System.out.println();

        return ACWR;
    }

    public long getDaysBetween(Date one, Date two) {
        long difference = ((one.getTime() - two.getTime()) / 86400000);
        return Math.abs(difference);
    }

    private static Date getNewDate() {

        return new Date();
    }


}
