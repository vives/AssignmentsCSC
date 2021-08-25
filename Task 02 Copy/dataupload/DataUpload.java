package dataupload;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*Performs data update operations*/
public class DataUpload extends Application {
    
    /*Main entry point for this application*/
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("DataUploadFXML.fxml"));
        
        Scene scene = new Scene(root);      
        stage.setTitle("Data Loader");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

   /*This is a main method which is used to initiate the process*/
    public static void main(String[] args) {
        launch(args);
    }
}
