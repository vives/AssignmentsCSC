import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

//Fills the circles
public class FillUpTheCircle extends Application {

    //This is a main method which is used to launch the task
    public static void main(String[] args) {
        launch(args);
    }

    //This method is used to initiate the process
    @Override
    public void start(Stage primaryStage) {
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(10, 10, 10, 10));
        Circle c1 = new Circle();
        Circle c2 = new Circle();
        Circle c3 = new Circle();

        c1.setCenterX(10);
        c1.setCenterY(40);
        c1.setRadius(10.0);
        c1.setStrokeWidth(2);
        c1.setFill(Color.WHITE);
        c1.setStroke(Color.BLACK);

        c2.setCenterX(10);
        c2.setCenterY(30);
        c2.setRadius(10.0);
        c2.setStrokeWidth(2);
        c2.setFill(Color.WHITE);
        c2.setStroke(Color.BLACK);

        c3.setCenterX(10);
        c3.setCenterY(20);
        c3.setRadius(10.0);
        c3.setStrokeWidth(2);
        c3.setFill(Color.WHITE);
        c3.setStroke(Color.BLACK);

        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Green");
        rb1.setToggleGroup(group);

        RadioButton rb2 = new RadioButton("Yellow");
        rb2.setToggleGroup(group);

        RadioButton rb3 = new RadioButton("Red");
        rb3.setToggleGroup(group);

        gp.setAlignment(Pos.CENTER);

        gp.setHgap(10);
        gp.setVgap(10);
        gp.add(c1, 1, 1);
        gp.add(c2, 1, 2);
        gp.add(c3, 1, 3);
        gp.add(rb1, 0, 5);
        gp.add(rb2, 1, 5);
        gp.add(rb3, 2, 5);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                RadioButton rb = (RadioButton) group.getSelectedToggle();

                if (rb == rb1) {
                    c1.setFill(Color.GREEN);
                    c2.setFill(Color.WHITE);
                    c3.setFill(Color.WHITE);
                } else if (rb == rb2) {
                    c2.setFill(Color.YELLOW);
                    c1.setFill(Color.WHITE);
                    c3.setFill(Color.WHITE);
                } else if (rb == rb3) {
                    c3.setFill(Color.RED);
                    c1.setFill(Color.WHITE);
                    c2.setFill(Color.WHITE);
                }
            }
        });

        Scene scene = new Scene(gp, 500, 500);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.setTitle("Fill up the Circles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
