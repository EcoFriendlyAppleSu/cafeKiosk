package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.order.Order;

class CafeKioskTest {

    @Test
    public void addTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 2);

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    public void removeTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    public void clearTest() throws Exception {

        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);
        cafeKiosk.add(new Americano(), 1);
        cafeKiosk.add(new Americano(), 1);
        cafeKiosk.clear();

        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @Test
    public void addBeverageExceptionTest() throws Exception {
        var cafeKiosk = new CafeKiosk();
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0)).isInstanceOf(IllegalArgumentException.class);
    }

    /*
    * 과연 이 테스트가 항상 통과하는 테스트일까요?
    * */
    @Test
    public void createOrderTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 5,15,10,0));

        assertThat(order.getBeverages().size()).isEqualTo(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    public void createOrderExceptionTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 5,15,9,59))).isInstanceOf(IllegalArgumentException.class);
    }

}
