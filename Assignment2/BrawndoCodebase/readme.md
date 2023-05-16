# Assignment 2: Design Patterns

## Solutions to the Key Issues

### RAM Issue

#### FlyWeight Pattern

I will be implementing fly weight pattern to fix this issue. Need to wait for 
tests to be fixed so I can see what are the intrinsic vs extrinsic variables
inside each product. Currently im thinking all of the variables are instrinsic
because they dont seem to be modified from anywhere, however testing will
help me to figure that out.

The idea here is I will have the productImpl constructor ask the fly weight 
factory if there is an flyWeightItem with the same x variables, if so then return that 
object, otherwise, create a new flyWeightItem with those x variables

- participant name (as defined in the lectures): correlated java class
- participant name (as defined in the lectures): correlated java class
- participant name (as defined in the lectures): correlated java class

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

Change this to a chain of responsibility design pattern, such that if it is not ones responsibility it passes it to the next.

### System Lag

Create a software sublayer that is accessed instead of the database, so when we initially load the application, we
should load from the database, but when we do that we should create a copy of the customers (or at least part of, maybe like a
lazy loading thing) that gets accessed instead of the actual database.

### Hard to Compare Products

Create a hash for a product, and then when comparing products, compare their hashs.

### Slow Order Creation

Change this to an item of work type of system, where once all of the changes are made it will be submitted in a single go, rather
then submitting things seperately

## Notes About the Submission