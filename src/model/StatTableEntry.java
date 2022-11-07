package model;

/**
 * Created by KungPeter on 2016-03-14.
 */
public class StatTableEntry {

    private String day;
    private int numOfPass;

    public StatTableEntry(String day, int numOfPass)
    {
        this.day = day;
        this.numOfPass = numOfPass;
    }

    public String getDay() {
        return day;
    }

    public int getNumOfPass() {
        return numOfPass;
    }
}

