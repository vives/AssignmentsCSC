import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;

/*This DrawPieChart program implements an application that draws Pie Chart*/
public class DrawPieChart extends Application {

    /*This is a main method which is used to run the task*/
    public static void main(String args[]) {
        launch(args);
    }

    /*This method is used to initiate the process*/
    @Override
    public void start(Stage primaryStage) {
        VBox vbox = loadData();
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Grade Distribution");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*This method is used to add data into Pie Chart*/
    public VBox loadData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("HD", 15),
                new PieChart.Data("DI", 20),
                new PieChart.Data("CR", 25),
                new PieChart.Data("PS", 35),
                new PieChart.Data("FL", 5));

        PieChart pieChart = new PieChart(pieChartData);

        pieChart.setLabelsVisible(true);
        pieChart.setLegendVisible(false);
        pieChart.setClockwise(false);

        addLabel(pieChart);

        return new VBox(pieChart);
    }

    /*This method is used to modify the label name*/
    public void addLabel(PieChart pieChart) {
        pieChart.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), "=", String.format("%.0f", data.getPieValue()), "%"
                        )
                )
        );
    }
}