import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class GameBuilder {
    private Group root;
    private Drill drill;
    private ImageView[][] grid;
    private String[][] gridString;
    private Map<String,Integer> valuablesMoney;
    public GameBuilder(Drill drill,Group root,ImageView[][] grid,String[][] gridString,Map<String,Integer> valuablesMoney) {
        this.root = root;
        this.drill = drill;
        this.grid = grid;
        this.gridString = gridString;
        this.valuablesMoney = valuablesMoney;
    }
    public Scene buildTheGame(){

        final int SCENE_WIDTH = 750;
        final int SCENE_HEIGHT = 750;

        int SQUARE_WIDTH = 30;
        int SQUARE_HEIGHT = 30;

        int GRID_SIZE = SCENE_WIDTH/SQUARE_WIDTH;

        Image boulderImage = new Image("/assets/underground/obstacle_03.png");
        Image earthImage = new Image("/assets/underground/soil_01.png");
        Image topImage = new Image("/assets/underground/top_01.png");
        Image lavaImage = new Image("/assets/underground/lava_02.png");
        Image diamondImage = new Image("/assets/underground/valuable_diamond.png");
        Image emeraldImage = new Image("/assets/underground/valuable_emerald.png");
        Image platinumImage = new Image("/assets/underground/valuable_platinium.png");
        Image amazoniteImage = new Image("/assets/underground/valuable_amazonite.png");
        Image goldiumImage = new Image("/assets/underground/valuable_goldium.png");
        Image ironiumImage = new Image("/assets/underground/valuable_ironium.png");
        Image bronziumImage = new Image("/assets/underground/valuable_bronzium.png");
        Image rubyImage = new Image("/assets/underground/valuable_ruby.png");
        Image silveriumImage = new Image("/assets/underground/valuable_silverium.png");
        Image einsteiniumImage = new Image("/assets/underground/valuable_einsteinium.png");


        // sky
        for(int x=0;x<gridString.length;x++){
            for(int y=0;y<3;y++){
                gridString[x][y] = "sky";
            }
        }

        // boulders
        for(int x=0;x<GRID_SIZE;x++){
            for(int y=2;y<GRID_SIZE;y++){
                if((x==0 && y!=2) || (x == GRID_SIZE -1 && y!=2) || (y == GRID_SIZE -1)){
                    place(SQUARE_WIDTH, SQUARE_HEIGHT, boulderImage,"boulder", x, y);
                }else if(y == 2){
                    place(SQUARE_WIDTH, SQUARE_HEIGHT, topImage,"top", x, y);
                }
            }
        }
        Random random = new Random();
        int BOULDER_NUMBER = 10;
        int BOULDER_COUNT = 0;
        while(BOULDER_COUNT < BOULDER_NUMBER){
            int randomX = random.nextInt(23) + 1;
            int randomY = random.nextInt(19) + 3;
            place(SQUARE_WIDTH,SQUARE_HEIGHT,boulderImage,"boulder",randomX,randomY);
            BOULDER_COUNT ++;
        }

        // lava
        int LAVA_NUMBER = 25;
        int LAVA_COUNT = 0;
        while(LAVA_COUNT < LAVA_NUMBER){
            int randomX = random.nextInt(23) + 1;
            int randomY = random.nextInt(19) + 3;
            place(SQUARE_WIDTH,SQUARE_HEIGHT,lavaImage,"lava",randomX,randomY);
            LAVA_COUNT ++;
        }


        // valuables

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, diamondImage,"Diamond" ,random, 3);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, emeraldImage, "Emerald", random, 5);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, platinumImage, "Platinum",random, 7);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, amazoniteImage,"Amazonite", random, 1);

        placeValuables(SQUARE_WIDTH, SQUARE_HEIGHT, goldiumImage, "Goldium",random,8);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,ironiumImage,"Ironium",random,11);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,bronziumImage,"Bronzium",random,10);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,rubyImage,"Ruby",random,4);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,silveriumImage,"Silverium",random,9);

        placeValuables(SQUARE_WIDTH,SQUARE_HEIGHT,einsteiniumImage,"Einsteinium",random,6);

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

    private void placeValuables(int SQUARE_WIDTH, int SQUARE_HEIGHT, Image image,String value, Random random, int VALUABLE_NUMBER) {
        int count = 0;
        while (count < VALUABLE_NUMBER){
            int randomX = random.nextInt(23) + 1;
            int randomY = random.nextInt(19) + 5;
            if(grid[randomX][randomY] == null){
                place(SQUARE_WIDTH,SQUARE_HEIGHT,image,value,randomX,randomY);
                count++;
            }
        }
    }

    private void place(int SQUARE_WIDTH, int SQUARE_HEIGHT, Image image,String value, int x, int y) {

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(SQUARE_WIDTH);
        imageView.setFitHeight(SQUARE_HEIGHT);
        imageView.setX(x * SQUARE_WIDTH);
        imageView.setY(y * SQUARE_HEIGHT);
        grid[x][y] = imageView;
        gridString[x][y] = value;
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