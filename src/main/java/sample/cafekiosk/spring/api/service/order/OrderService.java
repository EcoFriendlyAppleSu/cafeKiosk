package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.api.service.order.command.OrderCreateCommand;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;
import sample.cafekiosk.spring.domain.stock.Stock;
import sample.cafekiosk.spring.domain.stock.StockRepository;



@Transactional
@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;

    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository,
        StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.stockRepository = stockRepository;
    }

    public OrderResponse createOrder(OrderCreateCommand command, LocalDateTime time) {
        List<String> productNumbers = command.getProductNumbers();
        List<Product> duplicateProducts = findProductsBy(productNumbers);

        deductStockQuantities(duplicateProducts);

        Order order = Order.create(duplicateProducts, time);
        Order saveOrder = orderRepository.save(order);

        return OrderResponse.of(saveOrder);
    }

    /*
    * Kiosk가 한 대가 아닐 때, 어떤 방식으로 동시성 처리를 할 것인지 생각해봅시다.
    * Hint, Optimistic lock / Pessimistic lock / ...
    * */
    private void deductStockQuantities(List<Product> duplicateProducts) {
        // 재고 차감 체크가 필요한 상품들 filter
        List<String> stockProductNumbers = extractStockProductNumbers(duplicateProducts);

        /*
        재고 Entity 조회,
        iterator를 모두 순회하게 되면 성능에 좋지 않습니다. 따라서, Map을 사용합니다.
        */
        Map<String, Stock> stockMap = createStockMapBy(stockProductNumbers);

        // 상품별 Counting
        Map<String, Long> productCountingMap = createCountingMapBy(stockProductNumbers);

        /*
        재고 차감 시도, Stock에도 검증과정이 존재하는데 왜 또 검증할까요?
        _> application Layer 에서 검증하는 것과 Stock Entity에서 검증하는 것은 다른 것입니다.
        _> 관점을 다르게 봐야합니다. 중복이라고 볼 수 있지만 서로 다른 기능을 수행합니다.
        */
        for (String stockProductNumber : new HashSet<>(stockProductNumbers)) {
            Stock stock = stockMap.get(stockProductNumber);
            int quantity = productCountingMap.get(stockProductNumber).intValue();

            if(stock.isQuantityLessThan(quantity)){
                throw new IllegalArgumentException("재고가 부족한 상품이 있습니다.");
            }
            stock.deductQuantity(quantity);
        }
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

    private List<String> extractStockProductNumbers(List<Product> duplicateProducts) {
        return duplicateProducts.stream()
            .filter(product -> ProductType.containsStockType(product.getType()))
            .map(Product::getProductNumber)
            .collect(Collectors.toList());
    }

    private Map<String, Stock> createStockMapBy(List<String> stockProductNumbers) {
        List<Stock> stocks = stockRepository.findAllByProductNumberIn(
            stockProductNumbers);
        return stocks.stream()
            .collect(Collectors.toMap(Stock::getProductNumber, stock -> stock));
    }

    private Map<String, Long> createCountingMapBy(List<String> stockProductNumbers) {
        return stockProductNumbers.stream()
            .collect(Collectors.groupingBy(product -> product, Collectors.counting()));
    }
}
