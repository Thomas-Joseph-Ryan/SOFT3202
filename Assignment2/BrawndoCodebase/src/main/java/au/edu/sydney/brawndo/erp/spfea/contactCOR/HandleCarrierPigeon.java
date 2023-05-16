package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.contact.CarrierPigeon;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class HandleCarrierPigeon extends BaseHandler {
    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        String pigeonCoopID = customer.getPigeonCoopID();
        if (null != pigeonCoopID) {
            CarrierPigeon.sendInvoice(token, customer.getfName(), customer.getlName(), data, pigeonCoopID);
            return true;
        } else {
            return super.handle(token, customer, data);
        }
    }
}
