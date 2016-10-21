/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author George
 */
public class Asteroid {

    private Image image;
    private ImageView imageView;
    private int resault;

    public Asteroid(int vysledek) {
        image = new Image("Images/Asteroid.png", 100, 200, true, true);
        imageView = new ImageView(image);
    
        resault = 0;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        if (image != null) {
            this.image = image;
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public double getPositionX() {
        return imageView.getLayoutX();
    }

    public void setPositionX(double positionX) {
        imageView.setX(positionX);
    }

    public double getPositionY() {
        return (int) imageView.getLayoutX();
    }

    public void setPositionY(double positionY) {

        imageView.setY(positionY);
    }

    public int getResault() {
        return resault;
    }

    public void setResault(int resault) {
        this.resault = resault;
    }

    public void setVisible(boolean stav) {
        if (imageView != null) {
            imageView.setVisible(stav);
        }
    }

}
