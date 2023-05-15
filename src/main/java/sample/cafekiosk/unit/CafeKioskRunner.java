package sample.cafekiosk.unit;

import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;

public class CafeKioskRunner {

    public static void main(String[] args) {
        var cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        System.out.println("_> Americano 추가");

        cafeKiosk.add(new Latte());
        System.out.println("_> Latte 추가");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("_> 총 결제 금액 : " + totalPrice);
    }

}
