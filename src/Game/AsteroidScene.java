/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 *
 * @author George
 */
public class AsteroidScene {
    /* hlavni graficke komponenty */

    private Group mainGroup;
    private Scene scene;
    private Stage stage;
    private Label numOfAsteroids;
    private Label numOfLifes;
    private final BorderPane borderLayout;

    Wait thread = null;
//    private Pane panelCenter;
    private Pane centerPanel;
    /* seznamy pouzitych objektu */
    private final ArrayList<Path> listOfPaths;
    private final ArrayList<PathTransition> listOfTravelingAst;
    private final ArrayList<PathTransition> listOfTravelingText;
    private final ArrayList<Asteroid> listOfAsteroids;
    private final ArrayList<Exercise> listOfExercises;
    private final ArrayList<RotateTransition> listOfRotates;
    private final ArrayList<Boolean> listOfLevels;
    /* promenne */
    private int lives;
    private final int tempResault = 0;
    private int sizeOfAsteroids;
    private int temp = 0;
    private Exercise actualText;
    private Asteroid actualAsteroid;
    private Point actPoint;
    private int actIndex;
    /**/
    private static final Image IMAGE = new Image("Images/explode_1.png");
    // vytvorit promenou scenu zde

    private static final int COLUMNS = 4;
    private static final int COUNT = 16;
    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 5;
    private static final int WIDTH = 66;
    private static final int HEIGHT = 66;

    public AsteroidScene() {
        this.listOfPaths = new ArrayList<>();
        this.listOfAsteroids = new ArrayList<>();
        this.listOfExercises = new ArrayList<>();
        this.listOfLevels = new ArrayList<>();;
        this.listOfRotates = new ArrayList<>();
        this.listOfTravelingAst = new ArrayList<>();
        this.listOfTravelingText = new ArrayList<>();
        this.lives = 3;

        mainGroup = new Group();
        borderLayout = new BorderPane();
        actualText = new Exercise(null);
    }

    public void init(Stage primaryStage) {
        Group tempGroup = new Group();
        this.stage = primaryStage;

        scene = new Scene(borderLayout, 800, 600, Color.WHITE);

        primaryStage.setScene(scene);
        // primaryStage.setFullScreen(true);
        //        Pane panel = new Pane();
        centerPanel = new Pane();

        // centerPanel.setStyle("-fx-background-color: blue;");
        // centerPanel.setStyle("-fx-background-image: url('splash.jpg');");
        centerPanel.setStyle("-fx-background-image: url('Images/universe.jpg');");
        centerPanel.getChildren().add(mainGroup);

        borderLayout.prefWidthProperty().bind(scene.widthProperty());
        borderLayout.prefHeightProperty().bind(scene.heightProperty());

        borderLayout.setTop(addMenu());
        borderLayout.setCenter(centerPanel);
        borderLayout.setBottom(addHBox());

        primaryStage.show();

//        borderPane.setPrefSize(scene.getWidth(), scene.getHeight());
//        generateAsteroids(10);
//        generateExcersises(10);
//        generateJourney(10);
//        generateObjects(12 , 3);
//        putAsteroidOnScreen();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent t) {
                JOptionPane.showMessageDialog(null, "Aplikace zavira");
                Platform.exit();
                System.exit(0);
            }
        });

    }

    /**
     *
     * @param a Nejmenší doba letu asteroidu.
     * @param b Nejmenší doba mezi přílety asteroidu.
     * @param c Rozdíly v letu ateroidu.
     */
    private void generateObjects(int a, int b, int c) {
        Random rand = new Random();
        int[] pole = new int[getSizeOfAsteroidList()];
        for (int i = 0; i < pole.length; i++) {
            pole[i] = c * rand.nextInt(a) + a;
            //Když C = 0, tak letí všechny asteroidy stejně rychle, při zvyšování C se rozdíly zvyšují
        }
// DO NOT EDIT
        generateTransitionAsteroid(pole);
        generateTransitionExcersise(pole);
        thread = new Wait(this, b);
        thread.statrVlakno();
    }

    private HBox addHBox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

//        Button buttonCurrent = new Button("Current");
//        buttonCurrent.setPrefSize(100, 20);
//
//        Button buttonProjected = new Button("Projected");
//        buttonProjected.setPrefSize(100, 20);
//        hBox.getChildren().addAll(buttonCurrent, buttonProjected);
        return hBox;

    }

    private MenuBar addMenu() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");

        MenuItem continueC = new MenuItem("Continue");

        MenuItem add = new MenuItem("New Game");
        add.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                //TODO CLEAR
                clear();
                generateAsteroids(10);
                generateExcersises(10);
                generateJourney(10);
                generateObjects(12, 3, 3);
                setLives();
            }

        });

        MenuItem pause = new MenuItem("Pause");
        pause.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        pause.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                for (int i = 0; i < getSizeOfAsteroidList(); i++) {
                    listOfTravelingAst.get(i).pause();
                    listOfTravelingText.get(i).pause();
                    listOfRotates.get(i).pause();
                }
            }
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                JOptionPane.showMessageDialog(null, "Aplikace zavira");
                System.exit(0);
            }
        });

        menuFile.getItems().addAll(add, pause, exit);
        menuBar.getMenus().add(menuFile);
        return menuBar;
    }

    private void clear() {
        if (thread != null) {
            thread.zrusVlaklo();
        }
        listOfAsteroids.clear();
        listOfExercises.clear();
        listOfPaths.clear();
        listOfRotates.clear();
        listOfTravelingAst.clear();
        listOfTravelingAst.clear();
        listOfTravelingText.clear();
        actIndex = -1;
        actualAsteroid = null;
        actualText = null;
        mainGroup.getChildren().clear();

    }

    private void generateAsteroids(int number) {
        Asteroid as;
        ImageView iv;
        int resault = 0;
        for (int i = 0; i < number; i++) {
            as = new Asteroid(0);
            listOfAsteroids.add(as);
            //mainGroup.getChildren().add(as.getImageView());
        }
    }

    private void generateExcersises(int number) {
        Exercise ex;
        int pom;
        String exercise;
        for (int i = 0; i < number; i++) {
            exercise = generateExercise();
            ex = new Exercise(exercise);
            listOfExercises.add(ex);
            pom = this.sizeOfAsteroids;
            pom = 0;
            listOfAsteroids.get(i).setResault(this.sizeOfAsteroids);
            // mainGroup.getChildren().add(ex.getText());
        }
    }

    private String generateExercise() {
        String example = null;

        int operator1 = 0, operator2 = 0;
        int random = 0;
        operator1 = (int) (Math.random() * 10) + 1;
        operator2 = (int) (Math.random() * 10) + 1;
        random = (int) (Math.random() * 2);

        switch (random) {
            case 0:
                example = operator1 + "+" + operator2;
                sizeOfAsteroids = operator1 + operator2;
                break;
            case 1:
                if (operator1 < operator2) {
                    example = operator2 + "-" + operator1;
                    sizeOfAsteroids = operator1 - operator2;
                } else {
                    example = operator1 + "-" + operator2;
                    sizeOfAsteroids = operator1 - operator2;
                }
                break;
            case 3:
                example = operator1 + "/" + operator2;
                break;
            case 2:
                example = operator1 + "*" + operator2;
                sizeOfAsteroids = operator1 * operator2;
                break;
        }
        return example;
    }

    private void generateJourney(int number) {
        Random rand = new Random();

        Path p;
        double positionX = 0, positionY = 0;
        double rn;
        for (int i = 0; i < number; i++) {
            rn = Math.random() * 5;
            p = new Path();
            positionX = (Math.random() * centerPanel.getWidth() - 250) + 250;
            positionX = rand.nextInt((int) (borderLayout.getWidth() - 50)) + 25;

            listOfAsteroids.get(i).setPositionX(positionX);
            listOfAsteroids.get(i).setPositionY(50);
            listOfExercises.get(i).setPositionX(positionX);
            listOfExercises.get(i).setPositionY(50);

            p.getElements().add(new MoveTo(positionX, 65));
//            if (rn > 3) {
            p.getElements().add(new LineTo(rand.nextInt((int) (centerPanel.getWidth() - 50)) + 25, borderLayout.getHeight()));
//            } else {
//                p.getElements().add(new LineTo(positionX - 150, borderLayout.getHeight()));
//            }

            listOfPaths.add(p);
        }
    }

    private RotateTransition generateRotateTransition(int number) {
        RotateTransition rt = null;
        int rn = 0;
        for (int i = 0; i < number; i++) {
            rn = (int) (Math.random() * 4) + 3;
            rt = new RotateTransition(Duration.millis(5), listOfAsteroids.get(i).getImageView());
            rt.setFromAngle(10);
            rt.setToAngle(170);

        }
        return rt;
    }

    private void generateTransitionAsteroid(int[] delay) {
        Random rand = new Random();
        PathTransition pathTrAsteroid;
        RotateTransition rt;
        int x = 0, y = 0;

        if (listOfAsteroids.size() == 0) {
            System.err.println("Prazdno");
        } else {
            for (int i = 0; i < listOfAsteroids.size(); i++) {
                pathTrAsteroid = generateAsteroidTransition(listOfAsteroids.get(i).getImageView(), Duration.seconds(delay[i]), Duration.seconds(0), listOfPaths.get(i));
                rt = new RotateTransition();
                rt.setFromAngle(10);
                rt.setToAngle(190);
                rt.setNode(listOfAsteroids.get(i).getImageView());
                rt.setDuration(Duration.seconds(30));
                rt.setCycleCount(1);
                listOfTravelingAst.add(pathTrAsteroid);
                listOfRotates.add(rt);
                pathTrAsteroid.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        PathTransition pt = (PathTransition) t.getSource();
                        Node nd = pt.getNode();
                        mainGroup.getChildren().removeAll(nd);

                        final ImageView iv = new ImageView(IMAGE);
                        iv.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
                        final Animation animation = new SpriteAnimation(iv, COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT, Duration.millis(1000));
                        animation.setCycleCount(1);
                        animation.play();
                        mainGroup.getChildren().add(iv);
                        System.out.println("konec");
                        if (lives > 0) {
                            if (pt == listOfTravelingAst.get(getSizeOfAsteroidList() - 1)) {
                                setLevelOnScreen();
                            }
                        }
                    }
                });
            }

        }
    }

    private void generateTransitionExcersise(int[] delay) {
        PathTransition pathTrText;
        if (listOfExercises.size() == 0) {
            System.out.println("nelze");
        } else {
            for (int i = 0; i < listOfExercises.size(); i++) {

                pathTrText = generateAsteroidTransition(listOfExercises.get(i).getText(), Duration.seconds(delay[i]), Duration.seconds(0), listOfPaths.get(i));

                listOfTravelingText.add(pathTrText);

                pathTrText.setOnFinished(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent t) {
                        PathTransition pt = (PathTransition) t.getSource();
                        Node nd = pt.getNode();
                        mainGroup.getChildren().removeAll(nd);

                    }
                });
            }
        }
    }

    private PathTransition generateAsteroidTransition(final Node shape,
            final Duration duration, final Duration delay, final Path path) {

        final PathTransition pt = new PathTransition();
        pt.setDuration(duration);
        pt.setDelay(delay);
        pt.setNode(shape);
        pt.setPath(path);
        pt.setCycleCount(1);
        return pt;
    }

    private void setButtonExercises() {
        int calculation = 0;
        calculation = actualAsteroid.getResault();
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");
        hBox.setAlignment(Pos.CENTER);
        Button buttonCalculation1 = new Button("" + calculation);
        buttonCalculation1.setPrefSize(100, 20);
        buttonCalculation1.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

              

                mainGroup.getChildren().remove(listOfAsteroids.get(actIndex).getImageView());
                mainGroup.getChildren().remove(listOfExercises.get(actIndex).getText());
                final ImageView iv = new ImageView(IMAGE);

                iv.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
                System.out.println(actPoint.x + " : " + actPoint.y);
                iv.setX(actualAsteroid.getImageView().getTranslateX());
                iv.setY(actualAsteroid.getImageView().getTranslateY());
//                System.out.println(actualText.getText());
                final Animation animation = new SpriteAnimation(iv, COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT, Duration.millis(1000));

                animation.setCycleCount(1);
                animation.play();

                mainGroup.getChildren().add(iv);
            }
        });

        Button buttonCalculation2 = new Button("" + (calculation + 5));
        buttonCalculation2.setPrefSize(100, 20);
        buttonCalculation2.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {

                if (actualAsteroid.getResault() + 5 != actualAsteroid.getResault()) {
                    JOptionPane.showMessageDialog(null, "Spatne");
                    lives--;
                    numOfLifes.setText(String.valueOf(lives));
                }
                mainGroup.getChildren().remove(listOfAsteroids.get(actIndex).getImageView());
                mainGroup.getChildren().remove(listOfExercises.get(actIndex).getText());
                mainGroup.getChildren().remove(listOfTravelingAst.get(actIndex));
                final ImageView iv = new ImageView(IMAGE);

                iv.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
//                iv.setX(actPoint.x);
//                iv.setY(actPoint.y);
                final Animation animation = new SpriteAnimation(iv, COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT, Duration.millis(1000));
                animation.setCycleCount(1);
                animation.play();
                mainGroup.getChildren().add(iv);
            }
        });
        Button buttonCalculation3 = new Button("" + (calculation - 5));
        buttonCalculation3.setPrefSize(100, 20);
        buttonCalculation3.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if (actualAsteroid.getResault() + 5 != actualAsteroid.getResault()) {
                    JOptionPane.showMessageDialog(null, "Spatne");
                    lives--;
                    numOfLifes.setText(String.valueOf(lives));
                }

                mainGroup.getChildren().remove(listOfAsteroids.get(actIndex).getImageView());
                mainGroup.getChildren().remove(listOfExercises.get(actIndex).getText());
                mainGroup.getChildren().remove(listOfTravelingAst.get(actIndex));
                final ImageView iv = new ImageView(IMAGE);
                iv.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
//                iv.setX(actPoint.x);
//                iv.setY(actPoint.y);
                final Animation animation = new SpriteAnimation(iv, COUNT, COLUMNS, OFFSET_X, OFFSET_Y, WIDTH, HEIGHT, Duration.millis(1000));
                animation.setCycleCount(1);
                animation.play();
                mainGroup.getChildren().add(iv);
            }
        });
        numOfLifes = new Label(String.valueOf(lives));
        numOfLifes.setFont(new Font("Arial", 24));

        hBox.getChildren().addAll(buttonCalculation1, buttonCalculation2, buttonCalculation3, numOfLifes);
        borderLayout.setBottom(hBox);
    }

    private void mouseEventClicked() {

        Exercise tempEx;
        Text tempText;

        for (int i = 0; i < getSizeOfAsteroidList(); i++) {
            tempEx = listOfExercises.get(i);
            tempText = tempEx.getText();
            tempText.setOnMousePressed(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {

                    Text tempNode = (Text) t.getSource();
                    int index = 0;
                    for (int j = 0; j < getSizeOfAsteroidList(); j++) {
                        if (tempNode == listOfExercises.get(j).getText()) {
                            index = j;
                            break;
                        }
                    }

                    actualAsteroid = listOfAsteroids.get(index);
                    actIndex = index;
                    actualText = listOfExercises.get(index);
                    actPoint = new Point((int) t.getX(), (int) t.getY());
//                    System.out.println(actPoint.toString());
                    //System.out.println(tempNode.toString());
//                    System.out.println(t.toString());
//                    System.out.println(actualAsteroid.getResault());
                    setButtonExercises();
                }

            });

            tempEx.setText(tempText);
            listOfExercises.set(i, tempEx);

        }
    }

    private void levels() {

        generateAsteroids(10);
        generateExcersises(10);
        generateJourney(10);
        generateRotateTransition(10);
//        generateTransitionAsteroid(5, 1);
//        generateTransitionExcersise(5, 1);
    }

    public synchronized void putAsteroidOnScreen() {
        mouseEventClicked();
        if (temp < getSizeOfAsteroidList()) {
            if (lives > 0) {
                mainGroup.getChildren().add(listOfAsteroids.get(temp).getImageView());
                mainGroup.getChildren().add(listOfExercises.get(temp).getText());

                listOfTravelingAst.get(temp).play();
                listOfTravelingText.get(temp).play();
                listOfRotates.get(temp).play();
                temp++;
            } else {
                endOfGame();
            }
        }
    }

    public int getSizeOfAsteroidList() {
        return listOfAsteroids.size();
    }

    public void setLevelOnScreen() {

        Text level = new Text();
        level.setText("LEVEL2");
        level.setFont(Font.font(null, FontWeight.BOLD, 102));
        level.setFill(Color.YELLOW);
        level.setX(borderLayout.getWidth() / 2 + 102);
        level.setY(borderLayout.getHeight() / 2 + 102);

        mainGroup.getChildren().add(level);
    }

    private void endOfGame() {

        JOptionPane.showMessageDialog(null, "Vyprseli vase zivoty");
        clear();

    }

    private void setLives() {
        this.lives = 3;
    }
}
