package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.contact.Email;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class HandleEmail extends BaseHandler{

    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        String email = customer.getEmailAddress();
        if (null != email) {
            Email.sendInvoice(token, customer.getfName(), customer.getlName(), data, email);
            return true;
        } else {
            return super.handle(token, customer, data);
        }
    }
}
