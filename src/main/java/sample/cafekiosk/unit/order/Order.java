package sample.cafekiosk.unit.order;

import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import sample.cafekiosk.unit.beverage.Beverage;

@Getter
@RequiredArgsConstructor
public class Order {

    private final Instant orderDateTime;
    private final List<Beverage> beverages;

}
