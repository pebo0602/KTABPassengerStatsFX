package com.peter.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by KungPeter on 2016-02-23.
 */
public class BoatStatEntry {

    private String day;
    private String round;
    private String departure;
    private int passenger_On;
    private int passenger_Off;
    private String added;


    public BoatStatEntry(String day, String round, String depart, int pass_on, int pass_off, String added)
    {
        this.day = day.toString();
        this.round = round;
        this.departure = depart;
        this.passenger_On = pass_on;
        this.passenger_Off = pass_off;
        this.added = added;
    }

    public String getDay() {
        return day;
    }

    public String getDeparture() {
        return departure;
    }

    public int getPassenger_Off() {
        return passenger_Off;
    }

    public int getPassenger_On() {
        return passenger_On;
    }

    public String getRound() {
        return round;
    }

    public String getAdded() {
        return added;
    }
}
