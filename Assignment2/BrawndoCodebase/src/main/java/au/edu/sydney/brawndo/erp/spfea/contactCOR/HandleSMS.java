package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.contact.SMS;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class HandleSMS extends BaseHandler{

    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        String smsPhone = customer.getPhoneNumber();
        if (null != smsPhone) {
            SMS.sendInvoice(token, customer.getfName(), customer.getlName(), data, smsPhone);
            return true;
        } else {
            return super.handle(token, customer, data);
        }
    }
}
