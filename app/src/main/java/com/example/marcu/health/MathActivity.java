package com.example.marcu.health;

import java.util.ArrayList;
import java.util.Date;

public class MathActivity {

    public static long getDaysBetween(Date one, Date two) {
        long difference = 0;
        try {
            difference = ((one.getTime() - two.getTime()) / 86400000);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return Math.abs(difference);
    }

    public int getAcuteWorkload(ArrayList<Integer> al) {
        int acuteWorkload = 0;
        int workLoadPastDays = 0;
        if (al.size() >= 7) {
            for (int i = al.size() - 1; i >= al.size() - 7; i--) {
                workLoadPastDays = workLoadPastDays + al.get(i);
            }
            acuteWorkload = workLoadPastDays / 7;
        }
        System.out.println("Acute Workload: " + acuteWorkload);
        return acuteWorkload;
    }

    public int getChronicWorkload(ArrayList<Integer> al) {
        int chronicWorkload = 0;
        int workLoadPastDays = 0;
        if (al.size() >= 28) {
            for (int i = 0; i <= 27; i++) {
                workLoadPastDays = workLoadPastDays + al.get(i);
            }
            chronicWorkload = workLoadPastDays / 28;
        }
        System.out.println("Chronic Workload: " + chronicWorkload);
        return chronicWorkload;
    }

    public double getACWR(int acuteWorkload, int chronicWorkload) {
        double ACWR = (double) acuteWorkload / (double) chronicWorkload;
        System.out.println("ACWR: " + ACWR);
        return ACWR;
    }

    public static int getRandomHR() {
        return (int) (Math.random() * ((280 - 120) + 1)) + 120;
    }

    public static int getRandomMinutes() {
        return (int) (Math.random() * ((100 - 30) + 1)) + 30;
    }

}
