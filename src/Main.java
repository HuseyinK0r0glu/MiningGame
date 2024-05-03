import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Main extends Application{
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Random random = new Random();
        int fuel = 1000 * (random.nextInt(10)) + 10000;

        Drill drill = new Drill(fuel,0,0,630,40);
        Group root = new Group();

        Map<String,Integer> valuablesMoney = new HashMap<>();
        Map<String,Integer> valuablesWeight = new HashMap<>();
        String[] valuables = FileInput.readFile("src/assets/atributes_of_valuables.txt",true,true);
        if (valuables != null) {
            for(int i=1;i<valuables.length;i++){
                String[] input = valuables[i].split("\t");
                valuablesMoney.put(input[0],Integer.parseInt(input[1]));
                valuablesWeight.put(input[0],Integer.parseInt(input[2]));
            }
        }

        Rectangle skyRectangle = new Rectangle(720,60, Color.SKYBLUE);
        skyRectangle.setX(0);
        skyRectangle.setY(0);
        Rectangle groundRectangle = new Rectangle(720,650,Color.SADDLEBROWN);
        groundRectangle.setX(0);
        groundRectangle.setY(60);

        root.getChildren().addAll(skyRectangle,groundRectangle);

        Text textForFuel = new Text("fuel:" + String.format("%.3f",drill.getFuel()));
        Text textForHaul = new Text("haul:" + drill.getHaul());
        Text textForMoney = new Text("money:" + drill.getMoney());

        textForFuel.setFont(new Font(20));
        textForHaul.setFont(new Font(20));
        textForMoney.setFont(new Font(20));

        textForFuel.setX(0);
        textForFuel.setY(20);

        textForHaul.setX(0);
        textForHaul.setY(40);

        textForMoney.setX(0);
        textForMoney.setY(60);

        root.getChildren().add(textForFuel);
        root.getChildren().add(textForHaul);
        root.getChildren().add(textForMoney);

        ImageView[][] grid = new ImageView[25][25];
        String[][] gridString = new String[25][25];


        GameBuilder gameBuilder = new GameBuilder(drill,root,grid,gridString,valuablesMoney);
        Scene scene = gameBuilder.buildTheGame();

        Game game = new Game(textForFuel,textForHaul,textForMoney,drill,scene,stage,root,grid,gridString,valuablesMoney,valuablesWeight);
        game.game();

        stage.setScene(scene);
        stage.setTitle("HU-Load");
        stage.show();
    }
}
