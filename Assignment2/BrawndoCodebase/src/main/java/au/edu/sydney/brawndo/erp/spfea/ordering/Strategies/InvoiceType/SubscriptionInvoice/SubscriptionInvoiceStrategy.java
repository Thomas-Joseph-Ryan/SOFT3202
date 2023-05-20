package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice;

/**
 * Interface for Invoices from subscription orders
 */
public interface SubscriptionInvoiceStrategy {

    /**
     * Method for generating subscription invoices
     * @param subscriptionInvoiceData Required data for thse invoices
     * @return The invoice data
     */
    String generateInvoiceData(SubscriptionInvoiceData subscriptionInvoiceData);
}
