/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajmfpnworm;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;

/**
 *
 * @author murp06
 */
public class MainMenuDocumentController extends Switchable implements Initializable, sceneInterface {

    @FXML
    AnchorPane mainPane;
    @FXML
    Label title, sub, aboutLabel;
    @FXML
    MenuButton play;
    @FXML
    Button choose, about;

    protected Timeline timeline;
    String file = "ajmfpnworm/red.jpg";

    int tick = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mainPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
        aboutLabel.setVisible(false);
        setAbout();
        makeDir();

        worm = new TheWorm();
        worm.startWorm();

        ImageView temp = worm.getImgView();
        worm.addView(temp);
        worm.addCoord(new Point(160, 40));

        addWorm();
        addWorm();
        addWorm();
        addWorm();
        addWorm();
        startTimeline();
    }

    public void handleChoose(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(mainPane.getScene().getWindow());
        try {
            file = selectedFile.getPath();
            FileWriter fstream = new FileWriter("ajmfpnworm/saved/state.txt");
            try (BufferedWriter out = new BufferedWriter(fstream)) {
                out.write(file);
            }
        } catch (IOException e) {

        }
        worm.changeImage();
    }

    public void handlePlay(ActionEvent event) {
        Switchable.switchTo("AjmfpnWorm");
    }

    public void handleLevel(ActionEvent event) {
        String level = ((MenuItem) event.getSource()).getText();
        System.out.println(level);
    }

    public void handleAbout(ActionEvent event) {
        if (((Button) event.getSource()).getText().equals("About")) {
            title.setVisible(false);
            sub.setVisible(false);
            play.setVisible(false);
            choose.setVisible(false);
            aboutLabel.setVisible(true);
            about.setText("Back");
        } else {
            title.setVisible(true);
            sub.setVisible(true);
            play.setVisible(true);
            choose.setVisible(true);
            aboutLabel.setVisible(false);
            about.setText("About");
        }
    }

    public void startTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(100),
                (ActionEvent actionEvent) -> {
                    int pX = worm.getpX(), pY = worm.getpY();
                    for (int i = worm.getSize() - 1; i >= 0; i--) {
                        int px1 = worm.getx1(i), py1 = worm.gety1(i);
                        worm.getView(i).setX(pX);
                        worm.getView(i).setY(pY);

                        pX = px1;
                        pY = py1;

                        worm.setCoord(i);
                    }
                    
                    if (worm.square(tick)) {
                        tick = 0;
                    }
                    tick++;
                }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void addWorm() {
        ImageView temp = new ImageView(worm.getImage());
        worm.addView(temp);

        int x2 = worm.getx2(false);
        int y2 = worm.gety2(false);

        worm.addCoord(new Point(x2, y2));
        temp.setX(x2);
        temp.setY(y2);
        mainPane.getChildren().add(temp);
    }

    public void makeDir() {
        try {
            File a = new File("ajmfpnworm/saved");
            if (!a.exists()) {
                a.mkdirs();
            }

            a = new File("ajmfpnworm/saved/state.txt");
            if (!a.exists()) {
                a.createNewFile();
            }

            FileWriter fstream = new FileWriter("ajmfpnworm/saved/state.txt");
            try (BufferedWriter out = new BufferedWriter(fstream)) {
                out.write(file);
            }

        } catch (IOException e) {
            System.out.println("Failure to detect and write to directory");
        }
    }

    public void setAbout() {
        aboutLabel.setText("Coded by Andrew Murphy for CS3330 Final Project\n\n"
                + "About Me: I'm a sophomore who has a great appreciation of classic games\n"
                + "Some of my favorites include: Tetris, Asteroid, Dig-Dug, and Missle Command.\n"
                + "This is the first game I've ever made.\n\n"
                + "This game is snake except its Worm.\n"
                + "Each snake segment is an image that is customizable by the user.\n"
                + "You can even make the image a gif.\n"
                + "The GUI components are customized with a style sheet because JavaFX is ugly.\n"
                + "I also made the conscious choice to feature only dark theme.");
    }
}
