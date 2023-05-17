package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice;

import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceData;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceStrategy;

public class OneOffBusinessOneOffInvoice implements OneOffInvoiceStrategy {
    @Override
    public String generateInvoiceData(OneOffInvoiceData oneOffInvoiceData) {
        return String.format("Your business account has been charged: $%,.2f" +
                "\nPlease see your Brawndo© merchandising representative for itemised details.", oneOffInvoiceData.totalCost());
    }
}
