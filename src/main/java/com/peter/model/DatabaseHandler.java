package com.peter.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by KungPeter on 2016-02-23.
 */
public class DatabaseHandler {


    private static Connection conn;
    private static Statement stmt;
    private static PreparedStatement prepStmt;
    private static ResultSet resultSet;
    private static String url = "jdbc:mysql://db01.karingotrafiken.se:3306/timetable?autoReconnect=true&useSSL=false";
    private static String userName;
    private static String password;
    private static LocalDate changeToSummerTableDate;
    private static LocalDate changeToWinterTableDate;

    public static void validateUserAndPass(String user, String pass) throws SQLException {
        conn = DriverManager.getConnection(url, user, pass);
        userName = user;
        password = pass;

        if (conn != null)
            conn.close();

    }

    public static void setSwitchTimeTableDates(ObservableList<LocalDate> switchTimeTableDates){

        changeToSummerTableDate = switchTimeTableDates.get(0);
        changeToWinterTableDate = switchTimeTableDates.get(1);
    }

    public static ArrayList<String> getFullTimeTable(String TableName) {
        String sql = "SELECT * FROM " + TableName;

        ArrayList<String> timetable = new ArrayList<>();

        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                timetable.add(resultSet.getString(1));
                System.out.println(resultSet.getString(1));
            }

            return timetable;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;

        } finally {
            closeConnection();
        }
    }


    public static void sendNewEntryToDatabase(Boat boat, BoatStatEntry newEntry, ObservableList<BoatStatEntry> localList) {
        String sql = "INSERT INTO " + boat.getStatTableName() + " (Dag, Tur, Avgang, Pass_pa, Pass_av, Tillagd) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = DriverManager.getConnection(url, userName, password);
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, newEntry.getDay());
            prepStmt.setString(2, newEntry.getRound());
            prepStmt.setString(3, newEntry.getDeparture());
            prepStmt.setInt(4, newEntry.getPassenger_On());
            prepStmt.setInt(5, newEntry.getPassenger_Off());
            prepStmt.setString(6, newEntry.getAdded());

            prepStmt.executeUpdate();
            localList.add(newEntry);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    public static void removeEntryFromDatabase(Boat boat, BoatStatEntry entry) {
        String sql = "DELETE FROM " + boat.getStatTableName() + " WHERE Dag = ? AND Tur = ?";

        try {
            conn = DriverManager.getConnection(url, userName, password);
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, entry.getDay());
            prepStmt.setString(2, entry.getRound());

            prepStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    public static void getEntriesForSelectedDate(Boat boat, LocalDate date, ObservableList<BoatStatEntry> localList) {

        localList.clear();

        String sql = "SELECT * FROM " + boat.getStatTableName() + " WHERE Dag = ?";

        try {
            conn = DriverManager.getConnection(url, userName, password);
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, date.toString());
            resultSet = prepStmt.executeQuery();

            while (resultSet.next()) {
                BoatStatEntry newEntry = new BoatStatEntry(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
                        resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6));
                localList.add(newEntry);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static ObservableList<String> getUpdatedDepartureList(Boat boat, LocalDate date, CheckBox holidayBox) {

        ObservableList<String> newList = FXCollections.observableArrayList();

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        ArrayList<String> tempList = null;

        if (dateIsWithinWinterPeriod(date)) {
            // its winter table

            if (dateIsHoliday(dayOfWeek) || holidayBox.isSelected())
                tempList = boat.getWeekendTimeTableWinter();

            else
                tempList = boat.getMonFriTimeTableWinter();

        } else {
            // Else its summer table

            if (dateIsHoliday(dayOfWeek) || holidayBox.isSelected())
                tempList = boat.getWeekendTimeTableSummer();

            else
                tempList = boat.getMonFriTimeTableSummer();
        }

        String sql = " SELECT Avgang FROM " + boat.getStatTableName() + " WHERE Dag = '" + date.toString() + "'";

        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).trim().equals(resultSet.getString(1).trim())) {
                        tempList.remove(i);
                        break;
                    }
                }
            }

            for (String departure : tempList)
                newList.add(departure);

            return newList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {

            closeConnection();
        }


    }

    private static boolean dateIsHoliday(DayOfWeek dayOfWeek) {
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }


    public static int calculatePassLeavingEndStation(Boat boat, LocalDate date, String round) {

        String sql = "SELECT Pass_pa, Pass_av FROM " + boat.getStatTableName()
                + " WHERE Dag = '" + date.toString() + "' AND Tur ='" + round + "'";

        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            int result = 0;

            while (resultSet.next()) {
                result += resultSet.getInt(1) - resultSet.getInt(2);
            }

            return result;
        } catch (SQLException e) {
            System.out.println("Failed to calculate num leaving pass");
            e.printStackTrace();
            return 0;
        } finally {

            closeConnection();
        }


    }

    public static ObservableList<BoatStatEntry> getDataForBoatAndPeriod(Boat boat, LocalDate startDate, LocalDate endDate) {

        ObservableList<BoatStatEntry> dataList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM " + boat.getStatTableName() + " WHERE Dag >= ? AND Dag <= ?";

        try {

            conn = DriverManager.getConnection(url, userName, password);
            prepStmt = conn.prepareStatement(sql);
            prepStmt.setString(1, startDate.toString());
            prepStmt.setString(2, endDate.toString());
            resultSet = prepStmt.executeQuery();

            while (resultSet.next()) {
                BoatStatEntry newReturnedEntry = new BoatStatEntry(resultSet.getString(1), resultSet.getString(2),
                        resultSet.getString(3), resultSet.getInt(4), resultSet.getInt(5), resultSet.getString(6));
                dataList.add(newReturnedEntry);
            }

            return dataList;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Kunde inte hämta info");
            return null;
        } finally {
            closeConnection();
        }
    }

    public static void getHolidays(ObservableList<LocalDate> holidayList) {

        String sql = "SELECT * FROM Helgdagar";

        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                holidayList.add(LocalDate.parse(resultSet.getString(1)));
            }

            for (LocalDate date : holidayList)
                System.out.println(date);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }

    }

    public static ObservableList<LocalDate> getSwitchTimeTableDates() {

        String sql = "SELECT * FROM SwitchTimeTableDates";
        ObservableList<LocalDate> switchTimeTableDatesList = FXCollections.observableArrayList();

        try {
            conn = DriverManager.getConnection(url, userName, password);
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                switchTimeTableDatesList.add(LocalDate.parse(resultSet.getString(1)));
            }

            return switchTimeTableDatesList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            closeConnection();
        }
    }

    public static void addHoliday(LocalDate date) {
        String sql = "INSERT INTO Helgdagar (Helgdagar) VALUES (?)";

        try {
            updateHolidayList(date, sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public static void removeHoliday(LocalDate date) {
        String sql = "DELETE FROM Helgdagar WHERE Helgdagar = ?";

        try {
            updateHolidayList(date, sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            closeConnection();
        }
    }

    private static void updateHolidayList(LocalDate date, String sql) throws SQLException {
        conn = DriverManager.getConnection(url, userName, password);
        prepStmt = conn.prepareStatement(sql);
        prepStmt.setString(1, date.toString());
        prepStmt.executeUpdate();
    }

    private static boolean dateIsWithinWinterPeriod(LocalDate date) {
        return date.getDayOfYear() < changeToSummerTableDate.getDayOfYear() || date.getDayOfYear() >= changeToWinterTableDate.getDayOfYear();
    }


    private static void closeConnection() {
        try {
            if (resultSet != null)
                resultSet.close();

            if (stmt != null)
                stmt.close();

            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
