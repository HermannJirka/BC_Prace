/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author George
 */
public class Wait implements Runnable {

    private AsteroidScene asteroidScene;
    private Thread thread;
    private int pom;
    private int time; //my time is between time and 2*time
    Random rn = new Random();
    private boolean zrusitVlaklno = false;

    public Wait(AsteroidScene as, int timeInSecs) {
        this.time = 1000*timeInSecs;
        this.thread = new Thread(this);
        this.asteroidScene = as;
        this.pom = asteroidScene.getSizeOfAsteroidList();
    }
    
    public void zrusVlaklo(){
        zrusitVlaklno = true;
    }

    @Override
    public void run() {
        Random rand = new Random();
        try {
            while (!zrusitVlaklno) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (!zrusitVlaklno)
                        asteroidScene.putAsteroidOnScreen();
                    }
                });
                Thread.sleep(rand.nextInt(time)+time);

            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Wait.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void statrVlakno() {
        thread.start();
    }
}
