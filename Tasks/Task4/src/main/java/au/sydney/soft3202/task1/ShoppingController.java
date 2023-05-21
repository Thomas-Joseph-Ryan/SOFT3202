package au.sydney.soft3202.task1;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;

class Item extends RepresentationModel<Item>  {
    public Integer id;
    public String name;
    public String user;
    public int count;
    public Double cost;
    public Item(int id, String name, String user, int count, Double cost) {
           this.id = id;
           this.name = name;
           this.user = user;
           this.count = count;
           this.cost = cost;
    }
}


@RestController
@RequestMapping("/api/shop")
public class ShoppingController {

    Map<String, Double> costs = new HashMap<String, Double>();

    @Autowired
    private ShoppingBasketRepository shoppingBasket;

    @GetMapping("viewall")
    public List<Item> getBasket() {
        List<ShoppingBasket> fs = shoppingBasket.findAll();
        List<Item> bs = new LinkedList<Item>();
        for (ShoppingBasket f : fs) {
            if (!costs.containsKey(f.getName())) {
                costs.put(f.getName(), 0.00);
            }
            bs.add(new Item(f.getId(), f.getName(), f.getUser(), f.getCount(), costs.get(f.getName())));
        }
        return bs;
    }

    @GetMapping("users")
    public List<String> getUsers() {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();
        List<String> users = new LinkedList<>();
        for (ShoppingBasket basket : baskets) {
            users.add(basket.getUser());
        }

        return users;
    }

    public record ItemCost(String name, String cost) {}

    @PostMapping("costs")
    public ResponseEntity<Void> addItemCost(@RequestBody ItemCost itemCost) {

        costs.put(itemCost.name, Double.valueOf(itemCost.cost));

        return ResponseEntity.ok().build();
    }

    @PutMapping("costs/{name}")
    public ResponseEntity<Void> updateItemCost(@PathVariable String name, @RequestBody ItemCost itemCost) {
        if (costs.containsKey(name)) {
            costs.replace(name, Double.valueOf(itemCost.cost));
        }

        return ResponseEntity.ok().build();
    }
    @GetMapping("costs")
    public Map<String, Double> getCosts() {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();
        for (ShoppingBasket basket : baskets) {
            if (!costs.containsKey(basket.getName())) {
                costs.put(basket.getName(), 0.00);
            }
        }

        return costs;
    }

    record ItemAndStringCount(String name, String count) {
    }

    @PostMapping("users/{username}/add")
    public ResponseEntity<String> addItemToUserCart(@PathVariable String username, @RequestBody ItemAndStringCount itemAndCount) {
        if (costs.containsKey(itemAndCount.name)) {
            List<ShoppingBasket> baskets = shoppingBasket.findAll();
            for (ShoppingBasket basket : baskets) {
                if (basket.getUser().equals(username) && basket.getName().equals(itemAndCount.name)) {
                    Integer totalCount = basket.getCount() + Integer.parseInt(itemAndCount.count);
                    basket.setCount(totalCount);
                    shoppingBasket.save(basket);
                    return ResponseEntity.ok("Added to existing cart - total count = " + totalCount );
                }
            }
            ShoppingBasket basket = new ShoppingBasket();
            basket.setUser(username);
            basket.setCount(Integer.parseInt(itemAndCount.count));
            basket.setName(itemAndCount.name);
            shoppingBasket.save(basket);
            return ResponseEntity.ok("Created new cart");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    public record ItemCountString(String count) {}
    @PutMapping("users/{username}/basket/{itemName}")
    public ResponseEntity<String> updateItemInUserCart(@PathVariable String itemName, @RequestBody ItemCountString count, @PathVariable String username) {
        if (costs.containsKey(itemName)) {
            List<ShoppingBasket> baskets = shoppingBasket.findAll();
            for (ShoppingBasket basket : baskets) {
                if (basket.getUser().equals(username) && basket.getName().equals(itemName)) {
                    basket.setCount(Integer.parseInt(count.count));
                    shoppingBasket.save(basket);
                    return ResponseEntity.ok("Updated existing cart - total count = " + count.count );
                }
            }
            return ResponseEntity.ok("Count could not be updated as user does not have cart with this item");
        } else {
            return ResponseEntity.badRequest().body("Item does not exist in cost map");
        }
    }

    public record ItemAndIntCount(String name, Integer count){}

    @GetMapping("users/{username}")
    public List<ItemAndIntCount> getItemsOfUser(@PathVariable String username) {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();

        List<ItemAndIntCount> items = new LinkedList<>();
        for (ShoppingBasket basket : baskets) {
            if (basket.getUser().equals(username)) {
                items.add(new ItemAndIntCount(basket.getName(), basket.getCount()));
            }
        }

        return items;
    }

    @GetMapping("users/{username}/total")
    public Double getCostForUser(@PathVariable String username) {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();
        Double cost = 0.0;
        for (ShoppingBasket basket : baskets) {
            if (!costs.containsKey(basket.getName())) {
                costs.put(basket.getName(), 0.00);
            }

            if (basket.getUser().equals(username)) {
                cost += costs.get(basket.getName());
            }
        }
        return cost;
    }

    @DeleteMapping("users/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();
        for (ShoppingBasket basket : baskets) {
            if (basket.getUser().equals(username)) {
                shoppingBasket.delete(basket);
            }
        }
        return ResponseEntity.ok(username + " successfully deleted");
    }

    @DeleteMapping("users/{username}/basket/{itemName}")
    public ResponseEntity<String> deleteItemFromUserBasket(@PathVariable String username, @PathVariable String itemName) {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();
        for (ShoppingBasket basket : baskets) {
            if (basket.getUser().equals(username) && basket.getName().equals(itemName)) {
                shoppingBasket.delete(basket);
            }
        }
        return ResponseEntity.ok(itemName + " sucessfully deleted from " + username);
    }

}
