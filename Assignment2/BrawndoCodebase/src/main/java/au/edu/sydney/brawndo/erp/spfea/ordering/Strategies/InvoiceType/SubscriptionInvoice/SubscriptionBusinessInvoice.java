package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice;

public class SubscriptionBusinessInvoice implements SubscriptionInvoiceStrategy {

    @Override
    public String generateInvoiceData(SubscriptionInvoiceData subscriptionInvoiceData) {
        return String.format("Your business account will be charged: $%,.2f each week, with a total overall cost of: $%,.2f" +
                "\nPlease see your Brawndo© merchandising representative for itemised details.", subscriptionInvoiceData.recurringCost(), subscriptionInvoiceData.totalCost());
    }
}
