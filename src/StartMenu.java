
import BrainGame.GUI.MainApplication;
import com.sun.javafx.application.PlatformImpl;
import java.io.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.application.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.media.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class StartMenu extends Application implements EventHandler<ActionEvent> {

    Stage window;
    Scene scene1;

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        window.setTitle("Brain Game");

        //The sound file playing
        String musicFile = "audio.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
       

        MediaView mediaView = new MediaView(mediaPlayer);

        //Preview Image
        final ImageView prevImage = new ImageView();
        Image image = new Image(new FileInputStream("Brain.png"));
        Image logo = new Image(new FileInputStream("DS-Logo.png"));
        prevImage.setImage(image);

        Button button1 = new Button("Play");
        button1.setOnAction(e -> {
            new MainApplication().run();
            window.hide();
                  });

        Button button2 = new Button("Exit");
        button2.setOnAction(e -> System.exit(0));

        //Sound Button
        Button Soundbutton1 = new Button("\uD83D\uDD0A");
        Soundbutton1.setOnAction(e -> {
            if (mediaPlayer.isMute()) {
                mediaPlayer.setMute(false);
                Soundbutton1.setText("\uD83D\uDD0A");
                mediaPlayer.play();
            } else {
                mediaPlayer.setMute(true);
                Soundbutton1.setText("\uD83D\uDD08");
                mediaPlayer.pause();
            }
        });

        //Buttons' Location 
        button1.setLayoutX(10);
        button1.setLayoutY(460);
        button2.setLayoutX(390);
        button2.setLayoutY(460);
        Soundbutton1.setLayoutX(10);
        Soundbutton1.setLayoutY(10);

        //Buttons' Size 
        button1.setPrefSize(100, 30);
        button2.setPrefSize(100, 30);
        Soundbutton1.setPrefSize(40, 40);

        //Icon
        window.getIcons().add(logo);

        //Creating Pane and adding the the objects
        Pane layout1 = new Pane();

      
        layout1.getChildren().addAll(prevImage, button1, button2, mediaView, Soundbutton1);
        scene1 = new Scene(layout1, 500, 500);
        window.setScene(scene1);
        window.setTitle("Brain Game");
        window.show();

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

    @Override
    public void handle(ActionEvent t) {
    }

}
