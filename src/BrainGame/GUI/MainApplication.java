package BrainGame.GUI;

import BrainGame.GUI.canvas.BrainCanvas;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/BrainGame/GUI/fxml/MainScene.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, root.prefWidth(0), root.prefHeight(0));
        stage.setScene(scene);
        stage.setTitle("Brain Game Simulator");
        stage.sizeToScene();
        //Icon to be changed
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("Icons/tmp.jpg")));
        stage.show();

    }

    public void run() {
        PlatformImpl.startup(() -> {
        });
        Platform.runLater(() -> {
            try {
                start(new Stage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
