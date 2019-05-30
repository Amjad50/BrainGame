package BrainGame.GUI.Controllers;

import BrainGame.DistanceTimePair;
import BrainGame.handlers.ConnectDialogHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionDialogController {
    @FXML
    private TextField time;
    @FXML
    private TextField distance;
    private ConnectDialogHandler handler;

    {
        handler = null;
    }


    public void addHandler(ConnectDialogHandler handler) {
        this.handler = handler;
    }


    @FXML
    private void sendData(ActionEvent actionEvent) {
        if (handler != null) {
            closeStage(actionEvent);
            String timeS = time.getCharacters().toString();
            String distanceS = distance.getCharacters().toString();

            try {
                handler.handle(new DistanceTimePair(Integer.parseInt(distanceS), Integer.parseInt(timeS)));
            } catch (NumberFormatException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Could not convert to integer", e);
                handler.handle(new DistanceTimePair(-1, -1));
            }
        }
    }

    @FXML
    private void closeStage(ActionEvent actionEvent) {
        Platform.runLater(() -> ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close());
    }

    @FXML
    private void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            sendData(new ActionEvent(keyEvent.getSource(), keyEvent.getTarget()));
    }
}
