package sample.cafekiosk.spring.domain.orderproduct;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.product.Product;


/*
* OrderProduct의 존재는 왜 필요한 것일까요?
* 존재하지 않고 Order와 Product 객체만으로 해결할 수 없는 부분이 있나요?
* */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY) // xToOne 때 EAGER보다 지연로딩(LAZY)를 사용하자
    private Order order;

    @ManyToOne(fetch = LAZY)
    private Product product;

    @Builder
    public OrderProduct(Order order, Product product) {
        this.order = order;
        this.product = product;
    }
}
