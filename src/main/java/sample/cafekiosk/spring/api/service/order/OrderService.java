package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(OrderCreateRequest request, LocalDateTime time) {
        List<String> productNumbers = request.getProductNumbers();

        List<Product> duplicateProducts = findProductsBy(productNumbers);

        Order order = Order.create(duplicateProducts, time);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);
    }

    private List<Product> findProductsBy(List<String> productNumbers) {
        // Product
        List<Product> products = productRepository.findAllByProductNumberIn(
            productNumbers);

        // Map을 생성합니다.
        Map<String, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductNumber, product -> product));

        // ProductNumber로 된 데이터는 일정하기 때문에 아래와 같이 사용하면 중복 문제를 처리할 수 있습니다.
        return productNumbers.stream()
            .map(productMap::get)
            .collect(Collectors.toList());
    }
}
