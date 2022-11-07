package com.peter.model;

import java.util.ArrayList;

/**
 * Created by KungPeter on 2016-02-23.
 */
public class Boat {

    private ArrayList<String> monFriTimeTableWinter = new ArrayList<>();
    private ArrayList<String> weekendTimeTableWinter = new ArrayList<>();
    private ArrayList<String> monFriTimeTableSummer = new ArrayList<>();
    private ArrayList<String> weekendTimeTableSummer = new ArrayList<>();
    private String name;
    private String monFriTableNameWinter;
    private String weekendTableNameWinter;
    private String monFriTableNameSummer;
    private String weekendTableNameSummer;
    private String statTableName;





    public Boat(String name, String monFriTableNameWinter, String weekendTableNameWinter, String monFriTableNameSummer,
                String weekendTableNameSummer, String statTableName)
    {
        this.name = name;
        this.monFriTableNameWinter = monFriTableNameWinter;
        this.weekendTableNameWinter = weekendTableNameWinter;
        this.monFriTableNameSummer = monFriTableNameSummer;
        this.weekendTableNameSummer = weekendTableNameSummer;
        this.statTableName = statTableName;
    }

    public void setWeekendTimeTableWinter(ArrayList<String> weekendTimeTableWinter) {
        this.weekendTimeTableWinter = weekendTimeTableWinter;
    }

    public void setMonFriTimeTableWinter(ArrayList<String> monFriTimeTableWinter) {
        this.monFriTimeTableWinter = monFriTimeTableWinter;
    }

    public void setWeekendTimeTableSummer(ArrayList<String> weekendTimeTableSummer) {
        this.weekendTimeTableSummer = weekendTimeTableSummer;
    }

    public void setMonFriTimeTableSummer(ArrayList<String> monFriTimeTableSummer) {
        this.monFriTimeTableSummer = monFriTimeTableSummer;
    }

    public ArrayList<String> getMonFriTimeTableSummer() {
        ArrayList<String> monFriTTSummerCopy = (ArrayList<String>) monFriTimeTableSummer.clone();
        return monFriTTSummerCopy;
    }

    public ArrayList<String> getWeekendTimeTableSummer() {
        ArrayList<String> weekendTTSummerCopy = (ArrayList<String>) weekendTimeTableSummer.clone();
        return weekendTTSummerCopy;
    }


    public ArrayList<String> getWeekendTimeTableWinter() {
        ArrayList<String> weekEndTTCopy = (ArrayList<String>) weekendTimeTableWinter.clone();
        return weekEndTTCopy;
    }

    public ArrayList<String> getMonFriTimeTableWinter() {
        ArrayList<String> monFriTTCopy = (ArrayList<String>) monFriTimeTableWinter.clone();
        return monFriTTCopy;
    }

    public String getMonFriTableNameWinter() {
        return monFriTableNameWinter;
    }

    public String getName() {
        return name;
    }

    public String getWeekendTableNameWinter() {
        return weekendTableNameWinter;
    }

    public String getStatTableName() {
        return statTableName;
    }

    public String getMonFriTableNameSummer() {
        return monFriTableNameSummer;
    }

    public String getWeekendTableNameSummer() {
        return weekendTableNameSummer;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
