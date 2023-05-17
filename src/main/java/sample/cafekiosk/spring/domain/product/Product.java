package sample.cafekiosk.spring.domain.product;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING) // DB에 String으로 적재
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    // @Column과 같은 Annotation은 생략 가능합니다.
    private int price;

    /*
    * Order의 정보가 필요할까요?
    * _> 아닙니다. 상품이 "자신"이 어느 주문에 포함되어 있는지 알 필요가 없기 때문입니다.
    * */

    @Builder
    public Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus,
        String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
