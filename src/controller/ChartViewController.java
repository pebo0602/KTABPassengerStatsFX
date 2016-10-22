package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import model.StatTableEntry;

/**
 * Created by KungPeter on 2016-03-16.
 */

public class ChartViewController {

    @FXML
    private BorderPane rootBorderPane;

    @FXML
    private Label headerLabel;

    @FXML
    private CheckBox showBarsCheckbox;



    private LineChart<String, Number> lineChart;
    private BarChart<String, Number> barChart;


    @FXML
    private void initialize()
    {
        CategoryAxis lineXAxis = new CategoryAxis();
        NumberAxis lineYAxis = new NumberAxis();

        CategoryAxis barXAxis = new CategoryAxis();
        NumberAxis barYAxis = new NumberAxis();

        lineChart = new LineChart<String, Number>(lineXAxis, lineYAxis);
        barChart = new BarChart<String, Number>(barXAxis, barYAxis);
    }


    public void setData(ObservableList<StatTableEntry> data)
    {
        XYChart.Series<String, Number> lineSeries = new XYChart.Series<>();
        XYChart.Series<String, Number> barSeries = new XYChart.Series<>();

        for (StatTableEntry entry : data)
        {
            lineSeries.getData().add(new XYChart.Data<>(entry.getDay(), entry.getNumOfPass()));
            barSeries.getData().add(new XYChart.Data<>(entry.getDay(), entry.getNumOfPass()));
        }

        lineChart.getData().add(lineSeries);
        barChart.getData().add(barSeries);
        rootBorderPane.setCenter(lineChart);
    }

    @FXML
    private void handleShowBarsCheckboxClicked()
    {
        if (showBarsCheckbox.isSelected())
        {
            rootBorderPane.setCenter(barChart);
        }

        else
            rootBorderPane.setCenter(lineChart);
    }
}
