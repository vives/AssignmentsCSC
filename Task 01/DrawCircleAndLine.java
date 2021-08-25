import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

//Performs draw circle tasks
public class DrawCircleAndLine extends Application {

    //Main entry point for this application
    @Override
    public void start(Stage stage) {
        Circle circle1 = new Circle();
        Circle circle2 = new Circle();
        Line line = new Line();
        Text text = new Text();

        int random1 = 300;
        int random2 = 150;
        double width = 500;
        double height = 500;

        float x1 = generateRandomNumber(random1);
        float y1 = generateRandomNumber(random1);
        float x2 = generateRandomNumber(random2);
        float y2 = generateRandomNumber(random2);

        drawCircle(circle1, x1, y1);
        drawCircle(circle2, x2, y2);

        drawLine(line, text, x1, y1, x2, y2);

        Group root = new Group();
        Scene scene = new Scene(root, width, height);

        root.getChildren().add(circle1);
        root.getChildren().add(circle2);
        root.getChildren().add(line);
        root.getChildren().add(text);

        stage.setTitle("Distance Between Two Circles");
        stage.setScene(scene);
        stage.show();
    }

    //Generates random number
    public float generateRandomNumber(int value) {
        return (float) (Math.random() * value);
    }

    //Draws circle
    public void drawCircle(Circle circle, float x, float y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        circle.setFill(Color.BLACK);
    }

    //Draws line
    public void drawLine(Line line, Text text, float x1, float y1, float x2, float y2) {
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);

        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        text.setText(String.valueOf(distance));
        text.setX((x1 + x2) / 2);
        text.setY((y1 + y2) / 2);

        if (distance >= 100) {
            text.setFill(Color.RED);
            line.setStroke(Color.RED);
        } else {
            text.setFill(Color.GREEN);
            line.setStroke(Color.GREEN);
        }
    }

    //This is a main method which is used to initiate the process
    public static void main(String[] args) {
        launch(args);
    }
}