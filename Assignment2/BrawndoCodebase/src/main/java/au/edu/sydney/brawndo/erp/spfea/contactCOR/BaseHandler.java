package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class BaseHandler implements ContactHandler{

    private ContactHandler next;

    public void setNext(ContactHandler next) {
        this.next = next;
    }

    /**
     * Providing the base functionality for this method.
     * @param token Authorisation token
     * @param customer Customer being sent the invoice
     * @param data Invoice data
     * @return true if handled, false if not.
     */
    @Override
    public Boolean handle(AuthToken token, Customer customer, String data) {
        if (next != null) {
            return next.handle(token, customer, data);
        } else {
            return false;
        }
    }

}
