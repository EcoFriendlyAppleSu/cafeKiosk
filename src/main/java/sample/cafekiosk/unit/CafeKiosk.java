package sample.cafekiosk.unit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import sample.cafekiosk.unit.beverage.Beverage;
import sample.cafekiosk.unit.order.Order;

@Getter
public class CafeKiosk {

    private final List<Beverage> beverages = new ArrayList<>();

    public void add(Beverage beverage) {
        beverages.add(beverage);
    }

    public int calculateTotalPrice() {
        int tempPrice = 0;
        for (Beverage beverage : beverages) {
            tempPrice += beverage.getPrice();
        }
        return tempPrice;
    }

    public void remove(Beverage beverage) {
        beverages.remove(beverage);
    }

    public void clear(){
        beverages.clear();
    }

    public Order createOrder(){
        return new Order(Instant.now(), beverages);
    }
}
