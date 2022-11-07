package com.peter.controller;

import com.peter.helperClasses.Utilities;
import com.peter.interfaces.BoatStatProgramViewController;
import com.peter.mainApplication.MainApplication;
import com.peter.model.Boat;
import com.peter.model.BoatStatEntry;
import com.peter.model.DatabaseHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class InputWindowController implements BoatStatProgramViewController {


    private static ObservableList<Boat> boatList;
    private static ObservableList<LocalDate> holidayList;
    private static ObservableList<BoatStatEntry> tableList = FXCollections.observableArrayList();
    private static LocalDate selectedDate;
    private static Boat selectedBoat;
    private static int passOn = 0;
    private static int passOff = 0;

    private static MainApplication mainApplication;


    private static final String passFieldDefaultColorStyle = "-fx-background-color: #ffffff;";
    private static final String passFieldEndStatioColorStyle = "-fx-background-color: #ff9999;";


    @FXML
    private DatePicker datePicker;

    @FXML
    private CheckBox holidayCheckBox;

    @FXML
    private ComboBox<Boat> boatChooserCmb;

    @FXML
    private ComboBox<String> departChooserCmb;

    @FXML
    private TextField passOnTextField;

    @FXML
    private TextField passOffTextField;

    @FXML
    private Button sendButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button controlButton;

    @FXML
    private TableView<BoatStatEntry> tableView;

    @FXML
    private TableColumn<BoatStatEntry, String> dayColumn;

    @FXML
    private TableColumn<BoatStatEntry, String> roundColumn;

    @FXML
    private TableColumn<BoatStatEntry, String> departureColumn;

    @FXML
    private TableColumn<BoatStatEntry, Integer> passONColumn;

    @FXML
    private TableColumn<BoatStatEntry, Integer> passOFFColumn;

    @FXML
    private TableColumn<BoatStatEntry, String> addedColumn;

    @FXML
    private ProgressBar progressBar;


    //Setup
    /*----------------------------------------------------------------------------------------*/

    @Override
    public void setMainApplication(MainApplication mainApp) {
        mainApplication = mainApp;
    }

    public void setBoats(ObservableList<Boat> boats) {
        boatList = boats;
    }

    public void setHolidays(ObservableList<LocalDate> holidays) {
        holidayList = holidays;
    }

    @Override
    public void initializeStart() {
        setupInitialLayout();
        setUpInitialState();
        setUpListeners();
        configureTable();
        getUpdatedDepartureList();
        DatabaseHandler.getEntriesForSelectedDate(selectedBoat, selectedDate, tableList);
    }

    private void setupInitialLayout() {

    }

    private void setUpInitialState() {
        datePicker.setValue(LocalDate.now());
        selectedDate = datePicker.getValue();

        boatChooserCmb.setItems(boatList);

        boatChooserCmb.getSelectionModel().selectFirst();
        selectedBoat = boatChooserCmb.getValue();

        progressBar.setVisible(false);
        sendButton.setDefaultButton(true);
    }

    private void setUpListeners() {

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            selectedDate = newValue;

            if (holidayList.contains(selectedDate))
                holidayCheckBox.setSelected(true);
            else
                holidayCheckBox.setSelected(false);

            DatabaseHandler.getEntriesForSelectedDate(selectedBoat, selectedDate, tableList);

            getUpdatedDepartureList();

        });


        departChooserCmb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null)
                return;

            if (newValue.contains("Start")) {

                passOnTextField.setEditable(true);
                passOnTextField.setText("");
                passOnTextField.setStyle(passFieldDefaultColorStyle);

                passOffTextField.setEditable(false);
                passOffTextField.setText("0");
                passOffTextField.setStyle(passFieldDefaultColorStyle);

            } else if (newValue.contains("Slut")) {
                passOnTextField.setEditable(false);
                passOnTextField.setText("0");
                passOnTextField.setStyle(passFieldEndStatioColorStyle);

                passOffTextField.setEditable(false);
                passOffTextField.setStyle(passFieldEndStatioColorStyle);


                String round = newValue.substring(newValue.length() - 2, newValue.length());
                round.trim();
                System.out.println(round);
                int numLeavingPass = DatabaseHandler.calculatePassLeavingEndStation(selectedBoat, selectedDate, round);

                if (numLeavingPass < 0) {

                    showAlert("Oj då! något gick fel!", "Negativt antal avstigande passagerare är inte möjligt.\n" +
                            "Radera senaste inmatningen och försök igen!", Alert.AlertType.ERROR);
                } else {
                    passOffTextField.setText(Integer.toString(numLeavingPass));
                }

            } else {
                passOnTextField.setEditable(true);
                passOnTextField.setText("");
                passOnTextField.setStyle(passFieldDefaultColorStyle);

                passOffTextField.setEditable(true);
                passOffTextField.setText("");
                passOffTextField.setStyle(passFieldDefaultColorStyle);

            }

        });


    }

    private void configureTable() {
        tableView.setItems(tableList);
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        roundColumn.setCellValueFactory(new PropertyValueFactory<>("round"));
        departureColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
        passONColumn.setCellValueFactory(new PropertyValueFactory<>("passenger_On"));
        passOFFColumn.setCellValueFactory(new PropertyValueFactory<>("passenger_Off"));
        addedColumn.setCellValueFactory(new PropertyValueFactory<>("added"));

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }


    // End setup
    /*-------------------------------------------------------------------------------------------*/


    @FXML
    private void holidayCheckBoxClicked() {
        this.getUpdatedDepartureList();
    }

    @FXML
    private void handleBoatChooserCmbClicked(ActionEvent e) {
        selectedBoat = boatChooserCmb.getValue();
        DatabaseHandler.getEntriesForSelectedDate(selectedBoat, selectedDate, tableList);
        this.getUpdatedDepartureList();
    }

    @FXML
    private void handleSendButtonClicked() {
        if (isValidInput()) {
            String date = selectedDate.toString();
            String depart = departChooserCmb.getSelectionModel().getSelectedItem();
            String round = depart.substring(depart.length() - 2, depart.length());

            LocalTime time = LocalTime.now();
            String added = selectedDate.atTime(time.getHour(), time.getMinute(), time.getSecond()).toString();

            BoatStatEntry newEntry = new BoatStatEntry(date, round, depart, passOn, passOff, added);
            DatabaseHandler.sendNewEntryToDatabase(selectedBoat, newEntry, tableList);
            this.getUpdatedDepartureList();
        }
    }

    @FXML
    private void handlEraseButtonClicked() {
        BoatStatEntry selectedEntry = tableView.getSelectionModel().getSelectedItem();

        if (selectedEntry != null) {
            DatabaseHandler.removeEntryFromDatabase(selectedBoat, selectedEntry);
            DatabaseHandler.getEntriesForSelectedDate(selectedBoat, selectedDate, tableList);
            getUpdatedDepartureList();
        }
    }


    @FXML
    private void handleControlButtonClicked() {
        ArrayList<Button> buttons = new ArrayList<>();

        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                progressBar.setVisible(true);
                LocalDate currentDate = datePicker.getValue();

                int daysBetween = (int) ChronoUnit.DAYS.between(currentDate, LocalDate.now()) + 2;

                if (daysBetween == 0)
                    daysBetween = 1;

                int dayUnit = 100 / daysBetween;
                int progress = 0;

                ObservableList<String> templist;

                while (currentDate.isBefore(LocalDate.now().plusDays(1))) {
                    if (holidayList.contains(currentDate))
                        holidayCheckBox.setSelected(true);
                    else
                        holidayCheckBox.setSelected(false);

                    templist = DatabaseHandler.getUpdatedDepartureList(selectedBoat, currentDate, holidayCheckBox);

                    // Create list of clickable DateLables
                    if (!templist.isEmpty()) {
                        Button button = new Button(currentDate.toString());
                        button.setPrefWidth(240);
                        button.setPrefHeight(30);
                        button.setId("stat-button");
                        button.setOnAction(event -> {
                            datePicker.setValue(LocalDate.parse(button.getText()));
                        });

                        buttons.add(button);
                    }

                    progress = progress + dayUnit;
                    updateProgress(progress, 100);
                    currentDate = currentDate.plusDays(1);
                }
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());


        task.setOnSucceeded(event -> {
            progressBar.setVisible(false);

            FXMLLoader checkDayWindowLoader = new FXMLLoader(getClass().getResource("/view/checkDayWindow.fxml"));
            VBox vBox = null;
            try {
                vBox = checkDayWindowLoader.load();

            } catch (IOException e) {
                showAlert("Oj då!, ett fel inträffade!", "Kunde inte ladda Kontrollfönstret", Alert.AlertType.ERROR);
                e.printStackTrace();
                return;
            }

            CheckDayWindowController controller = checkDayWindowLoader.getController();

            VBox listOfDaysVbox = controller.getListOfDateVbox();

            for (Button button : buttons) {
                listOfDaysVbox.getChildren().add(button);
            }

            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        });

        Thread th = new Thread(task);
        th.start();

    }

    private boolean isValidInput() {
        try {
            passOn = Integer.parseInt(passOnTextField.getText());
            passOff = Integer.parseInt(passOffTextField.getText());

            if (passOn >= 0 && passOff >= 0)
                return true;

            else
                showAlert("OOOOPS!", "Endast icke negativa tal kan anges", Alert.AlertType.ERROR);
            return false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showAlert("OOOPS! Npgot gick fel!", "Kontrollera inmatning, endast siffror är tillåtna", Alert.AlertType.ERROR);
            return false;
        }

    }

    private void getUpdatedDepartureList() {

        departChooserCmb.setItems(DatabaseHandler.getUpdatedDepartureList(selectedBoat, selectedDate, holidayCheckBox));

        if (departChooserCmb.getItems().isEmpty()) {

            departChooserCmb.getItems().add("Alla turer inmatade för dagen!");
            sendButton.setDisable(true);
        }

        else {
            sendButton.setDisable(false);
        }

        departChooserCmb.getSelectionModel().selectFirst();
    }

    private void showAlert(String headerText, String message, Alert.AlertType alertType) {
        Utilities.showAlert(headerText, message, alertType);
    }


}
