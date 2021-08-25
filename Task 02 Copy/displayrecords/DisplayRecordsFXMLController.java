package displayrecords;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*Perform Controller operation*/
public class DisplayRecordsFXMLController implements Initializable {

    static String hostName = "localhost";
    static String databaseName = "dataclustering";
    static String portNo = "3306";
    static String tableName = "Result";
    static String user = "root";
    static String password = "admin";

    @FXML
    private ScatterChart<?, ?> scatterChart;

    /*Performs task when program is being loaded where Scatter Chart is 
    being modified*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Statement statement1 = getConnection().createStatement();
            Statement statement2 = getConnection().createStatement();
            String query1 = buildDistinctClusterQuery();
            ResultSet getDistinctClusterResult = statement1.executeQuery(query1);
            if (getDistinctClusterResult.next() == false) {
                showErrorMessage(AlertType.WARNING, "No data to display");
            } else {
                NumberAxis xAxis = (NumberAxis) scatterChart.getXAxis();
                xAxis.setUpperBound(getMax("X"));
                xAxis.setLowerBound(getMin("X"));
                NumberAxis yAxis = (NumberAxis) scatterChart.getYAxis();
                yAxis.setUpperBound(getMax("Y"));
                yAxis.setLowerBound(getMin("Y"));

                do {
                    XYChart.Series series = new XYChart.Series();
                    String query2 = buildXAndYQuery(getDistinctClusterResult.getString("Cluster"));
                    ResultSet getXAndYResult = statement2.executeQuery(query2);
                    series.setName(getDistinctClusterResult.getString("Cluster"));
                    
                    while (getXAndYResult.next()) {
                        String x = getXAndYResult.getString("X");
                        String y = getXAndYResult.getString("Y");
                        series.getData().add(new XYChart.Data(Double.parseDouble(x),
                                Double.parseDouble(y)));
                    }
                    scatterChart.getData().addAll(series);
                } while (getDistinctClusterResult.next());
                statement1.close();
                statement2.close();
            }
        } catch (SQLException e) {
            showErrorMessage(AlertType.ERROR, "Error while connecting Database");
        }
    }

    /*This method is used to ceate Alert and show message*/
    public static void showErrorMessage(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText("Message");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*This method is used to get SQL connection*/
    public static Connection getConnection() {
        try {        
            String connectionURL = "jdbc:mysql://" + hostName + ":" + portNo + "/"
                    + databaseName;
            Connection connection = DriverManager.getConnection(connectionURL,
                    user, password);
            return connection;
        } catch (SQLException e) {
            showErrorMessage(AlertType.ERROR, "Could not connect with database");
        }
        return null;
    }

    /*This method is used to build a query which reterive all distinct Cluster query*/
    public static String buildDistinctClusterQuery() {
        return "SELECT DISTINCT Cluster FROM " + tableName;
    }

    /*This method is used to build a query which reterive Column X & Y  
    of given Cluster*/
    public static String buildXAndYQuery(String clusterName) {
        return "SELECT X,Y FROM " + tableName + " WHERE Cluster = "
                + '"' + clusterName + '"';
    }

    /*This is method is used to build a query to find maximum value of given Column*/
    public static String buildFindMaxQuery(String columeName) {
        return "SELECT MAX( " + columeName + " )FROM " + tableName;
    }

    /*This is method is used to build a query to find minium value of given Column*/
    public static String buildFindMinQuery(String columeName) {
        return "SELECT MIN( " + columeName + " )FROM " + tableName;
    }

    /*This method is used to get maximum value form result of query*/
    public static double getMax(String columnName) throws SQLException {
        double max = 0.0;
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery(buildFindMaxQuery(columnName));
        if (rs.next()) {
            max = rs.getDouble(1);
        }
        return max;
    }

    /*This method is used to get minium value form result of query*/
    public static double getMin(String columnName) throws SQLException {
        double min = 0.0;
        Statement st = getConnection().createStatement();
        ResultSet rs = st.executeQuery(buildFindMinQuery(columnName));
        if (rs.next()) {
            min = rs.getDouble(1);
        }
        return min;
    }
}