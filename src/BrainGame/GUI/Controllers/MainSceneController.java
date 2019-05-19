package BrainGame.GUI.Controllers;

import BrainGame.handlers.NewDialogHandler;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class MainSceneController {

    private static final double nodeRadius = 25;

    @FXML
    private Pane canvasContainer;
    @FXML
    private Canvas mainCanvas;
    private GraphicsContext graphics;
    private AnimationTimer gameloop;
    private int n_neurons;

    private Point2D[] circles = null;

    private boolean canDrawLine = true;
    private Point2D start, end;


    @FXML
    private void initialize() {
        initCanvas();
        initGameLoop();
    }

    private void initCanvas() {
        mainCanvas.widthProperty().bind(canvasContainer.widthProperty());
        mainCanvas.heightProperty().bind(canvasContainer.heightProperty());
        graphics = mainCanvas.getGraphicsContext2D();

        mainCanvas.setOnMousePressed(event -> {
            canDrawLine = checkIntersectWithNodes(event.getX(), event.getY());
            if(canDrawLine) {
                start = end = new Point2D(event.getX(), event.getY());
            }
        });
        mainCanvas.setOnMouseDragged(event -> {
            if (canDrawLine) {
                end = new Point2D(event.getX(), event.getY());
            }
        });
        mainCanvas.setOnMouseReleased(event -> {
            if (canDrawLine) {
                if(checkIntersectWithNodes(event.getX(), event.getY()))
                    end = new Point2D(event.getX(), event.getY());
                else
                    start = end = null;
            }
        });

    }

    private void initGameLoop() {
        gameloop = new AnimationTimer() {
            long past = 0;

            // this function only called 30 times per second (~~ 30 FPS)
            private void realHandler(long now) {
                graphics.save();
                graphics.setFill(Color.AZURE);

                graphics.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
                graphics.setFill(Color.BLACK);
                graphics.fillText("" + 1000_000_000 / (now - past), 0, 10);
                graphics.restore();

                update();
                render();

                past = now;
            }

            @Override
            public void handle(long now) {
                // because this is called so rapidly, try to reduce it.
                if (now - past > 27_000_000) {
                    realHandler(now);
                }
            }
        };

        gameloop.start();
    }

    private boolean checkIntersectWithNodes(double x, double y) {
        if (circles != null) {
            for (Point2D circle : circles) {
                if(circle.distance(x, y) < nodeRadius)
                    return true;
            }
        }
        return false;
    }

    // prepare objects to be drawn on the canvas
    private void update() {
        if (circles == null && n_neurons != 0) {
            circles = new Point2D[n_neurons];
            Random rand = new Random();
            for (int i = 0; i < n_neurons; i++) {
                circles[i] = new Point2D(rand.nextInt((int) mainCanvas.getWidth()), rand.nextInt((int) mainCanvas.getHeight()));
            }
        }
    }

    // draw to the canvas
    private void render() {
        if (circles != null) {
            for (Point2D circle : circles) {
                graphics.strokeOval(circle.getX() - nodeRadius, circle.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);
            }
        }
        if (start != null && end != null)
            graphics.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @FXML
    private void showNewBrainDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/BrainGame/GUI/fxml/newDialog.fxml"));
        Parent parent = loader.load();

        NewDialogController controller = loader.getController();
        controller.addHandler(this::newBrainHandler);

        Scene scene = new Scene(parent, 400, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.setTitle("New");
        stage.showAndWait();
    }

    // TODO: better implementation
    private void newBrainHandler(NewDialogHandler.Result result) {
        n_neurons = result.getNumberOfNodes();
        circles = null;
    }

    // TODO: add implementation
    @FXML
    private void editConnection(ActionEvent actionEvent) {
    }

    // TODO: add implementation
    @FXML
    private void sendMessages(ActionEvent actionEvent) {
    }
}
