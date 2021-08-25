package displayrecords;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*Performs data Display records operations*/
public class DisplayRecords extends Application {
    
    /*Main entry point for this application*/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DisplayRecordsFXML.fxml"));   
        
        Scene scene = new Scene(root);
        stage.setTitle("Clusters");
        stage.setScene(scene);
        stage.show();
    }

    /*This is a main method which is used to initiate the process*/
    public static void main(String[] args) {
        launch(args);
    }
    
}
