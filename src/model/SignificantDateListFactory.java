package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Created by KungPeter on 2016-03-08.
 */
public class SignificantDateListFactory {

    private ObservableList<LocalDate> holidayList = FXCollections.observableArrayList();

    public SignificantDateListFactory()
    {
        DatabaseHandler.getHolidays(holidayList);
    }

    public ObservableList<LocalDate> getHolidays()
    {
        return this.holidayList;
    }


    public ObservableList<LocalDate> getSwitchTimeTableDates() {
        return DatabaseHandler.getSwitchTimeTableDates();
    }
}
