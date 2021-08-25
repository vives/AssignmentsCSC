import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*This TaxCalculator program implements an application that calculates tax for taxable income*/
public class TaxCalculator extends Application {

    /*This is a main method which is used to run the task*/
    public static void main(String[] args) {
        launch(args);
    }

    /*Main entry point for this application*/
    @Override
    public void start(Stage primaryStage) {
        String inputFile = "taxrates.txt";
        ArrayList<ArrayList<Object>> dataArrayList = new ArrayList<>();
        readFile(dataArrayList, inputFile);
        Alert a = new Alert(Alert.AlertType.NONE);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label lable1 = new Label("Taxable Income:");
        grid.add(lable1, 0, 1);

        TextField textField1 = new TextField();
        grid.add(textField1, 1, 1);

        Label lable2 = new Label("Tax:");
        grid.add(lable2, 0, 2);

        TextField textField2 = new TextField();
        grid.add(textField2, 1, 2);

        Button btn = new Button("Calculate");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
        Scene scene = new Scene(grid, 500, 500);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Tax Calculation");
        primaryStage.setScene(scene);
        if (dataArrayList.isEmpty()) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setHeaderText("Error Message");
            a.setContentText("Could not find data to calculate tax");
            a.showAndWait();
        }else{
            primaryStage.show();
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    if (!textField1.getText().isEmpty()) {
                        DecimalFormat df = new DecimalFormat("0.00");
                        textField2.setText("$".concat(df.format(calculateTax(dataArrayList, textField1.getText()))));

                    } else {
                        a.setAlertType(Alert.AlertType.ERROR);
                        a.setHeaderText("Error Message");
                        a.setContentText("Please enter value for taxable income");
                        a.showAndWait();
                    }
                }
            });
        }
    }

    /*This method is used to read file and retrieve tax details*/
    public static void readFile(ArrayList<ArrayList<Object>> dataList, String inputFile) {
        BufferedReader reader = null;
        try {
            File file = new File(inputFile);
            if (!file.exists()) {
                System.out.println(inputFile + " does not exit");
            } else if (file.length() == 0) {
                System.out.println(inputFile + " is empty");
            } else {
                reader = new BufferedReader(new FileReader(inputFile));
                String line = reader.readLine();
                while (line != null) {
                    ArrayList<Object> words = new ArrayList<>();
                    Object[] lines = line.trim().split(" ");
                    for (int j = 0; j < lines.length; j++) {
                        words.add(lines[j]);
                    }
                    dataList.add(words);
                    line = reader.readLine();
                }
                dataList.remove(0);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error while reading file: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Error while closing BufferedReader " + e.getMessage());
            }
        }
    }

    /*This method is used to calculate tax for given taxable income*/
    public static Double calculateTax(ArrayList<ArrayList<Object>> dataList, Object income) {
        Double tax = 0.00;
        for (int row = 0; row < dataList.size(); row++) {
            ArrayList<Object> taxList = dataList.get(row);
            int size = taxList.size();
            Double taxableIncome = Double.parseDouble(income.toString());
            Double lowerIncome = 0.00;
            if (!taxList.get(0).toString().equals("0")) {
                lowerIncome = Double.parseDouble(taxList.get(0).toString().substring(1).replace(",", ""));
            }
            Double upperIncome = Double.POSITIVE_INFINITY;
            if (!taxList.get(2).toString().equals("over")) {
                upperIncome = Double.parseDouble(taxList.get(2).toString().substring(1).replace(",", ""));
            }
            System.out.println(size);
            System.out.println(taxList);
            if (taxableIncome >= lowerIncome && taxableIncome <= upperIncome) {
                if (size == 4) {
                    if (!taxList.get(size - 1).toString().equals("0") && taxList.get(size - 1).toString().contains("$")) {
                        tax = Double.parseDouble(taxList.get(size - 1).toString().substring(1).replace(",", ""));
                    }else{
                        tax = Double.parseDouble(taxList.get(size - 1).toString());
                    }
                } else {
                    int indexOfPlus = taxList.indexOf("plus");
                    int indexOfCents = taxList.indexOf("cents");
                    int indexOfOver = taxList.lastIndexOf("over");

                    if (indexOfPlus == -1 && indexOfCents != -1 && indexOfOver != -1) {
                        tax = (taxableIncome - Double.parseDouble(taxList.get(indexOfOver + 1).toString().substring(1).
                                replace(",", ""))) *
                                Double.parseDouble(taxList.get(indexOfCents - 1).toString()) / 100;
                    } else {
                        tax = ((taxableIncome - Double.parseDouble(taxList.get(indexOfOver + 1).toString().substring(1).
                                replace(",", ""))) *
                                Double.parseDouble(taxList.get(indexOfCents - 1).toString()) / 100) +
                                Double.parseDouble(taxList.get(indexOfPlus - 1).toString().substring(1).
                                        replace(",", ""));
                    }
                }
				break;
            }
        }
        return tax;
    }
}
