package sample.cafekiosk.spring.domain.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.spring.domain.product.ProductType.containsStockType;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ProductTypeTest {

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    public void productTypeNotContainTest() throws Exception {
        // given
        ProductType handmade = ProductType.HANDMADE;

        // when
        boolean result = containsStockType(handmade);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    @Test
    public void productTypeContainTest() throws Exception {
        // given
        ProductType bakery = ProductType.BAKERY;

        // when
        boolean result = containsStockType(bakery);

        // then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"HANDMADE, false","BOTTLE, true","BAKERY, true"})
    public void productTypeContainUsingParameterizedTest(ProductType productType, boolean expected) throws Exception {
        // when
        boolean result = containsStockType(productType);

        // then
        assertThat(result).isEqualTo(expected);
    }

}
