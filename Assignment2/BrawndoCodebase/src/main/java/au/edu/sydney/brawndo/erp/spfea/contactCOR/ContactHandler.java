package au.edu.sydney.brawndo.erp.spfea.contactCOR;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.ordering.Customer;

import java.util.Arrays;
import java.util.List;

public interface ContactHandler {

    void setNext(ContactHandler h);

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
