package BrainGame.GUI.canvas;

import BrainGame.Brain;
import BrainGame.DistanceTimePair;
import BrainGame.GUI.Controllers.ConnectionDialogController;
import BrainGame.GUI.MainApplication;
import BrainGame.Path;
import BrainGame.Search;
import BrainGame.handlers.ConnectDialogHandler;
import BrainGame.handlers.ModeHandler;
import BrainGame.tools.AlreadyConnectedException;
import BrainGame.tools.NoConnectionException;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BrainCanvas {

    private Brain brain;

    private static final double nodeRadius = 25;
    private static final String DistanceTimeFormat = "%s, %s";

    private AnimationTimer gameloop;
    private Canvas mainCanvas;
    private GraphicsContext graphics;
    private boolean Darkmode = false;

    private int n_neurons;

    private Point2D[] circles = null;
    private ArrayList<Point2D> connections;
    private ArrayList<Point2D> correctConnections;
    private DistanceTimePair best;

    //used to indicate that we can draw or move or delete (any action)
    private boolean canContinueAction = false;
    private Point2D start, end;
    private int startCircle, endCircle;

    private HashMap<EditMode, ModeHandler> handlersMap;
    private ModeHandler editHandler;
    private EditMode currentMode;

    public BrainCanvas(Pane container) {
        // dangerous code (VERY)
        mainCanvas = (Canvas) container.getChildren().get(0);

        mainCanvas.widthProperty().bind(container.widthProperty());
        mainCanvas.heightProperty().bind(container.heightProperty());

        graphics = mainCanvas.getGraphicsContext2D();

        connections = new ArrayList<>();
        correctConnections = new ArrayList<>();

        initClickListeners();
        initGameLoop();
        initModeHandlers();
    }

    private void initModeHandlers() {
        handlersMap = new HashMap<>();

        handlersMap.put(EditMode.MOVE, this::moveHandler);
        handlersMap.put(EditMode.CONNECT, this::connectHandler);
        handlersMap.put(EditMode.DISCONNECT, this::disconnectHandler);
        handlersMap.put(EditMode.EDIT, this::editConnectionHandler);
        handlersMap.put(EditMode.SEND, this::sendMessageHandler);
        handlersMap.put(EditMode.NOTHING, (x, y, mode) -> {
        });
        editHandler = (x, y, mode) -> {
        };
    }

    private void initClickListeners() {
        mainCanvas.setOnMousePressed(event -> editHandler.handle(event.getX(), event.getY(), ClickMode.START));
        mainCanvas.setOnMouseDragged(event -> editHandler.handle(event.getX(), event.getY(), ClickMode.DURING));
        mainCanvas.setOnMouseReleased(event -> editHandler.handle(event.getX(), event.getY(), ClickMode.END));

    }

    private int getIntersection(double x, double y) {
        if (circles != null) {
            for (int i = 0; i < circles.length; i++) {
                if (circles[i].distance(x, y) < nodeRadius) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isOverLap(double x, double y) {
        if (circles != null) {
            for (Point2D circle : circles) {
                try {
                    if (circle.distance(x, y) < nodeRadius * 2) {
                        return true;
                    }
                } catch (NullPointerException e) {
                    return false;
                }
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
                double x, y;
                // to generate non overlapping circles
                do {
                    x = rand.nextInt((int) mainCanvas.getWidth() - (int) nodeRadius * 4) + nodeRadius * 2;
                    y = rand.nextInt((int) mainCanvas.getHeight() - (int) nodeRadius * 4) + nodeRadius * 2;
                } while (isOverLap(x, y));

                circles[i] = new Point2D(x, y);
            }
        }
    }

    // draw to the canvas
    private void render() {
        graphics.save();

        // set stroke width and line width to 5
        graphics.setLineWidth(5);
        graphics.setLineCap(StrokeLineCap.ROUND);
        // check if there are any nodes to draw
        if (circles != null) {
            // draw the connection lines first (to be on bottom)
            if (Darkmode) {
                graphics.setStroke(Color.rgb(117, 56, 101));
            } else {
                graphics.setStroke(Color.rgb(1, 0, 0, 0.88));
            }
            for (Point2D connection : connections) {
                Point2D first = circles[(int) connection.getX()],
                        second = circles[(int) connection.getY()];
                graphics.strokeLine(first.getX(), first.getY(), second.getX(), second.getY());
            }

            // if there is a path, draw it with the best time info
            if (!correctConnections.isEmpty()) {
                graphics.save();

                // draw the time and distance info
                if (Darkmode) {
                    graphics.setFill(Color.rgb(176, 105, 11));
                } else {
                    graphics.setFill(Color.rgb(12, 2, 1, 0.8));
                }

                graphics.setFont(Font.font("Tahoma", FontWeight.LIGHT, 16));
                graphics.setTextBaseline(VPos.TOP);
                if (best != null) {
                    String s = String.format("Best Path: Distance = %s, Time = %s, using %d path(s)", best.getDistance(), best.getTime(), correctConnections.size());
                    graphics.fillText(s, nodeRadius / 2, nodeRadius / 2);
                }

                if (Darkmode) {
                    graphics.setStroke(Color.AZURE);
                } else {
                    graphics.setStroke(Color.BURLYWOOD);
                }
                // make it thicker to go over the lines on the bottom
                graphics.setLineWidth(6);
                for (Point2D connection : correctConnections) {
                    Point2D first = circles[(int) connection.getX()],
                            second = circles[(int) connection.getY()];
                    graphics.strokeLine(first.getX(), first.getY(), second.getX(), second.getY());
                }
                graphics.restore();
            }

            // if we are currently drawing lines, show them
            if (currentMode == EditMode.CONNECT) {
                if (start != null && end != null) {
                    graphics.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
                }
            }

            // draw the distance and time information
            for (Point2D connection : connections) {
                // get info
                int firstI = (int) connection.getX(),
                        secondI = (int) connection.getY();
                Point2D first = circles[firstI],
                        second = circles[secondI];
                DistanceTimePair pair = brain.getNeuron(firstI).children.get(brain.getNeuron(secondI));
                Point2D midpoint = first.midpoint(second);

                // draw
                graphics.setTextBaseline(VPos.BOTTOM);
                graphics.setTextAlign(TextAlignment.CENTER);
                if (Darkmode) {
                    graphics.setFill(Color.rgb(191, 187, 190));
                } else {
                    graphics.setFill(Color.rgb(1, 0, 0, 0.7));
                }
                graphics.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                graphics.fillText(String.format(DistanceTimeFormat, pair.getDistance(), pair.getTime()), midpoint.getX(), midpoint.getY());
            }

            // start drawing nodes circles
            int counter = 0;
            for (int i = 0; i < circles.length; i++) {
                // get the circle to draw now
                Point2D circle = circles[i];

                // set the insde body color and draw
                if (Darkmode) {
                    graphics.setFill(Color.rgb(238, 159, 46, 0.75));
                } else {
                    graphics.setFill(Color.rgb(220, 168, 53, 0.75));
                }

                graphics.fillOval(circle.getX() - nodeRadius, circle.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                // set the color of the outer stroke (based on the mode)
                // default color:
                if (Darkmode) {
                    graphics.setStroke(Color.rgb(238, 159, 46));

                } else {
                    graphics.setStroke(Color.rgb(220, 168, 53, 0.85));
                }

                if (canContinueAction && i == startCircle) {
                    if (currentMode == EditMode.MOVE) {
                        graphics.setStroke(Color.rgb(29, 5, 5));
                    }
                    if (currentMode == EditMode.DISCONNECT) {
                        graphics.setStroke(Color.rgb(137, 6, 6));
                    }
                    if (currentMode == EditMode.EDIT) {
                        graphics.setStroke(Color.rgb(33, 96, 35));
                    }
                    if (currentMode == EditMode.SEND) {
                        graphics.setStroke(Color.rgb(171, 160, 181));
                    }
                }
                graphics.strokeOval(circle.getX() - nodeRadius, circle.getY() - nodeRadius, nodeRadius * 2, nodeRadius * 2);

                // set stroke color(color of text also) and text settings to draw the number of the node
                graphics.setTextAlign(TextAlignment.CENTER);
                graphics.setTextBaseline(VPos.CENTER);
                graphics.setFont(Font.font(null, FontWeight.EXTRA_BOLD, 23));
                graphics.setFill(Color.rgb(1, 0, 0, 0.65));
                graphics.fillText("" + counter++, circle.getX(), circle.getY());
            }
        }

        graphics.restore();
    }

    private void connectHandler(double x, double y, ClickMode mode) {
        int node;
        switch (mode) {
            case START:
                node = getIntersection(x, y);
                canContinueAction = node != -1;
                if (canContinueAction) {
                    start = circles[node];
                    startCircle = node;
                    end = new Point2D(x, y);
                }
                break;
            case DURING:
                if (canContinueAction) {
                    end = new Point2D(x, y);
                }
                break;
            case END:
                if (canContinueAction) {
                    canContinueAction = false;
                    node = getIntersection(x, y);
                    if (node != -1) {
                        end = circles[node];
                        endCircle = node;
                        if (endCircle != startCircle) {
                            connectPopup(pair -> {
                                try {
                                    if (pair.getTime() > 0 && pair.getDistance() > 0) {
                                        brain.connect(startCircle, endCircle, pair.getDistance(), pair.getTime());
                                        connections.add(new Point2D(startCircle, endCircle));
                                    } else {
                                        Alert alertdialog = new Alert(Alert.AlertType.ERROR);
                                        alertdialog.setTitle("Connection Error");
                                        alertdialog.setHeaderText("Oops, you cannot create a connection with non or negative values");
                                        alertdialog.setContentText("Please specify correct non-negative values");
                                        alertdialog.showAndWait();
                                    }
                                } catch (AlreadyConnectedException e) {
                                    Alert alertdialog = new Alert(Alert.AlertType.ERROR);
                                    alertdialog.setTitle("Connection Error");
                                    alertdialog.setHeaderText("Oops, you cannot add connection on top of another. Use edit connection");
                                    alertdialog.setContentText("Cannot connect these two nodes");
                                    alertdialog.showAndWait();
                                }
                            });
                        }
                        start = end = null;
                        startCircle = endCircle = -1;
                    } else {
                        start = end = null;
                    }
                }

        }
    }

    private void connectPopup(ConnectDialogHandler handler) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/BrainGame/GUI/fxml/connectDialog.fxml"));
        Parent parent;
        try {
            parent = loader.load();
            ConnectionDialogController controller = loader.getController();
            controller.addHandler(handler);

            Scene scene = new Scene(parent, 500, 200);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setTitle("Connect");
            //Icon to be edited
            stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("Icons/tmp.jpg")));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void moveHandler(double x, double y, ClickMode mode) {
        int node;
        switch (mode) {
            case START:
                if ((node = getIntersection(x, y)) != -1) {
                    canContinueAction = true;
                    startCircle = node;
                }
                break;
            case DURING:
                if (canContinueAction) {
                    circles[startCircle] = new Point2D(x, y);
                }
                break;
            case END:
                if (canContinueAction) {
                    canContinueAction = false;
                }
        }
    }

    private void disconnectHandler(double x, double y, ClickMode mode) {
        int node;
        switch (mode) {
            case START:
                if ((node = getIntersection(x, y)) != -1) {
                    // this is the end node
                    if (canContinueAction) {
                        endCircle = node;
                    } else {
                        // first node
                        startCircle = node;
                    }
                }
                break;
            case DURING:
                break;
            case END:
                // now is the end node
                if (canContinueAction && endCircle != -1) {
                    canContinueAction = false;
                    if (startCircle != endCircle) {
                        try {
                            brain.removeConnection(startCircle, endCircle);
                            connections.remove(new Point2D(startCircle, endCircle));
                            connections.remove(new Point2D(endCircle, startCircle));
                        } catch (NoConnectionException e) {
                            Alert alertdialog = new Alert(Alert.AlertType.ERROR);
                            alertdialog.setTitle("Disconnection Error");
                            alertdialog.setHeaderText("Oops, you cannot remove a connection that is not there.");
                            alertdialog.setContentText("Cannot disconnect these two nodes");
                            alertdialog.showAndWait();
                        }
                    }
                    startCircle = endCircle = -1;
                } else {
                    // start node end selection
                    canContinueAction = true;
                }
        }
    }

    private void editConnectionHandler(double x, double y, ClickMode mode) {
        int node;
        switch (mode) {
            case START:
                if ((node = getIntersection(x, y)) != -1) {
                    // this is the end node
                    if (canContinueAction) {
                        endCircle = node;
                    } else {
                        // first node
                        startCircle = node;
                    }
                }
                break;
            case DURING:
                break;
            case END:
                // now is the end node
                if (canContinueAction && endCircle != -1) {
                    canContinueAction = false;
                    if (startCircle != endCircle) {
                        if (brain.checkConnection(startCircle, endCircle)) {
                            connectPopup(pair -> {
                                if (pair.getTime() > 0 && pair.getTime() > 0) {
                                    try {
                                        brain.editConnection(startCircle, endCircle, pair.getDistance(), pair.getTime());
                                    } catch (NoConnectionException e) {
                                        Alert alertdialog = new Alert(Alert.AlertType.ERROR);
                                        alertdialog.setTitle("Edition Error");
                                        alertdialog.setHeaderText("Oops, you cannot edit a non connected nodes. Please add connection.");
                                        alertdialog.setContentText("Cannot edit connection between these two nodes");
                                        alertdialog.showAndWait();
                                    }
                                }
                            });
                        } else {
                            Alert alertdialog = new Alert(Alert.AlertType.ERROR);
                            alertdialog.setTitle("Edition Error");
                            alertdialog.setHeaderText("Oops, you cannot edit a non connected nodes. Please add connection.");
                            alertdialog.setContentText("Cannot edit connection between these two nodes");
                            alertdialog.showAndWait();
                        }
                    }
                    startCircle = endCircle = -1;
                } else {
                    // start node end selection
                    canContinueAction = true;
                }
        }
    }

    private void sendMessageHandler(double x, double y, ClickMode mode) {
        int node;
        switch (mode) {
            case START:
                if ((node = getIntersection(x, y)) != -1) {
                    // this is the end node
                    if (canContinueAction) {
                        endCircle = node;
                    } else {
                        // first node
                        startCircle = node;
                    }
                }
                break;
            case DURING:
                break;
            case END:
                // now is the end node
                if (canContinueAction && endCircle != -1) {
                    canContinueAction = false;
                    if (startCircle != endCircle) {
                        Path path = Search.search(brain, startCircle, endCircle);
                        best = new DistanceTimePair(path.getDistance(), path.getTime());
                        Brain.Neuron prev = null;
                        for (Brain.Neuron n : path.getPath()) {
                            if (prev != null) {
                                correctConnections.add(new Point2D(prev.id, n.id));
                            }
                            prev = n;
                        }
                    }
                    startCircle = endCircle = -1;
                } else {
                    // start node end selection
                    canContinueAction = true;
                    correctConnections.clear();
                }
        }
    }

    private void initGameLoop() {
        gameloop = new AnimationTimer() {
            long past = 0;

            // this function only called 30 times per second (~~ 30 FPS)
            private void realHandler(long now) {

                graphics.save();
                if (Darkmode) {
                    graphics.setFill(Color.rgb(45, 45, 45, 0.8));
                } else {
                    graphics.setFill(Color.rgb(247, 246, 246));
                }

                graphics.fillRect(0, 0, mainCanvas.getWidth(), mainCanvas.getHeight());
                // FPS
//                graphics.setFill(Color.BLACK);
//                graphics.fillText("" + 1000_000_000 / (now - past), 0, 10);
                graphics.restore();

                update();
                render();

                graphics.save();
                graphics.setFont(new Font("Arial", 12));
                if (Darkmode) {
                    graphics.setFill(Color.rgb(191, 187, 190));
                } else {
                    graphics.setFill(Color.CADETBLUE);
                }

                graphics.fillText(String.format("Format: " + DistanceTimeFormat, "Distance", "Time"), nodeRadius / 2, mainCanvas.getHeight() - nodeRadius / 2);
                graphics.restore();

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
    }

    public void startGraphicsLoop() {
        gameloop.start();
    }

    public void newBrain(int numberOfNodes) {
        n_neurons = numberOfNodes;
        brain = new Brain(n_neurons);
        circles = null;
        correctConnections.clear();
        connections.clear();
        startCircle = endCircle = -1;
        start = end = null;
        canContinueAction = false;
    }

    public void changeMode(String name) {
        currentMode = EditMode.valueOf(name);
        editHandler = handlersMap.get(currentMode);

        //clear variables used in tool modes
        if (currentMode == EditMode.EDIT || currentMode == EditMode.DISCONNECT) {
            correctConnections.clear();
        }
        start = end = null;
        startCircle = endCircle = -1;
        canContinueAction = false;
    }

    public void changedisplay(boolean changemode) {
        if (changemode) {
            Darkmode = true;
        } else {
            Darkmode = false;
        }
    }

}
