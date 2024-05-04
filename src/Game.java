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

/**
 * Game class to play out game.It handles all components of the game.
 */
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

    /**
     * Constructor for creating an object of Game class.
     * @param textForFuel       Text that shows the amount of fuel drill has left.
     * @param textForHaul       Text that shows the current haul of the drill.
     * @param textForMoney      Text that shows the total money collected.
     * @param drill             The drill object.Our machine.
     * @param scene             Our game scene.It has all the images for our game.
     * @param stage             Stage of the game.
     * @param root              Root group of the scene.
     * @param grid              Grid of imageView objects representing the game components.
     * @param gridString        Grid of strings representing the type of each grid cell.
     * @param valuablesMoney    Map that contains the money of valuables.
     * @param valuablesWeight   Map that contains the weight of valuables.
     */
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

    /**
     * game method to play the game.
     */
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

    /**
     * Method fpr decreasing the fuel for every move.
     * @param decreaseAmount The change in the fuel for that move.If drill tries to drill upward decreaseAmount will
     *                       be bigger than normal conditions.
     */
    private void decreaseFuel(int decreaseAmount){
        drill.setFuel(drill.getFuel()-decreaseAmount);
        textForFuel.setText("fuel:" + drill.getFuel());
        if(drill.getFuel() <= 0){
            gameOverForFuel();
        }
    }

    /**
     * up method to control the move for upward direction.
     * @param SQUARE_LENGTH Height of each grid.
     * @param scene         Our game scene.It has all the images for our game.
     * @param root          Root group of the scene.
     */
    private void up(int SQUARE_LENGTH,Scene scene,Group root) {

        if(checkUp(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            // change the direction of drill
            if(drill.getCondition() != drillUp){
                drill.setCondition(drillUp);
            }
            // set the imageView components for drill
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

    /**
     * down method to control the move for downward direction.
     * @param SQUARE_LENGTH Height of each grid.
     * @param scene         Our game scene.It has all the images for our game.
     * @param root          Root group of the scene.
     */
    private void down(int SQUARE_LENGTH,Scene scene,Group root) {

        // change the direction of drill
        if(drill.getCondition() != drillDown){
            drill.setCondition(drillDown);
            root.getChildren().remove(root.getChildren().size()-1);
            move(SQUARE_LENGTH,scene,root,new ImageView(drill.getCondition()));
        }
        if(checkDown(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setYPosition(drill.getYPosition() + SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }

    /**
     * right method to control the move for rightward direction.
     * @param SQUARE_LENGTH Height of each grid.
     * @param scene         Our game scene.It has all the images for our game.
     * @param root          Root group of the scene.
     */
    private void right(int SQUARE_LENGTH,Scene scene,Group root) {

        // change the direction of drill
        if(drill.getCondition() != drillRight){
            drill.setCondition(drillRight);
            root.getChildren().remove(root.getChildren().size()-1);
            move(SQUARE_LENGTH,scene,root,new ImageView(drill.getCondition()));
        }
        if(checkRight(drill.getXPosition(),drill.getYPosition(),SQUARE_LENGTH)){
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setXPosition(drill.getXPosition() + SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }

    /**
     * left method to control the move for leftward direction.
     * @param SQUARE_LENGTH Height of each grid.
     * @param scene         Our game scene.It has all the images for our game.
     * @param root          Root group of the scene.
     */
    private void left(int SQUARE_LENGTH,Scene scene,Group root) {

        // change the direction of drill
        if(drill.getCondition() != drillLeft){
            drill.setCondition(drillLeft);
            root.getChildren().remove(root.getChildren().size()-1);
            move(SQUARE_LENGTH,scene,root,new ImageView(drill.getCondition()));
        }
        if(checkLeft(drill.getXPosition(), drill.getYPosition(), SQUARE_LENGTH)){
            ImageView drillImage = new ImageView(drill.getCondition());
            drill.setXPosition(drill.getXPosition() - SQUARE_LENGTH);
            move(SQUARE_LENGTH, scene, root, drillImage);
        }
    }

    /**
     * move method that changes certain things for left,right,down
     * @param SQUARE_LENGTH Height of each grid.
     * @param scene         Our game scene.It has all the images for our game.
     * @param root          Root group of the scene.
     * @param drillImage    The Image of drill's current condition.
     */
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

    /**
     * checkLeft method for checking the left move is valid or not.
     * @param xPosition     X position of our drill.
     * @param yPosition     Y position of our drill.
     * @param SQUARE_LENGTH Height of each grid.
     * @return              true if move is valid , false if it is not.
     */
    private boolean checkLeft(int xPosition , int yPosition, int SQUARE_LENGTH){
        // if drill is at the most left of our scene
        if(drill.getXPosition() == 0){
            return false;
        }
        String value = gridString[(xPosition - SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)];
        // checks for every possible element
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
        return true;
    }

    /**
     * checkRight method for checking the right move is valid or not.
     * @param xPosition     X position of our drill.
     * @param yPosition     Y position of our drill.
     * @param SQUARE_LENGTH Height of each grid.
     * @return              true if move is valid , false if it is not.
     */
    private boolean checkRight(int xPosition, int yPosition , int SQUARE_LENGTH){
        // if drill is at the most right of our scene
        if(drill.getXPosition() == (scene.getWidth() - SQUARE_LENGTH)){
            return false;
        }
        String value = gridString[(xPosition + SQUARE_LENGTH)/SQUARE_LENGTH][(yPosition/SQUARE_LENGTH)];
        // checks for every possible element
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

    /**
     * checkUp method for checking the up move is valid or not and also add animation if drill try to drill upward.
     * @param xPosition     X position of our drill.
     * @param yPosition     Y position of our drill.
     * @param SQUARE_LENGTH Height of each grid.
     * @return              true if move is valid , false if it is not.
     */
    private boolean checkUp(int xPosition, int yPosition , int SQUARE_LENGTH){
        // if drill is at the top of our scene
        if(drill.getYPosition() == 10){
            decreaseFuel(120);
            return false;
        }
        String value = gridString[xPosition/SQUARE_LENGTH][(yPosition - SQUARE_LENGTH)/SQUARE_LENGTH];
        // drill can not drill upward so if the up block is only "sky" or "empty" drill can fly
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
    /**
     * checkDown method for checking the down move is valid or not.
     * @param xPosition     X position of our drill.
     * @param yPosition     Y position of our drill.
     * @param SQUARE_LENGTH Height of each grid.
     * @return              true if move is valid , false if it is not.
     */
    private boolean checkDown(int xPosition, int yPosition , int SQUARE_LENGTH){
        // if drill is at the bottom of our scene
        if(drill.getYPosition() == scene.getHeight() - SQUARE_LENGTH){
            return false;
        }
        String value = gridString[xPosition/SQUARE_LENGTH][(yPosition + SQUARE_LENGTH)/SQUARE_LENGTH];
        // checks for every possible element
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

    /**
     * If drill encounters valuable this method is called to modify the drill's money and haul.
     * @param valuable Valuable that the drill encounters.
     */
    private void checkForValuables(String valuable) {
        root.getChildren().remove(root.getChildren().size()-1);
        for(String s : valuablesMoney.keySet()){
            if(s.equals(valuable)){
                drill.setMoney(drill.getMoney() + valuablesMoney.get(s));
                drill.setHaul(drill.getHaul() + valuablesWeight.get(s));
                textForMoney.setText("money:" + drill.getMoney());
                textForHaul.setText("haul:" + drill.getHaul());
                break;
            }
        }
    }

    /**
     * decreaseFuel method to decrease the fuel every 0.1 second.
     * @param drill         Our drill.
     * @param textForFuel   Text that shows the amount of fuel drill has left.
     * @param DECREASE_RATE Decrease amount that will be executed on drill's fuel every 0.1 second.
     */
    private void decreaseFuel(Drill drill,Text textForFuel,double DECREASE_RATE) {
        drill.setFuel(drill.getFuel()-DECREASE_RATE);
        textForFuel.setText("fuel:" + String.format("%.3f",drill.getFuel()));
        if(drill.getFuel() <= 0){
            timelineForFuel.stop();
            gameOverForFuel();
        }
    }

    /**
     * gravity method checks if there is no block below our drill it will fall down.
     * @param xPosition     X position of our drill.
     * @param yPosition     Y position of our drill.
     * @param SQUARE_LENGTH Height of each grid.
     */
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

    /**
     * gameForFuel method is called whenever our drill is out of fuel.This method creates another scene
     * and this scene to our stage.This also displays the collected money.
     */
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

    /**
     * gameForLava method is called whenever drill encounters a lava and this method ends the game,
     * create a new scene and adds it to our stage. This does not display the drill's money because
     * after touching the lava, machine is destroyed.
     */
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