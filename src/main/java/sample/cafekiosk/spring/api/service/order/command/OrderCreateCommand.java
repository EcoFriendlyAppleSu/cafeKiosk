package sample.cafekiosk.spring.api.service.order.command;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCreateCommand {

    private List<String> productNumbers;

    @Builder
    private OrderCreateCommand(List<String> productNumbers) {
        this.productNumbers = productNumbers;
    }
}
