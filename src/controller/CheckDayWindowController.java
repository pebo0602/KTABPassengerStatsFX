package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by KungPeter on 2016-03-20.
 */
public class CheckDayWindowController {

    @FXML
    private VBox listOfDateVbox;

    @FXML
    private Button closeButton;


    public VBox getListOfDateVbox(){
        return listOfDateVbox;
    }

    @FXML
    private void handleCloseButtonClicked()
    {
        listOfDateVbox.getScene().getWindow().hide();
    }
}
