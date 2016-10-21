/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author George
 */
public class Exercise {

    Text text;
    String calculation;

    public Exercise(String calculation) {
        this.calculation = calculation;
        text = new Text();
        text.setText(calculation);
        text.setFont(Font.font(null, FontWeight.BOLD, 36));
        text.setFill(Color.YELLOW);
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public void setPositionX(double positionX) {
        text.setX(positionX);
    }

    public void setPositionY(double positionY) {
        text.setY(positionY);
    }

}
