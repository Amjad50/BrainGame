package BrainGame.GUI.Controllers;

import BrainGame.handlers.NewDialogHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NewDialogController {

    @FXML
    private TextField neurons_n;
    private NewDialogHandler handler;

    {
        handler = null;
    }


    void addHandler(NewDialogHandler handler) {
        this.handler = handler;
    }


    @FXML
    private void sendData(ActionEvent actionEvent) {
        if(handler != null) {
            closeStage(actionEvent);
            String data = neurons_n.getCharacters().toString();
            int result;
            try {
                result = Integer.parseInt(data);
                handler.handle(new NewDialogHandler.Result(result));
            }catch (NumberFormatException e) {
                Logger.getAnonymousLogger().log(Level.SEVERE, "Could not convert to integer", e);
                handler.handle(new NewDialogHandler.Result(0));
            }
        }
    }

    @FXML
    private void closeStage(ActionEvent actionEvent) {
        Platform.runLater(() -> ((Stage)((Node)actionEvent.getSource()).getScene().getWindow()).close());
    }
}
