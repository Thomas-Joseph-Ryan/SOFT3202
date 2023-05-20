package au.edu.sydney.brawndo.erp.spfea;

import au.edu.sydney.brawndo.erp.auth.AuthToken;
import au.edu.sydney.brawndo.erp.database.TestDatabase;
import au.edu.sydney.brawndo.erp.ordering.Customer;

public class CustomerImpl implements Customer {

    private final int id;
    private final AuthToken token;
    private String fName;
    private String lName;
    private String phoneNumber;
    private String emailAddress;
    private String address;
    private String suburb;
    private String state;
    private String postCode;
    private String merchandiser;
    private String businessName;
    private String pigeonCoopID;

    /**
     * Assigns known data to object attributes now, all unknown objects
     * are only looked up when needed using lazy loading
     * @param token Authority token
     * @param id Customer id
     */
    public CustomerImpl(AuthToken token, int id) {
        this.id = id;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    /**
     * Gets customer fName from database if not gotten before.
     * All other get methods work the same way in this class
     * @return Customer first name
     */
    @Override
    public String getfName() {
        if (this.fName == null) {
            this.fName = TestDatabase.getInstance().getCustomerField(token, id, "fName");
        }
        return fName;
    }

    @Override
    public String getlName() {
        if (this.lName == null) {
            this.lName = TestDatabase.getInstance().getCustomerField(token, id, "lName");
        }
        return lName;
    }

    @Override
    public String getPhoneNumber() {
        if (this.phoneNumber == null) {
            this.phoneNumber = TestDatabase.getInstance().getCustomerField(token, id, "phoneNumber");
        }
        return phoneNumber;
    }

    @Override
    public String getEmailAddress() {
        if (this.emailAddress == null) {
            this.emailAddress = TestDatabase.getInstance().getCustomerField(token, id, "emailAddress");
        }
        return emailAddress;
    }

    @Override
    public String getAddress() {
        if (this.address == null) {
            this.address = TestDatabase.getInstance().getCustomerField(token, id, "address");
        }
        return address;
    }

    @Override
    public String getSuburb() {
        if (this.suburb == null) {
            this.suburb = TestDatabase.getInstance().getCustomerField(token, id, "suburb");
        }
        return suburb;
    }

    @Override
    public String getState() {
        if (this.state == null) {
            this.state = TestDatabase.getInstance().getCustomerField(token, id, "state");
        }
        return state;
    }

    @Override
    public String getPostCode() {
        if (this.postCode == null) {
            this.postCode = TestDatabase.getInstance().getCustomerField(token, id, "postCode");
        }
        return postCode;
    }

    @Override
    public String getMerchandiser() {
        if (this.merchandiser == null) {
            this.merchandiser = TestDatabase.getInstance().getCustomerField(token, id, "merchandiser");
        }
        return merchandiser;
    }

    @Override
    public String getBusinessName() {
        if (this.businessName == null) {
            this.businessName = TestDatabase.getInstance().getCustomerField(token, id, "businessName");
        }
        return businessName;
    }

    @Override
    public String getPigeonCoopID() {
        if (this.pigeonCoopID == null) {
            this.pigeonCoopID = TestDatabase.getInstance().getCustomerField(token, id, "pigeonCoopID");
        }
        return pigeonCoopID;
    }
}
