package BrainGame.GUI.Controllers;

import BrainGame.GUI.MainApplication;
import BrainGame.GUI.canvas.BrainCanvas;
import BrainGame.handlers.NewDialogHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;

public class MainSceneController {

    @FXML
    private Pane canvasContainer;
    private BrainCanvas brainCanvas;

    @FXML
    private ToggleGroup toolsGroup;
    
    @FXML
    private CheckBox DisplayMode;

    @FXML
    private void initialize() {
        brainCanvas = new BrainCanvas(canvasContainer);
        brainCanvas.startGraphicsLoop();
        initToolBarListener();
        brainCanvas.newBrain(8);
    }

    private void initToolBarListener() {
        toolsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                brainCanvas.changeMode((String) newValue.getUserData());
            else
                brainCanvas.changeMode("NOTHING");
        });
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
        //Icon to be changed
        stage.getIcons().add(new Image(MainApplication.class.getResourceAsStream("Icons/tmp.jpg")));
        stage.showAndWait();
    }

    private void newBrainHandler(NewDialogHandler.Result result) {
        brainCanvas.newBrain(result.getNumberOfNodes());
    }
    
    @FXML
    private void ChangeDisplayMode(){
        if(DisplayMode.isSelected()){
            System.out.println("am checked");  
        }else{
            System.out.println("am not");
        }
    }
}
