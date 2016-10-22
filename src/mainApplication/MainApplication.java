package mainApplication;

import controller.InputWindowController;
import controller.LoginWindowController;
import controller.MainWindowController;
import controller.StatsWindowController;
import helperClasses.Utilities;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;

import java.net.Socket;
import java.time.LocalDate;

public class MainApplication extends Application {

    private Stage stage;
    private BorderPane mainWindow;
    private BorderPane inputWindow;
    private BorderPane statsWindow;
    private static ObservableList<Boat> boatList = FXCollections.observableArrayList();
    private static ObservableList<LocalDate> holidayList;
    private static ObservableList<LocalDate> switchTimeTableDatesList;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Test internet connection
        Socket socket = null;
        try {
            socket = new Socket("www.google.com", 80);
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
            Utilities.showAlert("Ingen internetanslutning", "Kontrollera din anslutning och försök sedan igen", Alert.AlertType.ERROR);
            return;
        }

        stage = primaryStage;

        FXMLLoader logintWindowLoader = new FXMLLoader(getClass().getResource("/view/loginWindow.fxml"));
        Parent loginWindow = logintWindowLoader.load();
        LoginWindowController loginWindowController = logintWindowLoader.getController();
        loginWindowController.initializeStart();
        loginWindowController.setMainApplication(this);

        stage.setTitle("KTAB Database");
        stage.setScene(new Scene(loginWindow));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/rescources/lightHouseIcon.png")));
        stage.setResizable(false);
        stage.show();


    }

    public void loginOk() throws Exception {
        openProgramme(stage);
    }


    private void openProgramme(Stage stage) throws Exception {
        createAllLists();

        DatabaseHandler.setSwitchTimeTableDates(switchTimeTableDatesList);

        // Get InputWindow
        FXMLLoader inputWindowLoader = new FXMLLoader(getClass().getResource("/view/inputWindow.fxml"));
        inputWindow = inputWindowLoader.load();

        InputWindowController inputWindowController = inputWindowLoader.getController();
        inputWindowController.setMainApplication(this);
        inputWindowController.setBoats(boatList);
        inputWindowController.setHolidays(holidayList);
        inputWindowController.initializeStart();

        // Get StatWindow
        FXMLLoader statsWindowLoader = new FXMLLoader(getClass().getResource("/view/statsWindow.fxml"));
        statsWindow = statsWindowLoader.load();

        BoatStatisticalModel boatStatModel = new BoatStatisticalModel(boatList);

        StatsWindowController statsWindowController = statsWindowLoader.getController();
        statsWindowController.setMainApplication(this);
        statsWindowController.setBoats(boatList);
        statsWindowController.setBoatStatModel(boatStatModel);
        statsWindowController.initializeStart();

        // Get MainWindow
        FXMLLoader mainWindowLoader = new FXMLLoader(getClass().getResource("/view/mainWindow.fxml"));
        mainWindow = mainWindowLoader.load();
        mainWindow.setCenter(inputWindow);

        MainWindowController mainWindowController = mainWindowLoader.getController();
        mainWindowController.setMainApplication(this);
        mainWindowController.setHolidays(holidayList);
        mainWindowController.initializeStart();

        stage.setScene(new Scene(mainWindow));
        stage.centerOnScreen();

    }

    private void createAllLists() {
        BoatListFactory boatListFactory = new BoatListFactory();
        boatList = boatListFactory.getBoats();

        SignificantDateListFactory significatdDateListFactory = new SignificantDateListFactory();
        holidayList = significatdDateListFactory.getHolidays();

        switchTimeTableDatesList = significatdDateListFactory.getSwitchTimeTableDates();

    }


    public void switchView(String view) {
        if (view.equalsIgnoreCase("inputWindow"))
            mainWindow.setCenter(inputWindow);
        else if (view.equalsIgnoreCase("StatsWindow"))
            mainWindow.setCenter(statsWindow);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
