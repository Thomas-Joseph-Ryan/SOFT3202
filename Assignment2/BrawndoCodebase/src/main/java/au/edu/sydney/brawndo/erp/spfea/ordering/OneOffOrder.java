package au.edu.sydney.brawndo.erp.spfea.ordering;

import au.edu.sydney.brawndo.erp.ordering.Order;
import au.edu.sydney.brawndo.erp.ordering.Product;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts.CostData;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts.PricingStrategy;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceData;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * General Order class for one-off orders
 */
@SuppressWarnings("Duplicates")
public class OneOffOrder implements Order {
    private Map<Product, Integer> products = new HashMap<>();
    private int id;
    private LocalDateTime date;
    private int customerID;
    private boolean finalised = false;
    protected final PricingStrategy pricingStrategy;
    private final OneOffInvoiceStrategy oneOffInvoiceStrategy;

    /**
     * Constructor for one off oder
     * @param id Order id
     * @param date Date of the order
     * @param customerID ID of the customer
     * @param pricingStrategy Strategy for how the total cost will be calculated
     * @param oneOffInvoiceStrategy strategy for how the invoice will be created
     */
    public OneOffOrder(int id, LocalDateTime date, int customerID, PricingStrategy pricingStrategy, OneOffInvoiceStrategy oneOffInvoiceStrategy) {
        this.id = id;
        this.date = date;
        this.customerID = customerID;
        this.pricingStrategy = pricingStrategy;
        this.oneOffInvoiceStrategy = oneOffInvoiceStrategy;
    }

    /**
     * Constructor for the subscription Orders super() method, needs a separate constructor as
     * a subscription order has a different type of invoice generator
     * @param id order id
     * @param date date of order
     * @param customerID Id of customer
     * @param pricingStrategy Strategy for how total cost is calculated
     */
    protected OneOffOrder(int id, LocalDateTime date, int customerID, PricingStrategy pricingStrategy) {
        this.id = id;
        this.date = date;
        this.customerID = customerID;
        this.pricingStrategy = pricingStrategy;
        this.oneOffInvoiceStrategy = null;
    }

    @Override
    public LocalDateTime getOrderDate() {
        return date;
    }

    @Override
    public void setProduct(Product product, int qty) {
        if (finalised) throw new IllegalStateException("Order was already finalised.");

        // We can't rely on like products having the same object identity since they get
        // rebuilt over the network, so we had to check for presence and same values

        for (Product contained: products.keySet()) {
            if (contained.equals(product)) {
                product = contained;
                break;
            }
        }

        products.put(product, qty);
    }

    @Override
    public Set<Product> getAllProducts() {
        return products.keySet();
    }

    @Override
    public int getProductQty(Product product) {
        // We can't rely on like products having the same object identity since they get
        // rebuilt over the network, so we had to check for presence and same values

        for (Product contained: products.keySet()) {
            if (contained.equals(product)) {
                product = contained;
                break;
            }
        }

        Integer result = products.get(product);
        return null == result ? 0 : result;
    }

    @Override
    public int getCustomer() {
        return customerID;
    }

    @Override
    public Order copy() {
        Order copy = new OneOffOrder(id, date, customerID, pricingStrategy, oneOffInvoiceStrategy);
        for (Product product: products.keySet()) {
            copy.setProduct(product, products.get(product));
        }

        return copy;
    }

    /**
     * Uses the invoice strategy to generate the invoice string
     * @return InvoiceData
     */
    @Override
    public String generateInvoiceData() {
        OneOffInvoiceData oneOffInvoiceData = new OneOffInvoiceData(this.getTotalCost(), this.products);
        return oneOffInvoiceStrategy.generateInvoiceData(oneOffInvoiceData);
    }

    /**
     * Uses the pricing strategy to calculate the total cost of the order
     * @return Total cost for the order
     */
    @Override
    public double getTotalCost() {
        CostData costData = new CostData(this.products);
        return this.pricingStrategy.getTotalCost(costData);
    }

    protected Map<Product, Integer> getProducts() {
        return products;
    }

    @Override
    public int getOrderID() {
        return id;
    }

    @Override
    public void finalise() {
        this.finalised = true;
    }

    @Override
    public String shortDesc() {
        return String.format("ID:%s $%,.2f", id, getTotalCost());
    }

    @Override
    public String longDesc() {
        double fullCost = 0.0;
        double discountedCost = getTotalCost();
        StringBuilder productSB = new StringBuilder();

        List<Product> keyList = new ArrayList<>(products.keySet());
        keyList.sort(Comparator.comparing(Product::getProductName).thenComparing(Product::getCost));

        for (Product product: keyList) {
            double subtotal = product.getCost() * products.get(product);
            fullCost += subtotal;

            productSB.append(String.format("\tProduct name: %s\tQty: %d\tUnit cost: $%,.2f\tSubtotal: $%,.2f\n",
                    product.getProductName(),
                    products.get(product),
                    product.getCost(),
                    subtotal));
        }

        return String.format(finalised ? "" : "*NOT FINALISED*\n" +
                        "Order details (id #%d)\n" +
                        "Date: %s\n" +
                        "Products:\n" +
                        "%s" +
                        "\tDiscount: -$%,.2f\n" +
                        "Total cost: $%,.2f\n",
                id,
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                productSB.toString(),
                fullCost - discountedCost,
                discountedCost
        );
    }

    protected boolean isFinalised() {
        return finalised;
    }

}
