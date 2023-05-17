package au.edu.sydney.brawndo.erp.spfea;

import au.edu.sydney.brawndo.erp.auth.AuthModule;
import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.database.TestDatabase;
import au.edu.sydney.brawndo.erp.ordering.Customer;
import au.edu.sydney.brawndo.erp.ordering.Order;
import au.edu.sydney.brawndo.erp.ordering.Product;
import au.edu.sydney.brawndo.erp.spfea.contactCOR.*;
import au.edu.sydney.brawndo.erp.spfea.ordering.*;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts.BulkDiscount;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts.FlatRateDiscount;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts.PricingStrategy;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceStrategy;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffBusinessOneOffInvoice;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffPersonalCustomerOneOffInvoice;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice.SubscriptionBusinessInvoice;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice.SubscriptionInvoiceStrategy;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice.SubscriptionPersonalInvoice;
import au.edu.sydney.brawndo.erp.spfea.products.ProductDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
public class SPFEAFacade {
    private AuthToken token;

    public boolean login(String userName, String password) {
        token = AuthModule.login(userName, password);

        return null != token;
    }

    public List<Integer> getAllOrders() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();

        List<Order> orders = database.getOrders(token);

        List<Integer> result = new ArrayList<>();

        for (Order order: orders) {
            result.add(order.getOrderID());
        }

        return result;
    }

    public Integer createOrder(int customerID, LocalDateTime date, boolean isBusiness, boolean isSubscription, int discountType, int discountThreshold, int discountRateRaw, int numShipments) {
        if (null == token) {
            throw new SecurityException();
        }

        if (discountRateRaw < 0 || discountRateRaw > 100) {
            throw new IllegalArgumentException("Discount rate not a percentage");
        }

        double discountRate = 1.0 - (discountRateRaw / 100.0);

        Order order;

        if (!TestDatabase.getInstance().getCustomerIDs(token).contains(customerID)) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        int id = TestDatabase.getInstance().getNextOrderID();

//        Ideally, you would have the order be created first, then we could use setters to
//        set the strategies used, however since the order interface cannot be changed, we
//        must set the strategies through the constructor

//        Decide on pricing strategy
        PricingStrategy pricingStrategy;

        if (discountType == 1) {
            pricingStrategy = new FlatRateDiscount(discountRate);
        } else if (discountType == 2) {
            pricingStrategy = new BulkDiscount(discountThreshold, discountRate);
        } else {
            return null;
        }

//        Decide on invoicing strategy and order type
        if (isSubscription) {
            SubscriptionInvoiceStrategy subscriptionInvoiceStrategy;
            if (isBusiness) {
                subscriptionInvoiceStrategy = new SubscriptionBusinessInvoice();
            } else {
                subscriptionInvoiceStrategy = new SubscriptionPersonalInvoice();
            }

            order = new OrderSubscription(id, date, customerID, numShipments, pricingStrategy, subscriptionInvoiceStrategy);

        } else {
            OneOffInvoiceStrategy oneOffInvoiceStrategy;
            if (isBusiness) {
                oneOffInvoiceStrategy = new OneOffBusinessOneOffInvoice();
            } else {
                oneOffInvoiceStrategy = new OneOffPersonalCustomerOneOffInvoice();
            }

            order = new OneOffOrder(id, date, customerID, pricingStrategy, oneOffInvoiceStrategy);
        }

        TestDatabase.getInstance().saveOrder(token, order);
        return order.getOrderID();
    }

    public List<Integer> getAllCustomerIDs() {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        return database.getCustomerIDs(token);
    }

    public Customer getCustomer(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        return new CustomerImpl(token, id);
    }

    public boolean removeOrder(int id) {
        if (null == token) {
            throw new SecurityException();
        }

        TestDatabase database = TestDatabase.getInstance();
        return database.removeOrder(token, id);
    }

    public List<Product> getAllProducts() {
        if (null == token) {
            throw new SecurityException();
        }

        return new ArrayList<>(ProductDatabase.getTestProducts());
    }

    ContactHandler baseHandler = new BaseHandler();
    ContactHandler currentLowestPriorityHandler = baseHandler;

    public boolean finaliseOrder(int orderID, List<String> contactPriority) {
        if (null == token) {
            throw new SecurityException();
        }

        if (null != contactPriority) {
            for (String method: contactPriority) {
                switch (method.toLowerCase()) {
                    case "merchandiser" -> {
                        ContactHandler handler = new HandleMerchandiser();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    case "email" -> {
                        ContactHandler handler = new HandleEmail();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    case "carrier pigeon" -> {
                        ContactHandler handler = new HandleCarrierPigeon();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    case "mail" -> {
                        ContactHandler handler = new HandleMail();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    case "phone call" -> {
                        ContactHandler handler = new HandlePhoneCall();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    case "sms" -> {
                        ContactHandler handler = new HandleSMS();
                        currentLowestPriorityHandler.setNext(handler);
                        currentLowestPriorityHandler = handler;
                    }
                    default -> {
                    }
                }
            }
        }

        if (currentLowestPriorityHandler == baseHandler) { // needs setting to default
            ContactHandler hMerch = new HandleMerchandiser();
            ContactHandler hEmail = new HandleEmail();
            ContactHandler hCP = new HandleCarrierPigeon();
            ContactHandler hMail = new HandleMail();
            ContactHandler hPhCall = new HandlePhoneCall();

            baseHandler.setNext(hMerch);
            hMerch.setNext(hEmail);
            hEmail.setNext(hCP);
            hCP.setNext(hMail);
            hMail.setNext(hPhCall);
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        order.finalise();
        TestDatabase.getInstance().saveOrder(token, order);
        return baseHandler.handle(token, getCustomer(order.getCustomer()), order.generateInvoiceData());
    }

    public void logout() {
        AuthModule.logout(token);
        token = null;
    }

    public double getOrderTotalCost(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);
        if (null == order) {
            return 0.0;
        }

        return order.getTotalCost();
    }

    public void orderLineSet(int orderID, Product product, int qty) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            return;
        }

        order.setProduct(product, qty);

        TestDatabase.getInstance().saveOrder(token, order);
    }

    public String getOrderLongDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            return null;
        }

        return order.longDesc();
    }

    public String getOrderShortDesc(int orderID) {
        if (null == token) {
            throw new SecurityException();
        }

        Order order = TestDatabase.getInstance().getOrder(token, orderID);

        if (null == order) {
            return null;
        }

        return order.shortDesc();
    }

    public List<String> getKnownContactMethods() {if (null == token) {
        throw new SecurityException();
    }

        return ContactHandler.getKnownMethods();
    }
}
