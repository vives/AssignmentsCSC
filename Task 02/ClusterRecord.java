import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sun.plugin2.os.windows.SECURITY_ATTRIBUTES;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//Displays data in Scatter chart
public class ClusterRecord extends Application {

    //This is a main method which is used to initiate the process
    public static void main(String[] args) {
        launch(args);
    }

    //Main entry point for this application
    @Override
    public void start(Stage stage) {

        try {
            FileInputStream fs = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(fs);
            Statement statement1 = MYSQLConnection.getConnection().createStatement();
            Statement statement2 = MYSQLConnection.getConnection().createStatement();
            String getDistinctClusterQuery = "SELECT DISTINCT " + prop.getProperty("COLUMN3") + " FROM "
                    + prop.getProperty("TABLE_NAME");
            ResultSet getDistinctClusterResult = statement1.executeQuery(getDistinctClusterQuery);
            if(!getDistinctClusterResult.next()) {
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText("Warning Message");
                alert.setContentText("Database has no data to show");
                alert.showAndWait();
            }else {
                stage.setTitle("Clusters");
                final NumberAxis xAxis = new NumberAxis(0, 100, 2);
                final NumberAxis yAxis = new NumberAxis(0, 100, 2);
                final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);

                do{
                    XYChart.Series series = new XYChart.Series();
                    String getXAndYQuery = "SELECT " + prop.getProperty("COLUMN1") + "," + prop.getProperty("COLUMN2") + " FROM "
                            + prop.getProperty("TABLE_NAME") + " WHERE Cluster = " + '"' +
                            getDistinctClusterResult.getString("Cluster") + '"';
                    ResultSet getXAndYResult = statement2.executeQuery(getXAndYQuery);
                    series.setName(getDistinctClusterResult.getString("Cluster"));
                    while (getXAndYResult.next()) {
                        String x = getXAndYResult.getString("X");
                        String y = getXAndYResult.getString("Y");
                        series.getData().add(new XYChart.Data(Double.parseDouble(x), Double.parseDouble(y)));
                    }
                    sc.getData().addAll(series);
                } while (getDistinctClusterResult.next());
                statement1.close();
                statement2.close();

                Scene scene = new Scene(sc, 500, 400);
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException se) {
            System.out.println("Error while making MYSQL connection: " + se.getMessage());
        }catch (IOException ie){
            System.out.println("Error while loading config file: " + ie.getMessage());
        }
    }
}