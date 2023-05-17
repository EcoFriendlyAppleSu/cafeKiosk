package sample.cafekiosk.spring.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductType;

class OrderTest {

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    public void orderTotalPriceTest() throws Exception {
        // given
        List<Product> products = List.of(
            creatProduct("001", 1000),
            creatProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 상태는 INIT입니다.")
    @Test
    public void orderInitTest() throws Exception {
        // given
        List<Product> products = List.of(
            creatProduct("001", 1000),
            creatProduct("002", 2000)
        );

        // when
        Order order = Order.create(products, LocalDateTime.now());

        // then
        // 하나의 주문이 물품 전체를 갖고 있습니다. 따라서 Order(주문)의 상태만 검증하면 됩니다.
        assertThat(order.getOrderStatus()).isEqualByComparingTo(OrderStatus.INIT);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    public void registeredDateTimeTest() throws Exception {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = List.of(
            creatProduct("001", 1000),
            creatProduct("002", 2000)
        );

        // when
        // 테스트하기 까다로운 값을 바깥으로 빼서 테스트하기 쉽게 만듭니다.
        Order order = Order.create(products, registeredDateTime);

        // then
        // 하나의 주문이 물품 전체를 갖고 있습니다. 따라서 Order(주문)의 상태만 검증하면 됩니다.
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private Product creatProduct(String productNumber, int price) {
        return Product.builder()
            .type(ProductType.HANDMADE)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("Anything")
            .build();
    }
}
