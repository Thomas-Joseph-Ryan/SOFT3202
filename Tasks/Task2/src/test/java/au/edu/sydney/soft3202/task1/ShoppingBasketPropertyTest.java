package au.edu.sydney.soft3202.task1;

import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import net.jqwik.api.Property;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingBasketPropertyTest {

    @Property
    void getValueEmptyPropertyTest() {
        ShoppingBasket sb = new ShoppingBasket();
        assertThat(sb.getValue()).isEqualTo(null);
    }
}
