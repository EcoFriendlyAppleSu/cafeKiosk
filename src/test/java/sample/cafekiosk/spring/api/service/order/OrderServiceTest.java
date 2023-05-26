package sample.cafekiosk.spring.api.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.BAKERY;
import static sample.cafekiosk.spring.domain.product.ProductType.BOTTLE;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        // productRepository.deleteAll();
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    public void createOrderTest() throws Exception {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = creatProduct(HANDMADE, "001", 1000);
        Product product2 = creatProduct(HANDMADE, "002", 3000);
        Product product3 = creatProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();

        // when
        OrderResponse response = orderService.createOrder(request.toCommand(), registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();

        // 등록 시간은 어떻게 검증할까
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 4000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("002", 3000)
            );
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    public void createOrderDuplicateProductTest() throws Exception {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = creatProduct(HANDMADE, "001", 1000);
        Product product2 = creatProduct(HANDMADE, "002", 3000);
        Product product3 = creatProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001"))
            .build();

        // when
        OrderResponse response = orderService.createOrder(request.toCommand(), registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();

        // 등록 시간은 어떻게 검증할까
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 2000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000)
            );
    }

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    public void createOrderWithStockTest() throws Exception {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = creatProduct(BOTTLE, "001", 1000);
        Product product2 = creatProduct(BAKERY, "002", 3000);
        Product product3 = creatProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

        // when
        OrderResponse response = orderService.createOrder(request.toCommand(), registeredDateTime);

        // then
        assertThat(response.getId()).isNotNull();

        // 등록 시간은 어떻게 검증할까
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 10000);
        assertThat(response.getProducts()).hasSize(4)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000),
                tuple("002", 3000),
                tuple("003", 5000)
            );

        // 재고 확인
        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 0),
                tuple("002", 1)
            );
    }


    @DisplayName("재고가 부족한 상품으로 주문을 생성하는 경우 예외가 발생한다.")
    @Test
    public void createOrderWithoutNoStockTest() throws Exception {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        Product product1 = creatProduct(BOTTLE, "001", 1000);
        Product product2 = creatProduct(BAKERY, "002", 3000);
        Product product3 = creatProduct(HANDMADE, "003", 5000);

        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stock1.deductQuantity(1); // TODO: 2023/05/18 이렇게 테스트를 작성하면 안됩니다.
        stockRepository.saveAll(List.of(stock1, stock2));


        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

        // when, then
        assertThatThrownBy(() -> orderService.createOrder(request.toCommand(), registeredDateTime) ).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("재고가 부족한 상품이 있습니다.");
    }

    /*
     * 검증이 필요한 최소한의 값을 가진 Product Generator Method
     * */
    private Product creatProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("Anything")
            .build();
    }

}
