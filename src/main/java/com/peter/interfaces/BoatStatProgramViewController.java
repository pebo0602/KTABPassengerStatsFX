package com.peter.interfaces;

import com.peter.mainApplication.MainApplication;
import com.peter.model.Boat;
import javafx.collections.ObservableList;

/**
 * Created by KungPeter on 2016-03-08.
 */
public interface BoatStatProgramViewController {


    void setMainApplication(MainApplication mainApp);
    void setBoats(ObservableList<Boat> boats);
    void initializeStart();
}

