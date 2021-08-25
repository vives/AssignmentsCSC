package fillcircle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*Performs Fill up the Circles operations*/
public class FillCircle extends Application {
    
    /*Main entry point for this application*/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FillCircleFXML.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Fill up the Circles");
        stage.setScene(scene);
        stage.show();
    }

    /*This is a main method which is used to initiate the process*/
    public static void main(String[] args) {
        launch(args);
    }
}
