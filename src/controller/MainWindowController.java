package controller;

import helperClasses.Utilities;
import interfaces.BoatStatProgramViewController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mainApplication.MainApplication;
import model.Boat;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by KungPeter on 2016-03-08.
 */
public class MainWindowController implements BoatStatProgramViewController {

    private static MainApplication mainApplication;
    private static ObservableList<Boat> boatList;
    private static ObservableList<LocalDate> holidayList;



    @FXML
    private Button showInputViewButton;

    @FXML
    private Button showStatViewButton;

    @FXML
    private MenuItem setHolidaysMenuItem;


    @Override
    public void setMainApplication(MainApplication mainApp)
    {
        mainApplication = mainApp;
    }

    @Override
    public void setBoats(ObservableList<Boat> boats) {
        boatList = boats;
    }

    public void setHolidays(ObservableList<LocalDate> holidays)
    {
        holidayList = holidays;
    }

    @Override
    public void initializeStart()
    {
        showInputViewButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/rescources/Mata_in.png"))));
        showStatViewButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/rescources/Statistics.png"))));
    }

    @FXML
    private void handleShowInputViewButtonPressed()
    {
        if(mainApplication != null)
            mainApplication.switchView("InputWindow");
    }

    @FXML
    private void handleshowStatViewButtonPressed()
    {
        if(mainApplication != null)
        mainApplication.switchView("StatsWindow");
    }

    @FXML
    private void handleExitMenuItemClicked()
    {
        Platform.exit();
    }


    @FXML
    private void handleSetHolidaysMenuItemClicked()
    {
        FXMLLoader editHolidayWindowLoader = new FXMLLoader(getClass().getResource("/view/editHolidaysWindow.fxml"));
        BorderPane editHolidayWindow = null;

        try
        {
             editHolidayWindow =  editHolidayWindowLoader.load();
        }

        catch (IOException e)
        {
            Utilities.showAlert("Oj då! Ett fel intrffade", "Kunde inte ladda fönstret för att mata in helgdagar", Alert.AlertType.ERROR);
            e.printStackTrace();
        }

        EditHolidaysWindowController editHolidaysWindowController = editHolidayWindowLoader.getController();
        editHolidaysWindowController.setMainApplication(mainApplication);
        editHolidaysWindowController.setOwner(this);
        editHolidaysWindowController.setHolidays(holidayList);
        editHolidaysWindowController.initializeStart();

        Stage stage = new Stage();
        stage.setScene(new Scene(editHolidayWindow));
        stage.show();

    }
}


