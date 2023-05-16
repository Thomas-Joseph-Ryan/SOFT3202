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

I think my alternative solution will consist of creating strategies for the different
types of classes as there are only specific methods that rely on specific aspects of that
order type.

For example, all orders will have the same type of invoice, the same way that each
subscription will have the same type of invoice, so instead of having a different class
for each type, we can just pass in the strategy for that behaviour.

This would already reduce class load by half from 264 to 132. 

Not sure if i can do the same thing for the flat vs bulk discount part but 
if so, then could reduce total class load to 66 from 132, as low as it can go
as long as each of those seperate order classes must exist.

##### Solution Summary

The solution summary goes here, you should describe what changes you have made to the codebase, be specific to the classes and methods you changed and how.

##### Solution Benefit

How did you solution solve the problem, be brief.

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