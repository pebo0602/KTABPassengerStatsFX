package mainApplication;


import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by KungPeter on 2016-02-25.
 */
public class TestClass extends Application {

    @Override
    public void start(Stage theStage)
    {
        int i = 0;
        int j = 0;

        calk(i, j);

        System.out.println("" +  i + " " + j + ":");
    }

    public static void main(String[] args) {

        launch(args);
    }

    void calk(Integer k, Integer m)
    {

    }



}

