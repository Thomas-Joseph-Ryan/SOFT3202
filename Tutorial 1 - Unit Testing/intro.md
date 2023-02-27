# Introduction to Unit Testing

## Given - When - Then

### Question context 

Specification for class:  ShoppingBasket

Constructor:

ShoppingBasket()

Default constructor, throws no exceptions.

Methods:

Nothing addItem(String item, Number count)

addItem takes a String parameter that must be one of ‘apple’, ‘orange’, ‘pear’, or ‘banana’. If a different item is given, or an empty/null item, then the method throws an ArgumentException. addItem takes a Number parameter that must be an integer which is 1 or more. If the Number is not an integer or is less than 1 then the method throws an NumberException. addItem adds the ‘count’ of that ‘item’ to the basket.



Boolean removeItem(String item, Number count)

removeItem takes a String parameter, which must not be Nothing but may be any String. If it is Nothing then the method throws an ArgumentException. removeItem takes a Number parameter that must be an integer which is 1 or more. If the Number is not an integer or is less than 1 then the method throws an NumberException. If the item does not exist, or the count is more than the current count of the matching item, nothing is changed in the basket and removeItem returns false. Otherwise, removeItem removes the ‘count’ of that ‘item’ from the basket, and returns true. If the count would be reduced to 0 the item record is removed entirely.



List Of String and Number Pairs getItems()

getItems returns a list of all items currently held in this basket along with their counts. getItems never returns Nothing, but may return an empty list.



Number getValue()

getValue returns the current sale value of the combined cart contents. Apples are worth $2.50, Oranges $1.25, Pears $3.00, and Bananas $4.95. If the cart is empty getValue returns Nothing, not zero.



Nothing clear()

clear empties this basket, removing all items.

### Answer

#### addItem()


GIVEN empty cart  
WHEN addItem(‘banana’, 1)  
THEN getItems() returns [('banana', 1)]

GIVEN empty cart  
WHEN addItem(‘mango’, 1)  
THEN ArgumentException

GIVEN empty cart  
WHEN addItem(‘banana’, 0)  
THEN ArgumentException

GIVEN empty cart  
WHEN addItem(‘apple’, ‘banana’)  
THEN ArgumentException

GIVEN empty cart  
WHEN addItem('apple' 5)  
THEN getItems() returns [('apple', 5)]

GIVEN 5 apples already in the cart  
WHEN addItem('orange', 2)  
THEN getItems() returns [('apple', 5), ('orange', 2)]

GIVEN empty cart  
WHEN addItem(null, 3)  
THEN ArgumentException

GIVEN empty cart  
WHEN addItem(,3)  
THEN ArgumentException


//Edge case
GIVEN empty cart 
WHEN addItem("manga", -1.1)
THEN ArgumentException


### removeItem()

GIVEN empty cart  
WHEN removeItem(,1)  
THEN ArgumentException

GIVEN empty cart  
WHEN removeItem('apple', 0)  
THEN NumberException

GIVEN empty cart  
WHEN removeItem('apple', '1')  
THEN NumberException

GIVEN empty cart  
WHEN removeItem('mango', 1)  
THEN false

GIVEN empty cart  
WHEN removeItem('apple', 1)  
THEN false

GIVEN cart with 2 apples  
WHEN removeItem('apple', 1)  
THEN true




GIVEN nothing in cart
WHEN getValue()
THEN nothing returned
