package sample.cafekiosk.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

class CafeKioskTest {

    @DisplayName("음료 주문 시 음료와 수량을 입력합니다.")
    @Test
    public void addTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 2);

        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("음료를 삭제합니다.")
    @Test
    public void removeTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 1);

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    @DisplayName("주문 음료 전부를 삭제합니다.")
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
        assertThatThrownBy(() -> cafeKiosk.add(new Americano(), 0)).isInstanceOf(
            IllegalArgumentException.class);
    }

    /*
     * 과연 이 테스트가 항상 통과하는 테스트일까요?
     * */
    @DisplayName("영업 시간 내에 주문 시 주문이 생성됩니다.")
    @Test
    public void createOrderTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 5, 15, 10, 0));

        assertThat(order.getBeverages().size()).isEqualTo(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @DisplayName("영업 시간 이외에 시간에 주문 시 주문할 수 없습니다.")
    @Test
    public void createOrderExceptionTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(), 1);

        assertThatThrownBy(
            () -> cafeKiosk.createOrder(LocalDateTime.of(2023, 5, 15, 9, 59))).isInstanceOf(
            IllegalArgumentException.class);
    }

    @DisplayName("주문한 음료들의 가격을 구합니다.")
    @Test
    public void totalBeveragePriceTest() throws Exception {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        cafeKiosk.add(americano, 1);
        cafeKiosk.add(latte, 1);

        assertThat(cafeKiosk.calculateTotalPrice()).isEqualTo(8500);
    }
}
