package taxcalculator;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import static taxcalculator.TaxCalculator.calculateTax;
import static taxcalculator.TaxCalculator.fileRead;

//Perform Controller operation
public class TaxCalculatorFXMLController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Label lable1;
    @FXML
    private Label lable2;
    @FXML
    private TextField textField1;
    @FXML
    private Button button;
    @FXML
    private TextField textField2;

    //Performs task when program is being loaded
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    //Performs Button Click Event function
    @FXML
    private void handleButtonAction(ActionEvent event) {
        Alert a = new Alert(Alert.AlertType.NONE);
        String fileName = "taxrates.txt";
        ArrayList<ArrayList<Object>> dataArrayList = new ArrayList<>();
        fileRead(dataArrayList, fileName);
        if (dataArrayList.isEmpty()) {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setHeaderText("Error Message");
            a.setContentText("Tax details are not provided. Please check the "
                    + "data file");
            a.showAndWait();
        } else {
            if (!textField1.getText().isEmpty()) {
                DecimalFormat df = new DecimalFormat("0.00");
                textField2.setText("$".concat(df.format(calculateTax(dataArrayList, 
                        textField1.getText()))));

            } else {
                a.setAlertType(Alert.AlertType.WARNING);
                a.setHeaderText("Error Message");
                a.setContentText("Taxable income is not given to calculate");
                a.showAndWait();
            }
        }
    }
}
