import javafx.scene.image.Image;

import java.io.InputStream;

public class Drill {
    private double fuel;
    private int haul;
    private int money;
    private int XPosition;
    private int YPosition;
    private Image condition;
    public Drill(double fuel, int haul, int money, int XPosition, int YPosition) {
        this.fuel = fuel;
        this.haul = haul;
        this.money = money;
        this.XPosition = XPosition;
        this.YPosition = YPosition;
        condition = new Image("/assets/drill/drill_01.png");
    }

    // getter and setters
    public double getFuel() {return fuel;}
    public void setFuel(double fuel) {this.fuel = fuel;}
    public int getHaul() {return haul;}
    public void setHaul(int haul) {this.haul = haul;}
    public int getMoney() {return money;}
    public void setMoney(int money) {this.money = money;}
    public int getXPosition() {return XPosition;}
    public void setXPosition(int XPosition) {this.XPosition = XPosition;}
    public int getYPosition() {return YPosition;}
    public void setYPosition(int YPosition) {this.YPosition = YPosition;}
    public Image getCondition() {return condition;}
    public void setCondition(Image condition) {this.condition = condition;}
}