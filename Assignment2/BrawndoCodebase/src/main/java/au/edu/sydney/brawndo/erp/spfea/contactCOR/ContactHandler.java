package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.ordering.Customer;

import java.util.Arrays;
import java.util.List;

/**
 * Interface for how the chain of command classes will work along with static method providing
 * all methods available for contact
 */
public interface ContactHandler {


    /**
     * Sets the next handler in the chain
     * @param h Next handler
     */
    void setNext(ContactHandler h);

    /**
     * Checks if there is a handler after the current, if not returns false.
     * If along the chain of contact handlers, a handler is able to make contact
     * (send the invoice), then true is returned
     * @param token Authorisation token
     * @param customer Customer being sent the invoice
     * @param data Invoice data
     * @return boolean true if sent, false if it could not be sent
     */
    Boolean handle(AuthToken token, Customer customer, String data);

    public static List<String> getKnownMethods() {
        return Arrays.asList(
                "Carrier Pigeon",
                "Email",
                "Mail",
                "Merchandiser",
                "Phone call",
                "SMS"
        );
    }
}
