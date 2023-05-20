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

    record ItemAndCount(String name, Integer count) {
    }

    @PostMapping("users/{username}/add")
    public ResponseEntity<Void> addItemToUserCart(@PathVariable String username, @RequestBody ItemAndCount itemAndCount) {
        if (costs.containsKey(itemAndCount.name)) {
            Boolean added = false;
            List<ShoppingBasket> baskets = shoppingBasket.findAll();
            for (ShoppingBasket basket : baskets) {
                if (basket.getUser().equals(username) && basket.getName().equals(itemAndCount.name)) {
                    basket.setCount(basket.getCount() + itemAndCount.count);
                    added = true;
                    break;
                }
            }
            if (!added) {
                ShoppingBasket shoppingBasket = new ShoppingBasket();
                shoppingBasket.setUser(username);
                shoppingBasket.setCount(itemAndCount.count);
                shoppingBasket.setName(itemAndCount.name);
//                Save basket?
            }
        }
    }

    @GetMapping("users/{username}")
    public List<ItemAndCount> getItemsOfUser(@PathVariable String username) {
        List<ShoppingBasket> baskets = shoppingBasket.findAll();

        List<ItemAndCount> items = new LinkedList<>();
        for (ShoppingBasket basket : baskets) {
            if (basket.getUser().equals(username)) {
                items.add(new ItemAndCount(basket.getName(), basket.getCount()));
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



}
