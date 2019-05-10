/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ajmfpnworm;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author murp06
 */
public class TheWorm {

    ArrayList<ImageView> worm;
    ArrayList<Point> coord;
    ImageView dot;

    String file = "ajmfpnworm/red.jpg";

    String direction = "right";
    int y = 0, height, width, size = 19, x = size, x2, y2, time;


    public void startWorm() {
        
        worm = new ArrayList<>();
        coord = new ArrayList<>();

        //coord.add(new Point(0, 0));
        getFile();
    }
    
    public void getFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File("ajmfpnworm/saved/state.txt")));
            String line = reader.readLine();
            if (!line.equals(file)) {
                file = new File(line).toURI().toURL().toExternalForm();
            }
        } catch (IOException e) {
        }
    }

    public int getx2(boolean a) {
        if (a) {
            x2 = x;
        } else {
            x2 = -x;
        }
        return ((int) coord.get((coord.size() - 1)).getX() + x2);
    }

    public int gety2(boolean a) {
        if (a) {
            y2 = y;
        } else {
            y2 = -y;
        }
        return ((int) coord.get((coord.size() - 1)).getY() + y2);
    }

    public void addCoord(Point a) {
        coord.add(a);
    }

    public void addView(ImageView a) {
        worm.add(a);
    }
    
    public void addView2(ImageView a) {
        worm.add(0,a);
    }
    

    public Image getImage() {
        return (new Image(file, size - 1, size - 1, false, false));
    }

    public void setDirect(String direction) {
        switch (direction) {
            case "right":
                x = size;
                y = 0;
                break;
            case "left":
                x = -size;
                y = 0;
                break;
            case "up":
                x = 0;
                y = -size;
                break;
            case "down":
                x = 0;
                y = size;
                break;
        }
        this.direction = direction;
    }

    public boolean getFail() {
        int pX = getpX(), pY = getpY();
        boolean play = true;
        if (pX < 0 || pY < 0 || pX > 590 || pY > 390) {
            play = false;
        } else {
            for (int i = worm.size() - 2; i > 0; i--) {
                if (worm.get(i).getX() == pX && worm.get(i).getY() == pY) {
                    play = false;
                }
            }
        }

        return (play);
    }

    public void handleDirect(KeyEvent event, int tick) {
        if (time !=1) {
            if ((event.getCode() == KeyCode.RIGHT||event.getCode() == KeyCode.D) && (!"left".equals(getDirect()))) {
                setDirect("right");
            } else if ((event.getCode() == KeyCode.LEFT||event.getCode() == KeyCode.A) && (!"right".equals(getDirect()))) {
                setDirect("left");
            } else if ((event.getCode() == KeyCode.UP||event.getCode() == KeyCode.W) && (!"down".equals(getDirect()))) {
                setDirect("up");
            } else if ((event.getCode() == KeyCode.DOWN||event.getCode() == KeyCode.S) && (!"up".equals(getDirect()))) {
                setDirect("down");
            } else if (event.getCode() == KeyCode.A) {
                System.out.println(getSize());
            }
            time=1;
        }
    }
    
    public Boolean square(int tick) {
        switch (tick) {
            case 22:
                setDirect("down");
                break;
            case 37:
                setDirect("left");
                break;
            case 59:
                setDirect("up");
                break;
            case 74:
                setDirect("right");
                return (true);
        }
        return(false);
    }
    
    public void changeImage(){
        getFile();
        for (int i = getSize() - 1; i >= 0; i--) {
            getView(i).setImage(new Image(file, size - 1, size - 1, false, false));
        }
    }
    
    public boolean isDot(){
        if ((getView(getSize() - 1).getX() == dot.getX())&&(getView(getSize() - 1).getY() == dot.getY())) {
            return (true);
        }
        return (false);
    }
    
    public int dotX(){
        return((int)(Math.random() * (30) + 1)*19+1);
    }
    
    public int dotY(){
        return((int)(Math.random() * (20) + 1)*19+1);
    }
    
    public ImageView getDot(){
        return(dot);
    }
    
    public ImageView setDot(ImageView dot){
        this.dot=dot;
        return(dot);
    }

    public void setCoord(int i) {
        coord.get(i).setLocation(worm.get(i).getX(), worm.get(i).getY());
    }

    public int getSize() {
        return (worm.size());
    }

    public int getpX() {
        return ((int) worm.get(worm.size() - 1).getX() + x);
    }

    public int getpY() {
        return ((int) worm.get(worm.size() - 1).getY() + y);
    }

    public int getx1(int i) {
        return ((int) worm.get(i).getX());
    }

    public int gety1(int i) {
        return ((int) worm.get(i).getY());
    }

    public ImageView getView(int i) {
        return (worm.get(i));
    }

    public String getDirect() {
        return (direction);
    }

    public ImageView getImgView() {
        return (new ImageView(new Image(file, size - 1, size - 1, false, false)));
    }
    
    public void setTick(int tick){
        time=0;
    }
}
