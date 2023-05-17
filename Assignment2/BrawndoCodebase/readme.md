# Assignment 2: Design Patterns

## Solutions to the Key Issues

### RAM Issue

#### FlyWeight Pattern

To fix the RAM issue I will be employing the FlyWeight pattern. 

- ConcreteFlyweight: ProductFlyWeight
- FlyweightFactory: ProductFlyWeightFactory
- Client: ProductImpl

##### Explanation (I didn't realise we only had to justify if we do an alternate solution.. But I've written it now so ill keep it in along with any other explanations I've done so far.)

In order to maintain the interface for the ProductImpl object, I made the FlyWeightFactory
a singleton, so that the ProductImpl object can get the instance of the FlyWeightFactory without needing
to pass the factory object to the product through a setter or the constructor. 

I have identified the recipeData, marketingData, safetyData and licensingData to be the intrinsic
data that is saved in the shared object and name, cost and manufacturingData to be extrinsic. Although
since the ProductImpl object seems to be a value object it could be debated that all the data in the 
ProductImpl could be included in the FlyWeight and considered intrinsic as it cannot be modified
externally. But without the domain knowledge to be completely sure, I will limit the FlyWeight to 
just the 4 attributes.

### Too Many Orders

#### Alternative Solution (400 words max)

My general solution to this problem was to use the strategy pattern, however there are some idiosyncrasies to
how this has been implemented, so I will consider it an alternative solution. The idea is to extract all the non
duplicated codes from each class into separate strategies, such that there will only exist two base order classes,
the OneOffOrder class and the OrderSubscription class. I did this by identifying the 2 methods that changed 
dependent on what the specifics of the order are: the way total cost is calculated (discount type); the way
invoices are generated (business or not and subscription or not). This means I have 3 main strategy types: Discounts,
SubscriptionInvoiceTypes, OneOffInvoiceTypes. 

I employed the use of records to pass the necessary data to the appropriate strategy, which is why I needed to separate
the different invoice types, as OneOffInvoiceTypes do not need to know about recurringCosts where has Subscription ones 
do.

##### Solution Summary

The changes I have made are

- Deleted existing order classes
- Created generic order class for one-off orders all the methods that were common to one-off classes are maintained
in this class, and then the getTotalCost() method has been changed to use the pricing strat and generateInvoice() method
has been changed to use the OneOffInvoiceStrategy
- Created generic order class for subscriptions that extends the one-off order class, this class behaves in the same
way as the old class except it uses the SubscriptionInvoiceStrategy for its generateInvoice method(). Because this
class extends the OneOffOrder class, I needed to create two constructors for the OneOffOrder class as the super() method
in the constructor of the subscription class would not have been able to work with the public constructor of the OneOffOrder
class.
- Both of the classes copy() method have been changed to suit the new constructors.
- Created different strategies which are inside the ordering/strategies directory.
- Created a data structure for each type of strategy
- Changed the SPFEAFacade createOrder() method to use these strategies.

##### Solution Benefit

The benefit of this solution is that it is more extendable, and cuts down the total number of classes involved in the
order process from 264 to 78 (68 (pricing strategies + interface and costData) + 8 (invoice strategies + interfaces and data's) + 2
(actual order classes)) if my understanding of the specs are correct. This is already a large reduction, however
the better part of this solution is that it is more maintainable, and if there are more pricing strategies being added,
the number of classes only increases by 1 instead of 4 as it did before.

This method also makes the difference between each type of order clearer and you can specifically see the differences in
each order type by looking at its strategies.

### Bulky Contact Method

#### Chain of Responsibility

In order to slim down the method used for contacting the customer, I applied the Chain of Responsibility
design pattern

- Handler: ContactHandler
- BaseHandler: BaseHandler
- ConcreteHandler: HandleCarrierPigeon, HandleEmail, HandleMerchandiser, HandlePhoneCall, HandleSMS, HandleMail
- Client: SPFEAFacade

### System Lag

#### Lazy Loading

To reduce the system lag when loading a customer, I used the lazy loading design pattern. Specifically the Lazy Initialization
variant.

- Class: CustomerImpl

### Hard to Compare Products

#### Value Object

In order make products easier to compare, I completed the Value Object pattern that was
already being partially used on the ProductImpl as there was only getter methods and no setters.

- ValueObject: ProductImpl
- Client: All non-subscription Order types

When creating each product, a unique hashValue is generated. This hashValue effectively condenses the product's data into a compact, 
easily comparable form. The primary advantage of this hashValue is that it provides a quick, 
preliminary check for product equality, reducing the need for exhaustive and computationally 
expensive attribute-by-attribute comparisons.

In the overridden `equals()` method, the first step is to compare the hashValues of two products. 
If these hashValues are identical, it strongly suggests that the two products are likely to be 
the same. This is where the hashValue proves its worth – it enables a fast, initial equality 
check, saving time and computational resources.

However, hash functions, despite their usefulness, are not perfect and can lead to collisions 
(i.e., distinct products might end up with the same hashValue). Therefore, if two products have
the same hashValue, we conduct a secondary, more granular comparison. This involves checking 
each individual attribute of the products to confirm that they are indeed identical.

In this way, the hashValue serves as an efficient screening tool for equality, accelerating
the comparison process by reducing the need for full attribute comparisons. Yet, to ensure 
absolute accuracy in the face of potential hash collisions, we retain the thorough 
attribute-by-attribute comparison as a secondary, confirmatory step. This two-tiered process 
balances efficiency and precision in product comparison.

When overriding the `equals()` method it is imperative to also override the `hashcode()` method
to ensure correctness for datastructures like hashmaps which I have also done.

### Slow Order Creation

Change this to an item of work type of system, where once all of the changes are made it will be submitted in a single go, rather
then submitting things seperately

## Notes About the Submission