import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.Map;
import java.util.Random;

/**
 * GameBuilder class to create the initial condition of our game screen.
 */
public class GameBuilder {
    private Group root;
    private Drill drill;
    private ImageView[][] grid;
    private String[][] gridString;
    private Map<String,Integer> valuablesMoney;

    /**
     * Constructor for creating the gameBuilder object.
     * @param drill          Our drill object.
     * @param root           Root group where we add our elements.
     * @param grid           Array of imageView that represents the game grid.
     * @param gridString     Array of strings representing the type of each grid.
     * @param valuablesMoney A map contains the valuables money.
     */
    public GameBuilder(Drill drill,Group root,ImageView[][] grid,String[][] gridString,Map<String,Integer> valuablesMoney) {
        this.root = root;
        this.drill = drill;
        this.grid = grid;
        this.gridString = gridString;
        this.valuablesMoney = valuablesMoney;
    }

    Random random = new Random();

    /**
     * buildGame method to create initial condition of our game screen.It fills our screen with images.
     * @return Our game screen.
     */
    public Scene buildGame(){

        final int SCENE_WIDTH = 720;
        final int SCENE_HEIGHT = 720;

        int SQUARE_WIDTH = 30;
        int SQUARE_HEIGHT = 30;

        int GRID_SIZE = SCENE_WIDTH/SQUARE_WIDTH;

        // get the images from assets file for our game
        Image boulderImage = new Image("/assets/underground/obstacle_03.png");
        Image earthImage = new Image("/assets/underground/soil_01.png");
        Image topImage = new Image("/assets/underground/top_01.png");
        Image lavaImage = new Image("/assets/underground/lava_02.png");
        Image amazoniteImage = new Image("/assets/underground/valuable_amazonite.png");
        Image bronziumImage = new Image("/assets/underground/valuable_bronzium.png");
        Image diamondImage = new Image("/assets/underground/valuable_diamond.png");
        Image einsteiniumImage = new Image("/assets/underground/valuable_einsteinium.png");
        Image emeraldImage = new Image("/assets/underground/valuable_emerald.png");
        Image goldiumImage = new Image("/assets/underground/valuable_goldium.png");
        Image ironiumImage = new Image("/assets/underground/valuable_ironium.png");
        Image platinumImage = new Image("/assets/underground/valuable_platinum.png");
        Image rubyImage = new Image("/assets/underground/valuable_ruby.png");
        Image silveriumImage = new Image("/assets/underground/valuable_silverium.png");

        // sky
        for(int x=0;x<gridString.length;x++){
            for(int y=0;y<3;y++){
                gridString[x][y] = "sky";
            }
        }

        // boulders that are in the borders
        for(int x=0;x<GRID_SIZE;x++){
            for(int y=2;y<GRID_SIZE;y++){
                if((x==0 && y!=2) || (x == GRID_SIZE -1 && y!=2) || (y == GRID_SIZE -1)){
                    place(SQUARE_WIDTH, SQUARE_HEIGHT, boulderImage,"boulder", x, y);
                }else if(y == 2){
                    place(SQUARE_WIDTH, SQUARE_HEIGHT, topImage,"top", x, y);
                }
            }
        }
        // boulders that are in the game screen
        int BOULDER_NUMBER = 10;
        int BOULDER_COUNT = 0;
        while(BOULDER_COUNT < BOULDER_NUMBER){
            int randomX = random.nextInt(22) + 1;
            int randomY = random.nextInt(18) + 3;
            place(SQUARE_WIDTH,SQUARE_HEIGHT,boulderImage,"boulder",randomX,randomY);
            BOULDER_COUNT ++;
        }

        // lava
        int LAVA_NUMBER = 25;
        int LAVA_COUNT = 0;
        while(LAVA_COUNT < LAVA_NUMBER){
            int randomX = random.nextInt(22) + 1;
            int randomY = random.nextInt(18) + 3;
            place(SQUARE_WIDTH,SQUARE_HEIGHT,lavaImage,"lava",randomX,randomY);
            LAVA_COUNT ++;
        }


        // valuables

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,ironiumImage,"Ironium",11);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,bronziumImage,"Bronzium",10);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,silveriumImage,"Silverium",9);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, goldiumImage, "Goldium",8);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, platinumImage, "Platinum", 7);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,einsteiniumImage,"Einsteinium",6);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, emeraldImage, "Emerald", 5);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,rubyImage,"Ruby",4);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, diamondImage,"Diamond", 3);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, amazoniteImage,"Amazonite", 1);

        // earth
        for(int x=0;x<GRID_SIZE;x++) {
            for (int y = 2; y < GRID_SIZE; y++) {
                if(grid[x][y] == null){
                    place(SQUARE_WIDTH,SQUARE_HEIGHT,earthImage,"earth",x,y);
                }
            }
        }

        // drill
        ImageView drillImage = new ImageView(drill.getCondition());
        drillImage.setFitWidth(SQUARE_WIDTH);
        drillImage.setFitHeight(SQUARE_HEIGHT);
        drillImage.setX(drill.getXPosition());
        drillImage.setY(drill.getYPosition());
        root.getChildren().add(drillImage);

        Scene scene = new Scene(root,SCENE_WIDTH,SCENE_HEIGHT, Color.SKYBLUE);
        return scene;
    }

    /**
     * placeValuables method for place the valuables randomly in the game screen.
     * @param SQUARE_WIDTH    Width of each grid.
     * @param SQUARE_HEIGHT   Height of each grid.
     * @param image           Image object of valuables to put in the game screen.
     * @param name            Name to put in gridString for each image in grid.
     * @param VALUABLE_NUMBER Number of the given valuable in the game.
     */
    private void placeValuables(int SQUARE_WIDTH, int SQUARE_HEIGHT, Image image,String name, int VALUABLE_NUMBER) {
        int count = 0;
        while (count < VALUABLE_NUMBER){
            int randomX = random.nextInt(22) + 1;
            int randomY = random.nextInt(18) + 5;
            if(grid[randomX][randomY] == null){
                place(SQUARE_WIDTH,SQUARE_HEIGHT,image,name,randomX,randomY);
                count++;
            }
        }
    }

    /**
     * place method to place all images int he grids.
     * @param SQUARE_WIDTH   Width of each grid.
     * @param SQUARE_HEIGHT  Height of each grid.
     * @param image          Image object of elements to put in the game screen.
     * @param name           Name to put in gridString for each image in grid.
     * @param x              X position of image that is given.
     * @param y              Y position of image that is given.
     */
    private void place(int SQUARE_WIDTH, int SQUARE_HEIGHT, Image image,String name, int x, int y) {

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SQUARE_WIDTH);
        imageView.setFitHeight(SQUARE_HEIGHT);
        imageView.setX(x * SQUARE_WIDTH);
        imageView.setY(y * SQUARE_HEIGHT);
        grid[x][y] = imageView;
        gridString[x][y] = name;
        root.getChildren().add(grid[x][y]);
    }

    // getter and setters
    public Group getRoot() {return root;}
    public void setRoot(Group root) {this.root = root;}
    public Drill getDrill() {return drill;}
    public void setDrill(Drill drill) {this.drill = drill;}
    public ImageView[][] getGrid() {return grid;}
    public void setGrid(ImageView[][] grid) {this.grid = grid;}
    public String[][] getGridString() {return gridString;}
    public void setGridString(String[][] gridString) {this.gridString = gridString;}
    public Map<String, Integer> getValuablesMoney() {return valuablesMoney;}
    public void setValuablesMoney(Map<String, Integer> valuablesMoney) {this.valuablesMoney = valuablesMoney;}
}