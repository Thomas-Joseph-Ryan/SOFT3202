package au.sydney.soft3202.task1;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

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
}
