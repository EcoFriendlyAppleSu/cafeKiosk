package sample.cafekiosk.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.api.service.mail.MailService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderStatus;

@Service
public class OrderStatisticsService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    @Autowired
    public OrderStatisticsService(OrderRepository orderRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    /*
    * 아래와 같은 서비스에선 Transactional을 사용하지 않는 것이 좋습니다. why?
    * */
    public boolean sendOrderStatisticsMail(LocalDate orderDate, String email){
        // 해당 일자에 결제완려된 주문들을 가져와서
        List<Order> orders = orderRepository.findOrdersBy(
            orderDate.atStartOfDay(),
            orderDate.plusDays(1).atStartOfDay(),
            OrderStatus.PAYMENT_COMPLETED
        );

        // 총 매출 합계를 계산하고
        int totalIncome = orders.stream().mapToInt(Order::getTotalPrice).sum();

        // 메일을 전송합니다.
        boolean result = mailService.sendMail(
            "no-reply@cafekiost.com",
            email,
            String.format("[매출 통계] %s", orderDate),
            String.format("총 매출 합계는 %s원 입니다.", totalIncome)
        );

        if(!result){
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }
        return true;
    }
}
