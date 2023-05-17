package sample.cafekiosk.spring.api.controller.order;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
    * 주문 생성이니 POST로 설정합니다.
    * Post로 데이터 전송하는 것이 번거롭습니다. 따라서, .http file을 만들어 restAPI를 빠르게 사용할 수 있습니다.
    * */
    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();
         return orderService.createOrder(request, now);
    }
}
