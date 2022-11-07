package com.peter.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by KungPeter on 2016-02-26.
 */
public class BoatListFactory {

    private ObservableList<Boat> boats = FXCollections.observableArrayList();


    public BoatListFactory()
    {
        createBoats();
    }

    private void createBoats()
    {
        Boat stromStierna = new Boat("Strömstierna", "TimeTableStromstiernaMonFriWinter",
                "TimeTableStromstiernaWeekendWinter", "TimeTableStromstiernaMonFriSummer",
                "TimeTableStromstiernaWeekendSummer","Stromstierna");

        stromStierna.setMonFriTimeTableWinter(DatabaseHandler.getFullTimeTable(stromStierna.getMonFriTableNameWinter()));
        stromStierna.setWeekendTimeTableWinter(DatabaseHandler.getFullTimeTable(stromStierna.getWeekendTableNameWinter()));
        stromStierna.setMonFriTimeTableSummer(DatabaseHandler.getFullTimeTable(stromStierna.getMonFriTableNameSummer()));
        stromStierna.setWeekendTimeTableSummer(DatabaseHandler.getFullTimeTable(stromStierna.getWeekendTableNameSummer()));

        Boat stromCrona = new Boat("Strömcrona", "TimeTableStromcronaMonFriWinter",
                "TimeTableStromcronaWeekendWinter", "TimeTableStromcronaMonFriSummer",
                "TimeTableStromcronaWeekendSummer", "Stromcrona");

        stromCrona.setMonFriTimeTableWinter(DatabaseHandler.getFullTimeTable(stromCrona.getMonFriTableNameWinter()));
        stromCrona.setWeekendTimeTableWinter(DatabaseHandler.getFullTimeTable(stromCrona.getWeekendTableNameWinter()));
        stromCrona.setMonFriTimeTableSummer(DatabaseHandler.getFullTimeTable(stromCrona.getMonFriTableNameSummer()));
        stromCrona.setWeekendTimeTableSummer(DatabaseHandler.getFullTimeTable(stromCrona.getWeekendTableNameSummer()));

        Boat tullan = new Boat("Tullan", "TimeTableTullanMonFriWinter",
                "TimeTableTullanWeekendWinter", "TimeTableTullanMonFriSummer",
                "TimeTableTullanWeekendSummer", "Tullan");

        tullan.setMonFriTimeTableWinter(DatabaseHandler.getFullTimeTable(tullan.getMonFriTableNameWinter()));
        tullan.setWeekendTimeTableWinter(DatabaseHandler.getFullTimeTable(tullan.getWeekendTableNameWinter()));
        tullan.setMonFriTimeTableSummer(DatabaseHandler.getFullTimeTable(tullan.getMonFriTableNameSummer()));
        tullan.setWeekendTimeTableSummer(DatabaseHandler.getFullTimeTable(tullan.getWeekendTableNameSummer()));

        boats.add(stromStierna);
        boats.add(stromCrona);
        boats.add(tullan);

    }


    public void addBoat(Boat boat)
    {
        boats.add(boat);
    }

    public ObservableList<Boat> getBoats()
    {
        return this.boats;
    }


}