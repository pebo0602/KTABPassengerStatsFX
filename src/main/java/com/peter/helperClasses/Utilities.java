package com.peter.helperClasses;

import javafx.scene.control.Alert;

/**
 * Created by KungPeter on 2016-03-15.
 */
public class Utilities {



    public static void showAlert(String headerText, String message, Alert.AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(message);
        alert.showAndWait();

    }
}
