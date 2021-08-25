import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/*This FillUpCircle program implements an application that draws two circles,e a line between those circles*/
public class FillUpCircle extends Application {

    /*This method is used to start the application*/
    @Override
    public void start(Stage primaryStage) {
        Circle circle1 = new Circle();
        Circle circle2 = new Circle();
        Line line = new Line();
        Text text = new Text();

        float x1 = (float) (Math.random() * 500);
        float y1 = (float) (Math.random() * 500);
        float x2 = (float) (Math.random() * 250);
        float y2 = (float) (Math.random() * 250);

        drawCircles(circle1, x1, y1);
        drawCircles(circle2, x2, y2);
        drawLine(line, text, x1, y1, x2, y2);

        Group root = new Group();
        Scene scene = new Scene(root, 600, 600);

        root.getChildren().add(circle1);
        root.getChildren().add(circle2);
        root.getChildren().add(line);
        root.getChildren().add(text);
        primaryStage.setTitle("Distance Between Two Circles");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*Draws a Circle*/
    public void drawCircles(Circle circle, float x, float y) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        circle.setFill(Color.BLACK);
    }

    /*Draws a Line*/
    public void drawLine(Line line, Text text, float x1, float y1, float x2, float y2) {
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x2);
        line.setEndY(y2);

        double distance = findDistance(x1, y1, x2, y2);
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

    /*Calculate distance between two points*/
    public double findDistance(float x1, float y1, float x2, float y2) {
        double distance = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return distance;
    }

    /*This is a main method which initiates the process*/
    public static void main(String[] args) {
        launch(args);
    }
}