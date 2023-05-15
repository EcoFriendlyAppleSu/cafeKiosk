package sample.cafekiosk.unit.beverage;

public class Latte implements Beverage {

    private String name;
    private int price;

    @Override
    public String getName() {
        return "라떼";
    }

    @Override
    public int getPrice() {
        return 4500;
    }
}
