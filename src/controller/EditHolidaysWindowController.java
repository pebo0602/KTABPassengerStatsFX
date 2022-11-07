package controller;

import helperClasses.Utilities;
import interfaces.BoatStatProgramViewController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import mainApplication.MainApplication;
import model.Boat;
import model.DatabaseHandler;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by KungPeter on 2016-03-28.
 */
public class EditHolidaysWindowController implements BoatStatProgramViewController {
    @FXML
    private BorderPane rootBorderPane;

    @FXML
    private ListView<LocalDate> holidaysListView;

   @FXML
   private TextField inputNewHolidayTextFiled;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;



    private static MainApplication mainApplication;
    private static MainWindowController owner;
    private static ObservableList<LocalDate> holidayList;
    private static LocalDate addedDate;


    public void setHolidays(ObservableList<LocalDate> holidays)
    {
        holidayList = holidays;
        holidaysListView.setItems(holidayList);
    }

    public void setOwner(MainWindowController ownerController)
    {
        owner = ownerController;
    }


    @Override
    public void setMainApplication(MainApplication mainApp)
    {
        mainApplication = mainApp;
    }

    @Override
    public void setBoats(ObservableList<Boat> boats) {

    }

    @Override
    public void initializeStart()
    {
        holidaysListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void handleAddButtonClicked()
    {
        if (isValidInput())
        {
            Comparator<LocalDate> comparator = new Comparator<LocalDate>()
            {
                @Override
                public int compare(LocalDate date1, LocalDate date2) {

                    if (date1.isBefore(date2))
                        return -1;

                    else if (date1.isAfter(date2))
                        return 1;

                    return 0;
                }
            };

            if (addedDate != null)
            {
                holidayList.add(addedDate);
                Collections.sort(holidayList, comparator);
                DatabaseHandler.addHoliday(addedDate);
            }
        }


    }

    @FXML
    private void handleDeleteButtonClicked()
    {
        LocalDate date = holidaysListView.getSelectionModel().getSelectedItem();

        if (date == null)
        {
            Utilities.showAlert("Ojdå!", "Du har inte valt något datum", Alert.AlertType.ERROR);
            return;
        }

        holidayList.remove(date);
        DatabaseHandler.removeHoliday(date);

    }

    @FXML
    private void handleCancelButtonClicked()
    {
        inputNewHolidayTextFiled.clear();
        rootBorderPane.getScene().getWindow().hide();
    }


    private boolean isValidInput()
    {
        try
        {
            addedDate = LocalDate.parse(inputNewHolidayTextFiled.getText());
            return true;

        } catch (DateTimeParseException e)
        {
            Utilities.showAlert("Ojdå!", "Ogiltigt datum, kontollera inmatningen och försök igen", Alert.AlertType.ERROR);
            e.printStackTrace();
            return  false;
        }
        catch (DateTimeException ex)
        {
            Utilities.showAlert("Ojdå!", "Ogiltigt datum, kontollera inmatningen och försök igen", Alert.AlertType.ERROR);
            ex.printStackTrace();
            return false;
        }
    }


}
