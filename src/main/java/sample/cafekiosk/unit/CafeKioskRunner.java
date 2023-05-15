package sample.cafekiosk.unit;

import java.time.LocalDateTime;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

public class CafeKioskRunner {

    public static void main(String[] args) {
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(),1);
        System.out.println("_> Americano 추가");

        cafeKiosk.add(new Latte(), 1);
        System.out.println("_> Latte 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("_> 총 결제 금액 : " + totalPrice);

        Order order = cafeKiosk.createOrder(LocalDateTime.now());
    }

}
