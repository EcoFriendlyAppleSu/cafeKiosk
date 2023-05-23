package sample.cafekiosk.spring.api.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
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
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@ActiveProfiles("test")
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @DisplayName("신규 상품을 등록할 때, 상품 번호는 가장 최근 상품 번호에서 1 증가한 것이다.")
    @Test
    public void createProductTest() throws Exception {
        // given
        Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        productRepository.save(product);

        ProductCreateRequest request = getProductCreateRequest();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        List<Product> products = productRepository.findAll();

        assertThat(products).hasSize(2)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactlyInAnyOrder(
                tuple("001", HANDMADE, SELLING, "아메리카노", 4000),
                tuple("002", HANDMADE, SELLING, "롱고", 5000)
            );
    }

    @DisplayName("신규 상품을 등록할 때, 어떠한 상품이 존재하지 않다면 001 번을 부여한다.")
    @Test
    public void createProductWhenProductIsEmptyTest() throws Exception {
        // given
        ProductCreateRequest request = getProductCreateRequest();

        // when
        ProductResponse productResponse = productService.createProduct(request);

        // then
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
            .extracting("productNumber", "type", "sellingStatus", "name", "price")
            .containsExactly(tuple("001", HANDMADE, SELLING, "롱고", 5000));
    }

    private ProductCreateRequest getProductCreateRequest() {
        return ProductCreateRequest.builder()
            .type(HANDMADE)
            .sellingStatus(SELLING)
            .name("롱고")
            .price(5000)
            .build();
    }

    private Product createProduct(String productNumber, ProductType type,
        ProductSellingStatus sellingStatus, String name, int price) {
        return Product.builder()
            .productNumber(productNumber)
            .type(type)
            .sellingStatus(sellingStatus)
            .name(name)
            .price(price)
            .build();
    }
}
