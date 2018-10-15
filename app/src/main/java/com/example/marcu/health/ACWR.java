package com.example.marcu.health;

public class ACWR {

    private int RPE;
    private int minutes;
    private int acuteWorkload;
    private int chronicWorkload;
    private int acuteChronicWorkload;

    public ACWR(int RPE, int minutes, int acuteWorkload, int chronicWorkload, int acuteChronicWorkload) {
        this.RPE = RPE;
        this.minutes = minutes;
        this.acuteWorkload = acuteWorkload;
        this.chronicWorkload = chronicWorkload;
        this.acuteChronicWorkload = acuteChronicWorkload;
    }

    public int getRPE() {
        return RPE;
    }

    public void setRPE(int RPE) {
        this.RPE = RPE;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getAcuteWorkload() {
        return acuteWorkload;
    }

    public void setAcuteWorkload(int acuteWorkload) {
        this.acuteWorkload = acuteWorkload;
    }

    public int getChronicWorkload() {
        return chronicWorkload;
    }

    public void setChronicWorkload(int chronicWorkload) {
        this.chronicWorkload = chronicWorkload;
    }

    public int getAcuteChronicWorkload() {
        return acuteChronicWorkload;
    }

    public void setAcuteChronicWorkload(int acuteChronicWorkload) {
        this.acuteChronicWorkload = acuteChronicWorkload;
    }
}