package com.peter.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.time.LocalDate;

/**
 * Created by KungPeter on 2016-03-14.
 */
public class BoatStatisticalModel {

    private static ObservableList<Boat> boatList;
    private static ObservableList<ObservableList<BoatStatEntry>> masterBoatStatList = FXCollections.observableArrayList();
    private static boolean isOkToShowAllBoats = true;

    public BoatStatisticalModel(ObservableList<Boat> boats) {
        boatList = boats;

    }

    public void addNewBoatStatList(ObservableList<BoatStatEntry> boatStatList) {

        masterBoatStatList.add(boatStatList);
    }

    public void clearModel() {
        masterBoatStatList.clear();
    }


    public void getDataForTables(int selectionIndex,
                                 ObservableList<StatTableEntry> passPerDayList,
                                 ObservableList<StatTableEntry> passPerRoundList,
                                 ObservableList<ExtendedStatTableEntry> roundsWithMoreThanXList,
                                 LocalDate fromDate, LocalDate toDate, String round, int moreThanXPass,
                                 Label totalPassOnLabel, Label totalPassOnForRoundLabel, Label totalNoRoundsLabel) {
        // if no specific boat is selected, that means show statistics for all boats!
        if (selectionIndex == -1) {
            getTableDataForAllboats(passPerDayList, passPerRoundList, fromDate, toDate, round, totalPassOnLabel,
                    totalPassOnForRoundLabel);
            return;
        } else if (!masterBoatStatList.get(selectionIndex).isEmpty() && masterBoatStatList.get(selectionIndex) != null) {
            passPerDayList.clear();
            passPerRoundList.clear();
            roundsWithMoreThanXList.clear();

            LocalDate curentDate = fromDate;
            int numOfTravelsPerDay = 0;
            int numOfTravelsPerDayForSelectedRound = 0;
            int numOfTravelsPerRound = 0;

            // Get first roundNo
            String currentRound = masterBoatStatList.get(selectionIndex).get(0).getRound();


            while (curentDate.isBefore(toDate.plusDays(1))) {
                // Setting data to passPerDayTable for selected boat
                for (BoatStatEntry entry : masterBoatStatList.get(selectionIndex)) {
                    // Geting data for first Table
                    if (entry.getDay().equalsIgnoreCase(curentDate.toString())) {
                        numOfTravelsPerDay += entry.getPassenger_On();
                    }

                    // Getting data for second table
                    if (entry.getDay().equalsIgnoreCase(curentDate.toString()) && entry.getRound().trim().equalsIgnoreCase(round.trim())) {
                        numOfTravelsPerDayForSelectedRound += entry.getPassenger_On();
                    }

                    // Getting data for third table
                    if (entry.getDay().equalsIgnoreCase(curentDate.toString()) && entry.getRound().equalsIgnoreCase(currentRound)) {
                        numOfTravelsPerRound += entry.getPassenger_On();

                    } else if (entry.getDay().equalsIgnoreCase(curentDate.toString()) && !entry.getRound().equalsIgnoreCase(currentRound)) {
                        int sumUp = numOfTravelsPerRound;

                        if (sumUp >= moreThanXPass) {
                            roundsWithMoreThanXList.add(new ExtendedStatTableEntry(curentDate.toString(), sumUp, currentRound));
                        }
                        currentRound = entry.getRound();
                        numOfTravelsPerRound = entry.getPassenger_On();
                    } else if (!entry.getDay().equalsIgnoreCase(curentDate.toString())) {
                        numOfTravelsPerRound = entry.getPassenger_On();
                        currentRound = entry.getRound();
                    }


                }
                passPerDayList.add(new StatTableEntry(curentDate.toString(), numOfTravelsPerDay));
                passPerRoundList.add(new StatTableEntry(curentDate.toString(), numOfTravelsPerDayForSelectedRound));


                numOfTravelsPerDay = 0;
                numOfTravelsPerDayForSelectedRound = 0;
                curentDate = curentDate.plusDays(1);
            }

            if (numOfTravelsPerRound >= moreThanXPass)
                roundsWithMoreThanXList.add(new ExtendedStatTableEntry(curentDate.minusDays(1).toString(), numOfTravelsPerRound, currentRound));

            // Count total no of passengers
            int totalPass = 0;

            for (StatTableEntry entry : passPerDayList) {
                totalPass += entry.getNumOfPass();
            }

            // Count total passengers for selected round
            int totalPassForRoundX = 0;

            for (StatTableEntry entry : passPerRoundList) {
                totalPassForRoundX += entry.getNumOfPass();
            }

            // Count total no of rounds with more than X passengers
            int totalNoOfRoundsWithMoreThanXPass = roundsWithMoreThanXList.size();

            totalPassOnLabel.setText(Integer.toString(totalPass));
            totalPassOnForRoundLabel.setText(Integer.toString(totalPassForRoundX));
            totalNoRoundsLabel.setText(Integer.toString(totalNoOfRoundsWithMoreThanXPass));

        } else {
            System.out.println("Masterboatlist is empty");
        }
    }

    public void getDataForLabels(int seselectionIndex, Label fromTuvesVikLabel, Label toTuvesvikLabel,
                                 Label fromGullholmenHLabel, Label toGullholmenHLabel, Label fromGullholmsbadenLabel,
                                 Label toGullholmsBadenLabel, Label fromKaringonLabel, Label toKaringonLabel) {
        if (seselectionIndex == -1) {
            getLabelDataForAllBoats(fromTuvesVikLabel, toTuvesvikLabel, fromGullholmenHLabel, toGullholmenHLabel,
                    fromGullholmsbadenLabel, toGullholmsBadenLabel, fromKaringonLabel, toKaringonLabel);
            return;
        } else if (!masterBoatStatList.get(seselectionIndex).isEmpty() && masterBoatStatList.get(seselectionIndex) != null) {
            int fromTuvesvik = 0;
            int toTuvesvik = 0;
            int fromGullholmenH = 0;
            int toGullholmenH = 0;
            int fromGholmsBaden = 0;
            int toGholmsBaden = 0;
            int fromKaringon = 0;
            int toKaringon = 0;

            for (BoatStatEntry entry : masterBoatStatList.get(seselectionIndex)) {
                if (entry.getDeparture().contains("Tuvesvik")) {
                    fromTuvesvik += entry.getPassenger_On();
                    toTuvesvik += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Gullholmen H")) {
                    fromGullholmenH += entry.getPassenger_On();
                    toGullholmenH += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Gullholmsbaden")) {
                    fromGholmsBaden += entry.getPassenger_On();
                    toGholmsBaden += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Käringön")) {
                    fromKaringon += entry.getPassenger_On();
                    toKaringon += entry.getPassenger_Off();
                }
            }

            fromTuvesVikLabel.setText(Integer.toString(fromTuvesvik));
            toTuvesvikLabel.setText(Integer.toString(toTuvesvik));
            fromGullholmenHLabel.setText(Integer.toString(fromGullholmenH));
            toGullholmenHLabel.setText(Integer.toString(toGullholmenH));
            fromGullholmsbadenLabel.setText(Integer.toString(fromGholmsBaden));
            toGullholmsBadenLabel.setText(Integer.toString(toGholmsBaden));
            fromKaringonLabel.setText(Integer.toString(fromKaringon));
            toKaringonLabel.setText(Integer.toString(toKaringon));


        }
    }

    private void getTableDataForAllboats(ObservableList<StatTableEntry> passPerDayList, ObservableList<StatTableEntry> passPerRoundList,
                                         LocalDate fromDate, LocalDate toDate, String round, Label totalPassOnLabel,
                                         Label totalPassOnForRoundLabel) {
        // Validate that every boat har statisticsList

        System.out.println("Getting data for all boats");
        passPerDayList.clear();
        passPerRoundList.clear();
        LocalDate currentDate = fromDate;
        int passPerDay = 0;
        int passPerSelectedRound = 0;

        while (currentDate.isBefore(toDate.plusDays(1))) {
            for (ObservableList<BoatStatEntry> entryList : masterBoatStatList) {
                for (BoatStatEntry entry : entryList) {
                    // Get Data for first table
                    if (entry.getDay().equalsIgnoreCase(currentDate.toString()))
                        passPerDay += entry.getPassenger_On();

                    // get data for second table
                    if (entry.getDay().equalsIgnoreCase(currentDate.toString()) && entry.getRound().trim().equalsIgnoreCase(round.trim()))
                        passPerSelectedRound += entry.getPassenger_On();
                }

            }

            // Fill tables
            passPerDayList.add(new StatTableEntry(currentDate.toString(), passPerDay));
            passPerRoundList.add(new StatTableEntry(currentDate.toString(), passPerSelectedRound));

            passPerDay = 0;
            passPerSelectedRound = 0;
            currentDate = currentDate.plusDays(1);
        }

        // Count total No of pass
        int totalNoOfPass = 0;

        for (StatTableEntry entry : passPerDayList) {
            totalNoOfPass += entry.getNumOfPass();
        }

        // Couunt total no of pass for selected round
        int totalPassForRoundX = 0;

        for (StatTableEntry entry : passPerRoundList) {
            totalPassForRoundX += entry.getNumOfPass();
        }

        totalPassOnLabel.setText(Integer.toString(totalNoOfPass));
        totalPassOnForRoundLabel.setText(Integer.toString(totalPassForRoundX));


    }

    private void getLabelDataForAllBoats(Label fromTuvesVikLabel, Label toTuvesvikLabel,
                                         Label fromGullholmenHLabel, Label toGullholmenHLabel, Label fromGullholmsbadenLabel,
                                         Label toGullholmsBadenLabel, Label fromKaringonLabel, Label toKaringonLabel) {
        System.out.println("Setting lables for all boats");

        int fromTuvesvik = 0;
        int toTuvesvik = 0;
        int fromGullholmenH = 0;
        int toGullholmenH = 0;
        int fromGholmsBaden = 0;
        int toGholmsBaden = 0;
        int fromKaringon = 0;
        int toKaringon = 0;

        for (ObservableList<BoatStatEntry> entryList : masterBoatStatList) {
            for (BoatStatEntry entry : entryList) {
                if (entry.getDeparture().contains("Tuvesvik")) {
                    fromTuvesvik += entry.getPassenger_On();
                    toTuvesvik += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Gullholmen H")) {
                    fromGullholmenH += entry.getPassenger_On();
                    toGullholmenH += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Gullholmsbaden")) {
                    fromGholmsBaden += entry.getPassenger_On();
                    toGholmsBaden += entry.getPassenger_Off();
                } else if (entry.getDeparture().contains("Käringön")) {
                    fromKaringon += entry.getPassenger_On();
                    toKaringon += entry.getPassenger_Off();
                }
            }
        }

        fromTuvesVikLabel.setText(Integer.toString(fromTuvesvik));
        toTuvesvikLabel.setText(Integer.toString(toTuvesvik));
        fromGullholmenHLabel.setText(Integer.toString(fromGullholmenH));
        toGullholmenHLabel.setText(Integer.toString(toGullholmenH));
        fromGullholmsbadenLabel.setText(Integer.toString(fromGholmsBaden));
        toGullholmsBadenLabel.setText(Integer.toString(toGholmsBaden));
        fromKaringonLabel.setText(Integer.toString(fromKaringon));
        toKaringonLabel.setText(Integer.toString(toKaringon));
    }


}
