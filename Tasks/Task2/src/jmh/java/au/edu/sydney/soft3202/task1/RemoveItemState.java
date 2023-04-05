package au.edu.sydney.soft3202.task1;

import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Thread)
public class RemoveItemState {
    public ShoppingBasket sut;

    @Setup(Level.Invocation)
    public void setUp() {
        ShoppingBasket.resetInstance();
        sut = ShoppingBasket.getInstance("A");
        sut.addItem("pear", 1);
    }
}
