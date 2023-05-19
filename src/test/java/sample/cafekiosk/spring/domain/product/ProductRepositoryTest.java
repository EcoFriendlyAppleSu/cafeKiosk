package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.STOP_SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // yml configuration file의 on-profile를 참조합니다.
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    public void findAllBySellingStatusInTest() throws Exception {
        // given
        Product product1 = createProduct("001", HANDMADE);

        Product product2 = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("라떼")
            .price(4500)
            .build();

        Product product3 = Product.builder()
            .productNumber("003")
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllBySellingStatusIn(
            List.of(SELLING, HOLD));

        // then
        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", SELLING),
                tuple("002", "라떼", HOLD)
            );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회합니다.")
    @Test
    public void findAllByProductNumberInTest() throws Exception {
        // given
        Product product1 = createProduct("001", HANDMADE);

        Product product2 = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("라떼")
            .price(4500)
            .build();

        Product product3 = Product.builder()
            .productNumber("003")
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        List<Product> products = productRepository.findAllByProductNumberIn(
            List.of("001", "002"));

        // then
        assertThat(products.size()).isNotNull();

        assertThat(products).hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", SELLING),
                tuple("002", "라떼", HOLD)
            );
    }

    @DisplayName("상품번호 리스트의 마지막 상품 번호를 조회합니다.")
    @Test
    public void findLatestProductTest() throws Exception {
        // given

        String targetProductNumber = "003";

        Product product1 = createProduct("001", HANDMADE);

        Product product2 = Product.builder()
            .productNumber("002")
            .type(HANDMADE)
            .sellingStatus(HOLD)
            .name("라떼")
            .price(4500)
            .build();

        Product product3 = Product.builder()
            .productNumber(targetProductNumber)
            .type(HANDMADE)
            .sellingStatus(STOP_SELLING)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1, product2, product3));

        // when
        String latestProductNumber = productRepository.findLatestProduct();

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    private Product createProduct(String productNumber, ProductType type) {
        Product product1 = Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
        return product1;
    }

}
