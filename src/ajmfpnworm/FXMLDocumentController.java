/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajmfpnworm;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author murp06
 */
public class FXMLDocumentController extends Switchable implements Initializable, sceneInterface {

    @FXML
    private AnchorPane wormPane;
    @FXML
    Label restart, scoreLabel, highscore;

    Pane t;
    protected Timeline timeline;
    int tick = 0, score = 0;

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        if ((event.getCode() == KeyCode.R)) {
            startWorm(2);
        } 
        else if(event.getCode() == KeyCode.ESCAPE){
            Switchable.switchTo("MainMenu");
        }else {
            worm.handleDirect(event, tick);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        wormPane.setBackground(new Background(new BackgroundFill(Color.web("#000000"), CornerRadii.EMPTY, Insets.EMPTY)));
        restart.setText(restart.getText()+"\nPress (Esc) to return to Main Menu.");
        
        startTimeline();
        startWorm(1);
        getHighScore();
    }

    public void newDot() {
        ImageView dot = new ImageView(new Image("ajmfpnworm/blue.png", 18, 18, false, false));
        wormPane.getChildren().add(dot);
        worm.setDot(dot);
        worm.getDot().setX(worm.dotX());
        worm.getDot().setY(worm.dotY());
    }

    public void addWorm(int i) {
        ImageView temp = new ImageView(worm.getImage());
        if (i == 1) {
            worm.addView(temp);
        } else {
            worm.addView2(temp);
        }

        int x2 = worm.getx2(true);
        int y2 = worm.gety2(true);

        worm.addCoord(new Point(x2, y2));
        temp.setX(x2);
        temp.setY(y2);
        wormPane.getChildren().add(temp);
    }

    public void startTimeline() {
        timeline = new Timeline(new KeyFrame(Duration.millis(100), (ActionEvent actionEvent) -> {
            boolean play = worm.getFail();
            int pX = worm.getpX(), pY = worm.getpY();
            if (play) {
                for (int i = worm.getSize() - 1; i >= 0; i--) {
                    int px1 = worm.getx1(i), py1 = worm.gety1(i);
                    worm.getView(i).setX(pX);
                    worm.getView(i).setY(pY);
                    worm.setTick(tick);
                    
                    pX = px1;
                    pY = py1;
                    
                    worm.setCoord(i);
                }
            } else {
                getHighScore();
                restart.toFront();
                restart.setVisible(true);
                timeline.pause();
            }
            if (worm.isDot()) {
                score += 15;
                addWorm(2);
                
                worm.getDot().setX(worm.dotX());
                worm.getDot().setY(worm.dotY());
            }
            setScore(score);
            tick++;
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void setScore(int score) {
        scoreLabel.setText(String.format("%04d", this.score));
    }

    public void getHighScore() {
        try {
            File a = new File("ajmfpnworm/saved");
            if (!a.exists()) {
                a.mkdirs();
            }

            a = new File("ajmfpnworm/saved/highscore.txt");
            if (!a.exists()) {
                a.createNewFile();
                FileWriter fstream = new FileWriter("ajmfpnworm/saved/highscore.txt");
                try (BufferedWriter out = new BufferedWriter(fstream)) {
                    out.write(String.format("%04d", this.score));
                    out.close();
                }
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File("ajmfpnworm/saved/highscore.txt")));
                String line = reader.readLine();
                if (score > Integer.parseInt(line)) {
                    FileWriter fstream = new FileWriter("ajmfpnworm/saved/highscore.txt");
                    try (BufferedWriter out = new BufferedWriter(fstream)) {
                        out.write(String.format("%04d", this.score));
                        out.close();
                    }
                    highscore.setText(String.format("%04d", this.score));
                } else {
                    highscore.setText(line);
                }
            } catch (IOException e) {
            }

        } catch (IOException e) {
            System.out.println("Failure to detect and write to directory");
        }
    }

    public void startWorm(int x) {
        if (x == 2) {
            getHighScore();
            for (int i = worm.getSize() - 1; i >= 0; i--) {
                wormPane.getChildren().remove(worm.getView(i));
            }
            wormPane.getChildren().remove(worm.getDot());
            score = 0;
        }
        if (timeline.getStatus().toString().equals("PAUSED")) {
            timeline.play();
        }
        restart.setVisible(false);
        worm = new TheWorm();
        worm.startWorm();

        ImageView temp = worm.getImgView();
        worm.addView(temp);
        worm.addCoord(new Point(20, 20));
        wormPane.getChildren().add(temp);

        addWorm(1);
        addWorm(1);
        addWorm(1);
        addWorm(1);
        addWorm(1);

        newDot();
    }

}
