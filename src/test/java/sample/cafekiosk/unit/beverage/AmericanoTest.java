package sample.cafekiosk.unit.beverage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AmericanoTest {

    @Test
    public void nameTest() throws Exception {
        var americano = new Americano();
        assertThat(americano.getName()).isEqualTo("아메리카노");
    }

}
