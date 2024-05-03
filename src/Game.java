import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class Game {
    private Text textForFuel;
    private Text textForHaul;
    private Text textForMoney;
    private Drill drill;
    private Scene scene;
    private Stage stage;
    private Group root;
    private ImageView[][] grid;
    private String[][] gridString;
    private Map<String,Integer> valuablesMoney;
    private Map<String,Integer> valuablesWeight;

    public Game(Text textForFuel, Text textForHaul, Text textForMoney, Drill drill, Scene scene, Stage stage, Group root, ImageView[][] grid, String[][] gridString,
                Map<String, Integer> valuablesMoney, Map<String, Integer> valuablesWeight) {
        this.textForFuel = textForFuel;
        this.textForHaul = textForHaul;
        this.textForMoney = textForMoney;
        this.drill = drill;
        this.scene = scene;
        this.stage = stage;
        this.root = root;
        this.grid = grid;
        this.gridString = gridString;
        this.valuablesMoney = valuablesMoney;
        this.valuablesWeight = valuablesWeight;
    }
    // drill conditions
    Image drillLeft = new Image("/assets/drill/drill_01.png");
    Image drillRight = new Image("/assets/drill/drill_60.png");
    Image drillDown = new Image("/assets/drill/drill_44.png");
    Image drillUp = new Image("/assets/drill/drill_25.png");
    Image[] imagesForUp = new Image[]{new Image("/assets/drill/drill_23.png"),new Image("/assets/drill/drill_24.png"),
            new Image("/assets/drill/drill_25.png"),new Image("/assets/drill/drill_26.png"),
            new Image("/assets/drill/drill_27.png")};
    Timeline timelineForGravity;
    Timeline timelineForFuel;
    AnimationTimer animationTimer;
    public void game(){
        int SQUARE_LENGTH = 30;

        double DECREASE_RATE = 1.511;
        timelineForFuel = new Timeline(new KeyFrame(Duration.millis(100), event -> decreaseFuel(drill,textForFuel,DECREASE_RATE)));
        timelineForFuel.setCycleCount(Animation.INDEFINITE);
        timelineForFuel.play();

        scene.setOnKeyPressed(keyEvent -> {
            if(drill.getFuel() > 0){
                if(keyEvent.getCode() == KeyCode.RIGHT){
                    right(SQUARE_LENGTH,scene,root);
                    decreaseFuel(100);
                }else if(keyEvent.getCode() == KeyCode.LEFT){
                    left(SQUARE_LENGTH,scene,root);
                    decreaseFuel(100);
                }else if(keyEvent.getCode() == KeyCode.UP){
                    up(SQUARE_LENGTH,scene,root);
                    // I did not call decreaseFuel function here because decrease amount changes for different up situations
                }else if(keyEvent.getCode() == KeyCode.DOWN){
                    down(SQUARE_LENGTH,scene,root);
                    decreaseFuel(100);
                }
            }
        });
    }

    private void decreaseFuel(int decreaseAmount){
        drill.setFuel(drill.getFuel()-decreaseAmount);
        textForFuel.setText("fuel:" + drill.getFuel());
        if(drill.getFuel() <= 0){
            gameOverForFuel();
        }
    }

    private void up(int SQUARE_LENGTH,Scene scene,Group root) {

        if(checkUp(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            // change the direction of drill
            if(drill.getCondition() != drillUp){
                drill.setCondition(drillUp);
            }
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setYPosition(drill.getYPosition() - SQUARE_LENGTH);

            drillImage.setFitWidth(SQUARE_LENGTH);
            drillImage.setFitHeight(SQUARE_LENGTH);
            drillImage.setX(drill.getXPosition());
            drillImage.setY(drill.getYPosition());
            root.getChildren().add(drillImage);
            scene.setOnKeyReleased(keyEvent -> {
                KeyCode keyCode = keyEvent.getCode();
                if(keyCode == KeyCode.UP){
                    if(timelineForGravity != null){
                        timelineForGravity.stop();
                    }
                    gravity(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH);
                }
            });
        }
    }
    private void down(int SQUARE_LENGTH,Scene scene,Group root) {

        if(checkDown(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            // change the direction of drill
            if(drill.getCondition() != drillDown){
                drill.setCondition(drillDown);
            }
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setYPosition(drill.getYPosition() + SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }

    private void move(int SQUARE_LENGTH, Scene scene, Group root, ImageView drillImage) {
        drillImage.setFitWidth(SQUARE_LENGTH);
        drillImage.setFitHeight(SQUARE_LENGTH);
        drillImage.setX(drill.getXPosition());
        drillImage.setY(drill.getYPosition());
        root.getChildren().add(drillImage);
        scene.setRoot(root);
        if(timelineForGravity != null){
            timelineForGravity.stop();
        }
        if(animationTimer != null){
            animationTimer.stop();
        }
        gravity(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH);
    }

    private void right(int SQUARE_LENGTH,Scene scene,Group root) {

        if(checkRight(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            // change the direction of drill
            if(drill.getCondition() != drillRight){
                drill.setCondition(drillRight);
            }
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setXPosition(drill.getXPosition() + SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }
    private void left(int SQUARE_LENGTH,Scene scene,Group root) {

        if(checkLeft(drill.getXPosition(), drill.getYPosition(), SQUARE_LENGTH,root)){
            // change the direction of drill
            if(drill.getCondition() != drillLeft){
                drill.setCondition(drillLeft);
            }
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setXPosition(drill.getXPosition() - SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }

    private boolean checkLeft(int xPosition , int yPosition, int SQUARE_LENGTH,Group root){
        if(drill.getXPosition() == 0){
            return false;
        }
        String value = gridString[(xPosition - SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)];
        switch (value) {
            case "empty":
                root.getChildren().remove(root.getChildren().size() - 1);
                return true;
            case "boulder":
                return false;
            case "earth":
            case "top":
                root.getChildren().remove(root.getChildren().size() - 1);
                root.getChildren().remove(grid[(xPosition - SQUARE_LENGTH) / SQUARE_LENGTH][(yPosition / SQUARE_LENGTH)]);
                gridString[(xPosition - SQUARE_LENGTH) / SQUARE_LENGTH][(yPosition / SQUARE_LENGTH)] = "empty";
                return true;
            case "lava":
                gameOverForLava();
                break;
            default:
                checkForValuables(value);
                root.getChildren().remove(grid[(xPosition - SQUARE_LENGTH) / SQUARE_LENGTH][(yPosition / SQUARE_LENGTH)]);
                gridString[(xPosition - SQUARE_LENGTH) / SQUARE_LENGTH][(yPosition / SQUARE_LENGTH)] = "empty";
                return true;
        }
        root.getChildren().remove(root.getChildren().size()-1);
        return true;
    }
    private boolean checkRight(int xPosition, int yPosition , int SQUARE_LENGTH){
        if(drill.getXPosition() == (scene.getWidth() - SQUARE_LENGTH)){
            return false;
        }
        String value = gridString[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)];
        switch (value) {
            case "empty":
                root.getChildren().remove(root.getChildren().size() - 1);
                return true;
            case "boulder":
                return false;
            case "earth":
            case "top":
                root.getChildren().remove(root.getChildren().size() - 1);
                root.getChildren().remove(grid[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)]);
                gridString[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)] = "empty";
                return true;
            case "lava":
                gameOverForLava();
                break;
            default:
                checkForValuables(value);
                root.getChildren().remove(grid[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)]);
                gridString[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)] = "empty";
                return true;
        }
        root.getChildren().remove(root.getChildren().size()-1);
        return true;
    }
    private boolean checkUp(int xPosition, int yPosition , int SQUARE_LENGTH){
        if(drill.getYPosition() == 10){
            decreaseFuel(120);
            return false;
        }
        String value = gridString[xPosition/SQUARE_LENGTH][(yPosition - SQUARE_LENGTH)/SQUARE_LENGTH];
        if(value.equals("empty") || value.equals("sky")){
            decreaseFuel(100);
            root.getChildren().remove(root.getChildren().size()-1);
            return true;
        }
        decreaseFuel(120);
        animationTimer = new AnimationTimer() {
            int currentImageIndex=0;
            long lastTime = 0;

            @Override
            public void handle(long now) {
                // Calculate time difference since the last frame
                long elapsedTime = now - lastTime;

                // If 100 milliseconds have passed, move to the next image
                if (elapsedTime >= 200_000_000) {
                    drill.setCondition(imagesForUp[currentImageIndex]);
                    root.getChildren().remove(root.getChildren().size()-1);
                    ImageView drillImage = new ImageView(drill.getCondition());
                    drillImage.setFitWidth(SQUARE_LENGTH);
                    drillImage.setFitHeight(SQUARE_LENGTH);
                    drillImage.setX(drill.getXPosition());
                    drillImage.setY(drill.getYPosition());
                    root.getChildren().add(drillImage);
                    currentImageIndex = (currentImageIndex + 1) % imagesForUp.length;
                    lastTime = now;
                }
            }
        };

    // Start the animation timer
        animationTimer.start();

        root.getChildren().remove(root.getChildren().size()-1);
        if(drill.getCondition() != drillUp){
            drill.setCondition(drillUp);
        }
        ImageView drillImage = new ImageView(drill.getCondition());
        drillImage.setFitWidth(SQUARE_LENGTH);
        drillImage.setFitHeight(SQUARE_LENGTH);
        drillImage.setX(drill.getXPosition());
        drillImage.setY(drill.getYPosition());
        root.getChildren().add(drillImage);
        return false;
    }

    private boolean checkDown(int xPosition, int yPosition , int SQUARE_LENGTH){
        if(drill.getYPosition() == scene.getHeight() - SQUARE_LENGTH){
            return false;
        }
        String value = gridString[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH];
        switch (value) {
            case "empty":
                root.getChildren().remove(root.getChildren().size() - 1);
                return true;
            case "boulder":
                return false;
            case "earth":
            case "top":
                root.getChildren().remove(root.getChildren().size() - 1);
                root.getChildren().remove(grid[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH]);
                gridString[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH] = "empty";
                return true;
            case "lava":
                gameOverForLava();
                break;
            default:
                checkForValuables(value);
                root.getChildren().remove(grid[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH]);
                gridString[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH] = "empty";
                return true;
        }
        root.getChildren().remove(root.getChildren().size()-1);
        return true;
    }

    private void checkForValuables(String value) {
        root.getChildren().remove(root.getChildren().size()-1);
        for(String s : valuablesMoney.keySet()){
            if(s.equals(value)){
                drill.setMoney(drill.getMoney() + valuablesMoney.get(s));
                drill.setHaul(drill.getHaul() + valuablesWeight.get(s));
                textForMoney.setText("money:" + drill.getMoney());
                textForHaul.setText("haul:" + drill.getHaul());
                break;
            }
        }
    }
    private void decreaseFuel(Drill drill,Text textForFuel,double DECREASE_RATE) {
        drill.setFuel(drill.getFuel()-DECREASE_RATE);
        textForFuel.setText("fuel:" + String.format("%.3f",drill.getFuel()));
        if(drill.getFuel() <= 0){
            timelineForFuel.stop();
            gameOverForFuel();
        }
    }

    private void gravity(int xPosition, int yPosition , int SQUARE_LENGTH) {
        int EMPTY_COUNT = 0;
        for(int y = (yPosition/SQUARE_LENGTH) + 1;y<scene.getHeight()/SQUARE_LENGTH;y++){
            if(gridString[xPosition/SQUARE_LENGTH][y].equals("empty") || gridString[xPosition/SQUARE_LENGTH][y].equals("sky")){
                EMPTY_COUNT += 1;
            }else{
                break;
            }
        }
        timelineForGravity = new Timeline(new KeyFrame(Duration.millis(450),actionEvent -> {
            root.getChildren().remove(root.getChildren().size() - 1);
            drill.setYPosition(drill.getYPosition() + SQUARE_LENGTH);
            ImageView drillImage = new ImageView(drill.getCondition());
            drillImage.setFitWidth(SQUARE_LENGTH);
            drillImage.setFitHeight(SQUARE_LENGTH);
            drillImage.setX(drill.getXPosition());
            drillImage.setY(drill.getYPosition());
            root.getChildren().add(drillImage);
            scene.setRoot(root);
        }));
        if(EMPTY_COUNT > 0 ){
            timelineForGravity.setCycleCount(EMPTY_COUNT);
            timelineForGravity.play();
        }
    }
    private void gameOverForFuel(){
        Group root = new Group();
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(new Font(30));
        Text collectedMoneyText = new Text("Collected Money: " + drill.getMoney());
        collectedMoneyText.setFont(new Font(30));
        // Calculate the center coordinates of the scene
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight() / 2;

        // Set the position of the texts to the center coordinates
        gameOverText.setX(centerX - (gameOverText.getBoundsInLocal().getWidth() / 2));
        gameOverText.setY(centerY + (gameOverText.getBoundsInLocal().getHeight() / 2));
        collectedMoneyText.setX(centerX - (collectedMoneyText.getBoundsInLocal().getWidth()/2));
        collectedMoneyText.setY(gameOverText.getY() + gameOverText.getBoundsInLocal().getHeight());

        root.getChildren().add(gameOverText);
        root.getChildren().add(collectedMoneyText);
        Scene greenGameOverScene = new Scene(root,scene.getWidth(), scene.getHeight(), Color.GREEN);
        stage.setScene(greenGameOverScene);
        stage.show();
    }

    private void gameOverForLava(){
        Group root = new Group();
        Text gameOverText = new Text("Game Over");
        gameOverText.setFont(new Font(30));
        double centerX = scene.getWidth() / 2;
        double centerY = scene.getHeight() / 2;
        gameOverText.setX(centerX - (gameOverText.getBoundsInLocal().getWidth() / 2));
        gameOverText.setY(centerY + (gameOverText.getBoundsInLocal().getHeight() / 2));
        root.getChildren().add(gameOverText);
        Scene greenGameOverScene = new Scene(root,scene.getWidth(), scene.getHeight(), Color.RED);
        stage.setScene(greenGameOverScene);
        stage.show();
    }
    // getter and setter
    public Text getTextForFuel() {return textForFuel;}
    public void setTextForFuel(Text textForFuel) {this.textForFuel = textForFuel;}
    public Text getTextForHaul() {return textForHaul;}
    public void setTextForHaul(Text textForHaul) {this.textForHaul = textForHaul;}
    public Text getGetTextForMoney() {return textForMoney;}
    public void setGetTextForMoney(Text getTextForMoney) {this.textForMoney = getTextForMoney;}
    public Drill getDrill() {return drill;}
    public void setDrill(Drill drill) {this.drill = drill;}
    public Scene getScene() {return scene;}
    public void setScene(Scene scene) {this.scene = scene;}
    public Stage getStage() {return stage;}
    public void setStage(Stage stage) {this.stage = stage;}
    public Group getRoot() {return root;}
    public void setRoot(Group root) {this.root = root;}
    public ImageView[][] getGrid() {return grid;}
    public void setGrid(ImageView[][] grid) {this.grid = grid;}
    public String[][] getGridString() {return gridString;}
    public void setGridString(String[][] gridString) {this.gridString = gridString;}
    public Map<String, Integer> getValuablesMoney() {return valuablesMoney;}
    public void setValuablesMoney(Map<String, Integer> valuablesMoney) {this.valuablesMoney = valuablesMoney;}
    public Map<String, Integer> getValuablesWeight() {return valuablesWeight;}
    public void setValuablesWeight(Map<String, Integer> valuablesWeight) {this.valuablesWeight = valuablesWeight;}
}