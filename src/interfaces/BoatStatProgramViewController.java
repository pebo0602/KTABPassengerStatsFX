package interfaces;

import javafx.collections.ObservableList;
import mainApplication.MainApplication;
import model.Boat;

/**
 * Created by KungPeter on 2016-03-08.
 */
public interface BoatStatProgramViewController {


    void setMainApplication(MainApplication mainApp);
    void setBoats(ObservableList<Boat> boats);
    void initializeStart();
}

