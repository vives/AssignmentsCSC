package dataupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

/*Perform Controller operation*/
public class DataUploadFXMLController implements Initializable {

    static String hostName = "localhost";
    static String databaseName = "dataclustering";
    static String portNo = "3306";
    static String tableName = "result";
    static String user = "root";
    static String password = "admin";

    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private TextField textField;
    @FXML
    private Label lable;

    @FXML
    /*This Event is used to handle select data file*/
    private void handleButton1Action(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File choosedFile = fileChooser.showOpenDialog(null);
        if (choosedFile != null) {
            textField.setText(choosedFile.getAbsolutePath());
        } else {
            showErrorMessage(Alert.AlertType.WARNING,
                    "File is not selected to upload data");
        }
    }

    @FXML
    /*This Event is used to handle upload data*/
    private void handleButton2Action(ActionEvent event) {
        if (!textField.getText().isEmpty()) {
            boolean reult = uploadData();
            String status = "";
            if (reult) {
                status = "Success";
            } else {
                status = "Fail";
                textField.setText("");
            }
            showErrorMessage(AlertType.INFORMATION, "Upload Status : " + status);
        } else {
            showErrorMessage(AlertType.WARNING, "Please select the file");
        }
    }

    /*Performs task when program is being loaded*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /*This method is used to get SQL connection*/
    public static Connection getConnection() {
        try {
            String hostName = "localhost";
            String databaseName = "dataclustering";
            String portNo = "3306";
            String tableName = "RESULT";
            String user = "root";
            String password = "admin";

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

    /*This method is used to ceate Alert and show message*/
    public static void showErrorMessage(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText("Error Message");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /*This methos is used to update data into Database*/
    public boolean uploadData() {
        BufferedReader reader = null;
        boolean result = false;
        try {
            File file = new File(textField.getText());
            String fileName = file.getName();

            //To check whether this file is text or not
            if (!fileName.substring(fileName.lastIndexOf(".")).equals(".txt")) {
                showErrorMessage(AlertType.ERROR, "Please check the type of file");
            } else if (file.length() == 0) {
                showErrorMessage(AlertType.ERROR, "File has no data");
            } else {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                int count = 0;
                while (line != null) {
                    Object[] lines = line.trim().split(" ");
                    count++;
                    Statement statement = getConnection().createStatement();
                    if (count == 1) {
                        DatabaseMetaData dbm = getConnection().getMetaData();
                       ResultSet existingTable = dbm.getTables(null, null, tableName, null);
                        if (!existingTable.next()) {                        
                            String query = buildCreateTableQuery();
                            System.out.println(query);
                            statement.executeUpdate(query);
                            showErrorMessage(AlertType.INFORMATION, "Table has been created");
                            statement.close();
                        }                      
                    }else {                      
                        String query = buildInsertQuery(lines[0].toString(),
                                lines[1].toString(), lines[2].toString());
                       
                        statement.executeUpdate(query);                      
                    }
                    //To move to next line
                        line = reader.readLine();
                    }
                    result = true;
                }
            }catch (IOException e) {
            showErrorMessage(AlertType.ERROR, "File reading error");
        }catch (SQLException e) {
            showErrorMessage(AlertType.ERROR, "SQL conection error");
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                showErrorMessage(AlertType.ERROR, "Error on closing "
                        + "BufferedReader");
            }
        }
            return result;
        }

    

    public static String buildInsertQuery(String Column1, String Column2,
            String Column3) {
        return "INSERT INTO " + tableName + "(X,Y,Cluster) "
                + "VALUES(" + Double.parseDouble(Column1) + ","
                + Double.parseDouble(Column2) + "," + '"'
                + Column3 + '"' + ")";
    }

    public static String buildCreateTableQuery() {
        return "CREATE TABLE " + tableName + " (X DOUBLE, Y DOUBLE,"
                + " Cluster VARCHAR(45))";
    }
}