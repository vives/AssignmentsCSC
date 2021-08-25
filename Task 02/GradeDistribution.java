import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.chart.PieChart;

//Displays Pie Chart
public class GradeDistribution extends Application {

    //Main entry point for this application
    @Override
    public void start(Stage stage) {

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("HD", 15),
                new PieChart.Data("DI", 20),
                new PieChart.Data("CR", 25),
                new PieChart.Data("PS", 35),
                new PieChart.Data("FL", 5));

        final PieChart chart = new PieChart(pieChartData);
        VBox vbox = new VBox(chart);
        chart.setLabelLineLength(50);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(false);
        chart.setClockwise(false);

        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), "=", String.format("%.0f", data.getPieValue()), "%"
                        )
                )
        );

        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 400, 400);
        stage.setWidth(500);
        stage.setHeight(500);
        stage.setTitle("Grade Distribution");
        stage.setScene(scene);
        stage.show();
    }

    //This is a main method which is used to initiate the process
    public static void main(String args[]) {
        launch(args);
    }
}
