package au.edu.sydney.soft3202.task1;

import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import net.jqwik.api.*;
import net.jqwik.api.lifecycle.BeforeTry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.Map;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingBasketPropertyTest {

    ShoppingBasket sut;

    @BeforeTry
    public void setup() {
        ShoppingBasket.resetInstance();
        sut = ShoppingBasket.getInstance("A");
    }

    @Property
    void testCostReflectsNumberOfItems(@ForAll("itemCostMap") Map.Entry<String, Double> itemCostEntry,
                                       @ForAll("positiveIntegers") int n) {
        String item = itemCostEntry.getKey();
        Double cost = itemCostEntry.getValue();

        sut.addItem(item, n);

        double expectedCost = cost * n;

        assertThat(sut.getValue()).isEqualTo(expectedCost);
    }

    @Property
    void testCostIsZeroAfterRemovingAllItems(@ForAll("itemCostMap") Map.Entry<String, Double> itemCostEntry,
                                       @ForAll("positiveIntegers") int n) {
        String item = itemCostEntry.getKey();
        Double cost = itemCostEntry.getValue();

        sut.addItem(item, n);
        sut.removeItem(item, n);

//        Assert null because the if the total value of the basket is zero, get value returns null
        Assertions.assertNull(sut.getValue());
    }

    @Provide
    Arbitrary<Map.Entry<String, Double>> itemCostMap() {
        Map<String, Double> itemCosts = Map.of("apple", 2.5, "banana", 4.95, "orange", 1.25, "pear", 3.0);
        Random random = new Random();
        int index = random.nextInt(itemCosts.size());
        String item = (String) itemCosts.keySet().toArray()[index];
        Double cost = itemCosts.get(item);
        return Arbitraries.just(Map.entry(item, cost));
    }

    @Provide
    Arbitrary<Integer> positiveIntegers() {
        return Arbitraries.integers().greaterOrEqual(1);
    }
}
