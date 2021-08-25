package fillcircle;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*Perform Controller operation*/
public class FillCircleFXMLController implements Initializable {

    @FXML
    private GridPane gridPane;
    @FXML
    private Circle circle1;
    @FXML
    private Circle circle3;
    @FXML
    private Circle circle2;
    @FXML
    private RadioButton radioButton1;
    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton radioButton2;
    @FXML
    private RadioButton radioButton3;
    
    /*Performs task when program is being loaded*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup group = new ToggleGroup();
        radioButton1.setToggleGroup(group);
        radioButton2.setToggleGroup(group);
        radioButton3.setToggleGroup(group);
        
         group.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
         {
            public void changed(ObservableValue<? extends Toggle> ob, 
                    Toggle o, Toggle n) {
                RadioButton rb = (RadioButton) group.getSelectedToggle();

                if (rb == radioButton1) {                
                    circle1.setFill(Color.GREEN);
                    circle2.setFill(Color.WHITE);
                    circle3.setFill(Color.WHITE);
                } else if (rb == radioButton2) {
                    circle2.setFill(Color.YELLOW);
                    circle1.setFill(Color.WHITE);
                    circle3.setFill(Color.WHITE);
                } else if (rb == radioButton3) {
                    circle3.setFill(Color.RED);
                    circle2.setFill(Color.WHITE);
                    circle1.setFill(Color.WHITE);
                }
            }
        });
    }
}