package StoreAPI.Tests;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryTest {

    private Inventory sut;

    @BeforeAll
    public void createInventory() {
        sut = new Inventory();
    }

    @Test
    public void testGetInstance() {

        assertNotNull(sut.getInstance());
    }

    @Test
    public void testAddProduct() {
        // create a new inventory instance
        Inventory inventory = new Inventory();

        // add a valid product with a valid quantity
        Product validProduct = new Product("valid product", 10.0);
        int validQuantity = 5;
        inventory.addProduct(validProduct, validQuantity);

        // verify that the product was added correctly
        List<Product> products = inventory.getProducts();
        assertEquals(1, products.size());
        assertEquals(validProduct, products.get(0));
        assertEquals(validQuantity, inventory.getProductQuantity(validProduct));

        // try to add a product with a negative quantity
        Product negativeQuantityProduct = new Product("negative quantity product", 20.0);
        int negativeQuantity = -1;
        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(negativeQuantityProduct, negativeQuantity));

        // try to add a null product
        assertThrows(IllegalArgumentException.class, () -> inventory.addProduct(null, validQuantity));
    }

    /*
    This task is incomplete, but i think I have learned from it so its okay.
     */
}
