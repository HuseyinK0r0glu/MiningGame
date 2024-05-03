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

/**
 * Main class where the program starts and also this class extends Application from javafx.
 */
public class Main extends Application{
    /**
     * main method to start the program.
     * @param args  It takes arguments from the command line if there is any given.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * start method to start the javafx application.
     * @param stage      Stage for the javafx project.
     * @throws Exception Throws an exception if any occurs.
     */
    @Override
    public void start(Stage stage) throws Exception {

        // Generate fuel randomly.
        Random random = new Random();
        int fuel = 1000 * (random.nextInt(10)) + 10000;

        // Create drill object with its initial values.
        Drill drill = new Drill(fuel,0,0,630,40);
        Group root = new Group();

        // Maps to hold the valuables items information.
        Map<String,Integer> valuablesMoney = new HashMap<>();
        Map<String,Integer> valuablesWeight = new HashMap<>();
        // Read valuable items information from attributes file.
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

        // Texts for displaying the attributes of our drill.
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

        // Crate two grid. One of them for hold the images of the game,other one for the name of the images that we are putting int the game.
        ImageView[][] grid = new ImageView[25][25];
        String[][] gridString = new String[25][25];

        // Create a gameBuilder object and call buildGame method to create initial condition of our game screen.
        GameBuilder gameBuilder = new GameBuilder(drill,root,grid,gridString,valuablesMoney);
        Scene scene = gameBuilder.buildGame();

        // Call the game method from Game class to start the game.
        Game game = new Game(textForFuel,textForHaul,textForMoney,drill,scene,stage,root,grid,gridString,valuablesMoney,valuablesWeight);
        game.game();

        // display the stage
        stage.setScene(scene);
        stage.setTitle("HU-Load");
        stage.show();
    }
}
