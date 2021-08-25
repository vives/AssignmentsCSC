package taxcalculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Performs Tax calculation operations
public class TaxCalculator extends Application {

    //Main entry point for this application
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TaxCalculatorFXML.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Tax Calculation");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    //Performs file read operation
    public static void fileRead(ArrayList<ArrayList<Object>> arrayList,
            String inputFilename) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFilename));
            File file = new File(inputFilename);
            if (!file.exists()) {
                System.out.println(inputFilename + " does not exit");
            } else if (file.length() <= 0) {
                System.out.println(inputFilename + " is empty");
            } else {
                readLine(reader, arrayList);
            }
        } catch (IOException e) {
            System.out.println("Error has been occurred while reading file "
                    + e.getMessage());
        } finally {
            closeBufferedReader(reader);
        }
    }

    //Reads a file line by line
    public static void readLine(BufferedReader reader,
            ArrayList<ArrayList<Object>> arrayList) throws IOException {
        String delimiter = " ";
        String line = reader.readLine();
        while (line != null) {
            ArrayList<Object> element = new ArrayList<>();

            Object[] linesData = line.trim().split(delimiter);
            for (int row = 0; row < linesData.length; row++) {
                element.add(linesData[row]);
            }
            arrayList.add(element);
            line = reader.readLine();
        }
        arrayList.remove(0);
    }

    //Closes Buffered Reader
    public static void closeBufferedReader(BufferedReader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            System.out.println("Error while closing BufferedReader " 
                    + e.getMessage());
        }
    }

    //This method is used to calculate tax for given taxable income
    public static Double calculateTax(ArrayList<ArrayList<Object>> arrayList, 
            Object income) {
        Double tax = 0.0;
        for (int row = 0; row < arrayList.size(); row++) {
            ArrayList<Object> taxList = arrayList.get(row);
            int size = taxList.size();
            Double taxableIncome = Double.parseDouble(income.toString());
            Double lowerIncome = 0.00;
            if (!taxList.get(0).toString().equals("0")) {
                lowerIncome = Double.parseDouble(modifyString(taxList.get(0).toString()));
            }
            Double upperIncome = Double.POSITIVE_INFINITY;
            if (!taxList.get(2).toString().equals("over")) {
                upperIncome = Double.parseDouble(modifyString(taxList.get(2).toString()));
            }
            if (taxableIncome >= lowerIncome && taxableIncome <= upperIncome) {
                if (size == 4) {
                    if (!taxList.get(size - 1).toString().equals("0")
                            && taxList.get(size - 1).toString().contains("$")) {
                        tax = Double.parseDouble(modifyString(taxList.get(size - 1).toString()));
                    } else {
                        tax = Double.parseDouble(taxList.get(size - 1).toString());
                    }
                } else {
                    int indexOfPlus = taxList.lastIndexOf("plus");
                    int indexOfCents = taxList.lastIndexOf("cents");
                    int indexOfOver = taxList.lastIndexOf("over");
                    if (indexOfPlus == -1) {
                        String cents = taxList.get(3).toString().substring(0,
                                taxList.get(3).toString().length() - 1);
                        tax = taxCalculation(taxableIncome,
                                modifyString(taxList.get(indexOfOver + 1).toString()),
                                cents, "");
                    } else {
                        tax = taxCalculation(taxableIncome,
                                modifyString(taxList.get(indexOfOver + 1).toString()),
                                taxList.get(indexOfCents - 1).toString(),
                                modifyString(taxList.get(indexOfPlus - 1).toString()));
                    }
                }
                break;
            }
        }
        return tax;
    }

    //This method is used to remove "," from given string value
    public static String modifyString(String Value) {
        return Value.substring(1).replace(",", "");
    }

    //This method is used to make calculations
    public static Double taxCalculation(Double income, String num1, String num2,
            String num3) {
        Double tax = 0.0;

        if (!num3.isEmpty()) {
            tax = ((income - Double.parseDouble(num1)) * Double.parseDouble(num2) / 100)
                    + Double.parseDouble(num3);
        } else {
            tax = ((income - Double.parseDouble(num1)) * Double.parseDouble(num2) / 100);
        }
        return tax;
    }
    //This is a main method which is used to initiate the process

    public static void main(String[] args) {
        launch(args);
    }
}
