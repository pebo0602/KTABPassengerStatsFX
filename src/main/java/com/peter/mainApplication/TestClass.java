package com.peter.mainApplication;


import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by KungPeter on 2016-02-25.
 */
public class TestClass extends Application {

    @Override
    public void start(Stage theStage) {
        int i = 0;
        int j = 0;

        calk(i, j);

        System.out.println("" + i + " " + j + ":");
    }

    public static void main(String[] args) {

        launch(args);
    }

    void calk(Integer k, Integer m) {

    }


}

