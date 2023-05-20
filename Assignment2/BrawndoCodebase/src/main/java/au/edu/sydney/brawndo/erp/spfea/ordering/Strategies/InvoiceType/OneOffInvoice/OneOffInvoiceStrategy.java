package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice;


/**
 * Invoice strategy interface for one-off orders
 */
public interface OneOffInvoiceStrategy {

    String generateInvoiceData(OneOffInvoiceData oneOffInvoiceData);
}
