import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

//Upload data into Database
public class DataLoader extends Application {

    //This is a main method which is used to initiate the process
    public static void main(String[] args) {
        launch(args);
    }

    //Main entry point for this application
    @Override
    public void start(Stage primaryStage) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button btn1 = new Button("Select File");
        grid.add(btn1, 0, 1);
        Button btn2 = new Button("Upload Data");
        btn2.setAlignment(Pos.CENTER);
        grid.add(btn2, 1, 2);


        TextField textField1 = new TextField();
        grid.add(textField1, 1, 1);

        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fc = new FileChooser();
                File selectedFile = fc.showOpenDialog(null);
                if (selectedFile != null) {
                    textField1.setText(selectedFile.getAbsolutePath());
                } else {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setHeaderText("Warning Message");
                    alert.setContentText("Please select correct file");
                    alert.showAndWait();
                }
            }
        });

        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!textField1.getText().isEmpty()) {
                    int count = 0;
                    count = loadData(textField1.getText(), count);
                    if (count > 0) {
                        alert.setAlertType(Alert.AlertType.INFORMATION);
                        alert.setContentText("Number of updated data is " + count);
                    } else if (count == 0) {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("Uploading Data is not succeeded, Please check the input file data");
                    } else {
                        alert.setAlertType(Alert.AlertType.ERROR);
                        alert.setContentText("Error while uploading file, Please check the file extension");
                    }
                    alert.showAndWait();
                } else {
                    alert.setAlertType(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error Message");
                    alert.setContentText("File is not selected to upload data");
                    alert.showAndWait();
                }
            }
        });

        Scene scene = new Scene(grid, 500, 200);
        primaryStage.setWidth(500);
        primaryStage.setHeight(200);
        primaryStage.setTitle("Data Loader");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*This method is used to read file and retrieve tax details*/
    public static int loadData(String inputFile, int totalRow) {
        BufferedReader reader = null;
        try {
            FileInputStream fs = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(fs);

            File file = new File(inputFile);
            String fileName = file.getName();

            if (!fileName.substring(fileName.lastIndexOf(".")).equals(".txt")) {
                System.out.println(fileName + " is not a text file");
            } else if (file.length() == 0) {
                System.out.println(fileName + " is empty");
            } else {
                reader = new BufferedReader(new FileReader(file));
                //  reader.readLine();
                String line = reader.readLine();
                totalRow = 0;
                while (line != null) {
                    Statement statement = MYSQLConnection.getConnection().createStatement();
                    totalRow++;
                    Object[] lines = line.trim().split(" ");
                    if (totalRow == 1) {
                        DatabaseMetaData dbm = MYSQLConnection.getConnection().getMetaData();
                        ResultSet tables = dbm.getTables(null, null, prop.getProperty("TABLE_NAME")
                                , null);
                        if(!tables.next()) {
                            String createQuery = "CREATE TABLE " + prop.getProperty("TABLE_NAME") + "( "
                                    + prop.getProperty("COLUMN1") + " " + prop.getProperty("DOUBLE__DATA_TYPE") + ", "
                                    + prop.getProperty("COLUMN2") + " " + prop.getProperty("DOUBLE__DATA_TYPE") + ", "
                                    + prop.getProperty("COLUMN3") + " " + prop.getProperty("VARCHAR_DATA_TYPE") + " )";
                            statement.executeUpdate(createQuery);
                            System.out.println("New Table has been created");
                            statement.close();
                        }
                    } else {
                        String updateQuery = "INSERT INTO " + prop.getProperty("TABLE_NAME") + "(" + prop.getProperty("COLUMN1") +
                                "," + prop.getProperty("COLUMN2") + "," + prop.getProperty("COLUMN3") + ") " +
                                "VALUES(" + Double.parseDouble(lines[0].toString()) + "," +
                                Double.parseDouble(lines[1].toString()) + "," + '"' + lines[2].toString() + '"' + ")";
                        statement.executeUpdate(updateQuery);
                        statement.close();
                    }
                    line = reader.readLine();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("MYSQL connection error: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing BufferedReader " + e.getMessage());
            }
        }
        return totalRow - 1;
    }
}