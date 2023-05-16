package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType;

public class BusinessInvoice implements InvoiceStrategy{
    @Override
    public String generateInvoiceData(InvoiceData invoiceData) {
        return String.format("Your business account has been charged: $%,.2f" +
                "\nPlease see your Brawndo© merchandising representative for itemised details.", invoiceData.getTotalCost());
    }
}
