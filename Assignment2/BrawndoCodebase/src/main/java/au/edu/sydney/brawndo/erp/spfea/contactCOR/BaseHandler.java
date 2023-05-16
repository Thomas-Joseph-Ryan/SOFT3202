package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class BaseHandler implements ContactHandler{

    private ContactHandler next;

    public void setNext(ContactHandler next) {
        this.next = next;
    }

    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        if (next != null) {
            return next.handle(token, customer, data);
        } else {
            return false;
        }
    }

}
