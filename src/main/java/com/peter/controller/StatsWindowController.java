package com.peter.controller;


import com.peter.helperClasses.ExcelWriter;
import com.peter.helperClasses.Utilities;
import com.peter.interfaces.BoatStatProgramViewController;
import com.peter.mainApplication.MainApplication;
import com.peter.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by KungPeter on 2016-03-08.
 */
public class StatsWindowController implements BoatStatProgramViewController {

    private static MainApplication mainApplication;
    private static ObservableList<Boat> boatList;
    private static LocalDate fromDate;
    private static LocalDate toDate;
    private static BoatStatisticalModel boatStatModel;
    private static boolean dataHasBeenCollected = false;

    private static ObservableList<StatTableEntry> passPerDayList = FXCollections.observableArrayList();
    private static ObservableList<StatTableEntry> passPerRoundList = FXCollections.observableArrayList();
    private static ObservableList<ExtendedStatTableEntry> roundsWithMoreThanXList = FXCollections.observableArrayList();



    @FXML
    private DatePicker fromDatePicker;

    @FXML
    private DatePicker toDatePicker;

    @FXML
    private Button collectDataButton;

    @FXML
    private ComboBox<Boat> boatChooserCmb;

    @FXML
    private CheckBox chooseAllBoatsCheckbox;

    @FXML
    private ComboBox<String> roundComboBox;

    @FXML
    private TextField roundsWithMoreThanXTextField;

    @FXML
    private Label selectedStatsLabel;

    @FXML
    private Label numberOfRoundsLabel0;

    @FXML
    private Label numberOfRoundsLabel1;

    @FXML
    private Label numberOfRoundsLabel2;

    @FXML
    private Label totalPassOnLabel;

    @FXML
    private Label totalPassOnForRoundLabel;

    @FXML
    private Label totalNoRoundsLabel;

    @FXML
    private Button showStatsButton;

    @FXML
    private TableView<StatTableEntry> passPerDayTableView;

    @FXML
    private TableColumn<StatTableEntry, String> passPerDayDayColumn;

    @FXML
    private TableColumn<StatTableEntry, Integer> passPerDayNumOfTravelsColumn;

    @FXML
    private TableView<StatTableEntry> passPerRoundTableView;

    @FXML
    private TableColumn<StatTableEntry, String> passPerRoundDayColumn;

    @FXML
    private TableColumn<StatTableEntry, Integer> passPerRoundNumOfTravelsColumn;

    @FXML
    private TableView<ExtendedStatTableEntry> roundsWithMoreThanTableview;

    @FXML
    private TableColumn<ExtendedStatTableEntry, String> roundsWithMoreThanDayColumn;

    @FXML
    private TableColumn<ExtendedStatTableEntry, String> roundsWithMoreThanRoundColomn;

    @FXML
    private TableColumn<ExtendedStatTableEntry, Integer> roundsWithMoreThanTravelsColumn;

    @FXML
    private Label numberOfTravelsForRoundLabel;

    @FXML
    private GridPane overallStatsGridPane;

    @FXML
    private Label fromTuvesvikLabel;

    @FXML
    private Label toTuvesvikLabel;

    @FXML
    private Label fromGullholmenHLabel;

    @FXML
    private Label toGullholmenHLabel;

    @FXML
    private Label fromGullholmsbadenLabel;

    @FXML
    private Label toGullholmsBadenLabel;

    @FXML
    private Label fromKaringonLabel;

    @FXML
    private Label toKaringonLabel;

    // Setup
    /*------------------------------------------------------------------------------------*/

    @Override
    public void setBoats(ObservableList<Boat> boats) {
        boatList = boats;
    }

    @Override
    public void setMainApplication(MainApplication mainApp) {
        mainApplication = mainApp;
    }


    public void setBoatStatModel(BoatStatisticalModel boatStatisticalModel)
    {
        boatStatModel = boatStatisticalModel;
    }

    @Override
    public void initializeStart()
    {
        setupInitialLayout();
        setUpInitialState();
        configureTables();
        setUpListeners();

    }

    private void setupInitialLayout()
    {

    }

    private void setUpInitialState()
    {
        fromDatePicker.setValue(LocalDate.now());
        toDatePicker.setValue(LocalDate.now());
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();

        boatChooserCmb.setItems(boatList);
        boatChooserCmb.getSelectionModel().selectFirst();
        selectedStatsLabel.setText("Statistik för " + boatChooserCmb.getSelectionModel().getSelectedItem().getName());

        roundComboBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17" +
                "18", "19", "20", "21", "22");
        roundComboBox.getSelectionModel().selectFirst();

        roundsWithMoreThanXTextField.setEditable(true);
        roundsWithMoreThanXTextField.setText("30");

        numberOfRoundsLabel1.textProperty().bind(roundsWithMoreThanXTextField.textProperty());

    }

    private void configureTables()
    {
        // set up passPerDayTableview
        passPerDayTableView.setItems(passPerDayList);

        passPerDayDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        passPerDayDayColumn.prefWidthProperty().bind(passPerDayTableView.prefWidthProperty().divide(2));

        passPerDayNumOfTravelsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPass"));
        passPerDayNumOfTravelsColumn.prefWidthProperty().bind(passPerDayTableView.prefWidthProperty().divide(2).subtract(23));

        // set up passPerRoundTableview
        passPerRoundTableView.setItems(passPerRoundList);

        passPerRoundDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        passPerRoundDayColumn.prefWidthProperty().bind(passPerRoundTableView.prefWidthProperty().divide(2));

        passPerRoundNumOfTravelsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPass"));
        passPerRoundNumOfTravelsColumn.prefWidthProperty().bind(passPerRoundTableView.prefWidthProperty().divide(2).subtract(23));

        // set up roundsWithMoreThanTableview
        roundsWithMoreThanTableview.setItems(roundsWithMoreThanXList);

        roundsWithMoreThanDayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        roundsWithMoreThanDayColumn.prefWidthProperty().bind(roundsWithMoreThanTableview.prefWidthProperty().divide(3));

        roundsWithMoreThanRoundColomn.setCellValueFactory(new PropertyValueFactory<>("round"));
        roundsWithMoreThanRoundColomn.prefWidthProperty().bind(roundsWithMoreThanTableview.prefWidthProperty().divide(3));

        roundsWithMoreThanTravelsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfPass"));
        roundsWithMoreThanTravelsColumn.prefWidthProperty().bind(roundsWithMoreThanTableview.prefWidthProperty().divide(3).subtract(23));

    }

    private void setUpListeners()
    {
        // Set up listener to fromDateSelectedValueProperty
        fromDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isAfter(toDate))
            {
                showAlert("OOOPS!", "\"Från och med\" kan inte vara efter \"till och med\"", Alert.AlertType.ERROR);
                fromDatePicker.setValue(oldValue);
                fromDate = oldValue;
                return;
            }
            fromDate = newValue;
        });

        // Set up listener to toDateSelectedValueProperty
        toDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.isBefore(fromDate))
            {
                showAlert("OOOPS!", "\"Tilloch med\" kan inte vara tidigare än \"från och med\"", Alert.AlertType.ERROR);
                toDatePicker.setValue(oldValue);
                fromDate = oldValue;
                return;
            }
            toDate = newValue;
        });
    }


    // End Setup
    /*--------------------------------------------------------------------------------------*/





    @FXML
    private void handleCollectDataButtonClicked()
    {

        if (boatStatModel != null && !boatList.isEmpty())
        {
            boatStatModel.clearModel();
            for(Boat boat : boatList)
            {
                boatStatModel.addNewBoatStatList(DatabaseHandler.getDataForBoatAndPeriod(boat, fromDate, toDate));
            }

            dataHasBeenCollected = true;
        }
    }

    @FXML
    private void handleBoatChooserClicked()
    {
        if (boatChooserCmb.getSelectionModel().getSelectedItem() == null)
            return;

        selectedStatsLabel.setText("Statistik för " + boatChooserCmb.getSelectionModel().getSelectedItem().getName());
    }

    @FXML
    private void handleAllBoatsCheckboxClicked()
    {
        if(chooseAllBoatsCheckbox.isSelected())
        {
            boatChooserCmb.setDisable(true);
            boatChooserCmb.getSelectionModel().clearSelection();
            roundsWithMoreThanTableview.setVisible(false);
            roundsWithMoreThanXTextField.setDisable(true);
            selectedStatsLabel.setText("Statistik för alla båtar");

        }

        else
        {
            boatChooserCmb.setDisable(false);
            boatChooserCmb.getSelectionModel().selectFirst();
            roundsWithMoreThanTableview.setVisible(true);
            roundsWithMoreThanXTextField.setDisable(false);
            selectedStatsLabel.setText("Statistik för " + boatChooserCmb.getSelectionModel().getSelectedItem().getName());

        }
    }

    @FXML
    private void handleShowStatsButtonClicked()
    {
        int moreThanXPass = 0;

        if (boatStatModel != null && dataHasBeenCollected)
        {
            // Fill the tableViews
            if (!chooseAllBoatsCheckbox.isSelected())
            {
                try
                {
                    moreThanXPass  = Integer.parseInt(roundsWithMoreThanXTextField.getText());
                }
                catch (NumberFormatException e)
                {
                    showAlert("OOOPS!", "Ogiltigt värde i fältet", Alert.AlertType.ERROR);
                    return;
                }
            }
            else if (chooseAllBoatsCheckbox.isSelected())
            {
                moreThanXPass = 0;
            }

            // If no boat is selected, the model will assume you want stats for all avalible boats
            int selectedBoat = boatChooserCmb.getSelectionModel().getSelectedIndex();
            String round = roundComboBox.getSelectionModel().getSelectedItem();

            // Fill the tables
            boatStatModel.getDataForTables(selectedBoat, passPerDayList, passPerRoundList, roundsWithMoreThanXList, fromDate, toDate, round,
                    moreThanXPass, totalPassOnLabel, totalPassOnForRoundLabel, totalNoRoundsLabel);

            numberOfTravelsForRoundLabel.setText(round);

            // Fill the labels
            boatStatModel.getDataForLabels(selectedBoat, fromTuvesvikLabel, toTuvesvikLabel, fromGullholmenHLabel, toGullholmenHLabel,
                    fromGullholmsbadenLabel, toGullholmsBadenLabel, fromKaringonLabel, toKaringonLabel);
        }

        else
        {
            showAlert("Oj då!!", "Välj först en statistikperiod och \n" +
                    "tryck på \"Hämta Data\" innan du kan bearbeta statistik", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void handleExportToExcelBtnClicked(){

        File selectedFile = new FileChooser().showSaveDialog(null);

        // If abort i chosen
        if (selectedFile == null)
            return;

        ObservableList<Object> labelList = (ObservableList) overallStatsGridPane.getChildren();
        ExcelWriter.writeToExcelFile(selectedFile, passPerDayList, passPerRoundList, roundsWithMoreThanXList, numberOfTravelsForRoundLabel,
                numberOfRoundsLabel1, selectedStatsLabel, labelList, fromDate, toDate);
    }

    @FXML
    private void handleTableViewClicked(MouseEvent event)
    {
        TableView selectedTableView = (TableView) event.getSource();
        ObservableList<StatTableEntry> selectedList = selectedTableView.getItems();
        FXMLLoader chartViewLoader = new FXMLLoader(getClass().getResource("/view/chartView.fxml"));

        BorderPane rootPane = null;
        ChartViewController controller = null;

        try
        {
            rootPane = chartViewLoader.load();
            controller = chartViewLoader.getController();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            showAlert("Fel!", "Kunde inte ladda Diagrambild", Alert.AlertType.ERROR);
            return;
        }

        controller.setData(selectedList);

        Scene scene = new Scene(rootPane);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.setAlwaysOnTop(true);
        stage.show();

    }

    public BoatStatisticalModel getBoatStatModel()
    {
        if (boatStatModel != null)
            return boatStatModel;
        else
            return  null;
    }

    private void showAlert(String headerText, String message, Alert.AlertType alertType)
    {
        Utilities.showAlert(headerText, message, alertType);
    }

}
