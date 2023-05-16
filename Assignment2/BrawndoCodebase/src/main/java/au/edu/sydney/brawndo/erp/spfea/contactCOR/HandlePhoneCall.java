package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.contact.PhoneCall;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class HandlePhoneCall extends BaseHandler {
    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        String phone = customer.getPhoneNumber();
        if (null != phone) {
            PhoneCall.sendInvoice(token, customer.getfName(), customer.getlName(), data, phone);
            return true;
        } else {
            return super.handle(token, customer, data);
        }
    }
}
