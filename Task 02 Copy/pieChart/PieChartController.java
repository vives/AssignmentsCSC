package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class PieChartController  implements Initializable {

    @FXML
    PieChart piechart;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("HD", 15),
                        new PieChart.Data("DI", 20),
                        new PieChart.Data("CR", 25),
                        new PieChart.Data("PS", 35),
                        new PieChart.Data("FL", 5));

        piechart.setData(pieChartData);
        piechart.setTitle("Grade Distribution");
    }
}
