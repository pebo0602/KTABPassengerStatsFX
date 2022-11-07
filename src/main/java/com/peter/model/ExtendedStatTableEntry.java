package com.peter.model;

/**
 * Created by KungPeter on 2016-03-15.
 */
public class ExtendedStatTableEntry extends StatTableEntry {

    private String round;

    public ExtendedStatTableEntry(String day, int numOfPass, String round)
    {
        super(day, numOfPass);
        this.round = round;
    }

    public String getRound() {
        return round;
    }
}
