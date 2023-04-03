package au.edu.sydney.soft3202.task1.controllers;

import au.edu.sydney.soft3202.task1.model.Cart;
import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ShoppingController {
    private final SecureRandom randomNumberGenerator = new SecureRandom();
    private final HexFormat hexFormatter = HexFormat.of();

    private final AtomicLong counter = new AtomicLong();
//    ShoppingBasket shoppingBasket = new ShoppingBasket();

    Map<String, String> sessions = new HashMap<>();

    String[] users = {"A", "B", "C", "D"};

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam(value = "user", defaultValue = "") String user) {

        // We are just checking the username, in the real world you would also check their password here
        // or authenticate the user some other way.
        if (!Arrays.asList(users).contains(user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user.\n");
        }

        // Generate the session token.
        byte[] sessionTokenBytes = new byte[16];
        randomNumberGenerator.nextBytes(sessionTokenBytes);
        String sessionToken = hexFormatter.formatHex(sessionTokenBytes);

        // Store the association of the session token with the user.
        sessions.put(sessionToken, user);

        // Create HTTP headers including the instruction for the browser to store the session token in a cookie.
        String setCookieHeaderValue = String.format("session=%s; Path=/; HttpOnly; SameSite=Strict;", sessionToken);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", setCookieHeaderValue);

        // Redirect to the cart page, with the session-cookie-setting headers.
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).location(URI.create("/cart")).build();
    }

    @PostMapping("/update-cart-count")
    public String updateMap(@RequestParam Map<String, String> values,
                            @CookieValue(value = "session", defaultValue = "") String sessionToken,
                            Model model) {
        ShoppingBasket cart = ShoppingBasket.getInstance(sessions.get(sessionToken));
        // Iterate over the values map and update the corresponding entries in myMap
        for (String key : values.keySet()) {
            Integer value = Integer.valueOf(values.get(key));
            try {
                cart.updateItemCount(key, value);
            } catch (IllegalArgumentException e) {
                model.addAttribute("e", e);
                return "invalid";
            }
        }
//        // Add the updated myMap to the model and return the view
        model.addAttribute("items", cart.getItems());

        return "cart";
    }

    @PostMapping("/insert-new-item")
    public String insertNewItem(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            Model model,
            @RequestParam (value = "new-name") String newName,
            @RequestParam (value = "new-cost") String newCount
    ) {
        if (!sessions.containsKey(sessionToken)) {
            return "unauthorized";
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken));
        shoppingBasket.insertNewItem(newName, Double.valueOf(newCount));
        return "newname";
    }


    @GetMapping("/cart")
    public String cart(@CookieValue(value = "session", defaultValue = "") String sessionToken, Model model) {
        if (!sessions.containsKey(sessionToken)) {
            return "unauthorized";
        }

        Cart cart = Cart.getCart(sessions.get(sessionToken));

        model.addAttribute("items", cart.getItems());

        return "cart";
    }

    @GetMapping("/counter")
    public ResponseEntity<String> counter() {
        counter.incrementAndGet();
        return ResponseEntity.status(HttpStatus.OK).body("[" + counter + "]");
    }

//    @GetMapping("/cost")
//    public ResponseEntity<String> cost() {
//        return ResponseEntity.status(HttpStatus.OK).body(
//                shoppingBasket.getValue() == null ? "0" : shoppingBasket.getValue().toString()
//        );
//    }

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model
    ) {
        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String accessSite(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model
    ) {
        if (!sessions.containsKey(sessionToken)) {
            return "login";
        }
        return cart(sessionToken, model);
    }

    @GetMapping("/logout")
    public String logout(
            @CookieValue(value = "session", defaultValue = "") String sessionToken
    ) {
        sessions.remove(sessionToken);
        return "login";
    }

    @GetMapping("/newname")
    public String newName(
            @CookieValue(value = "session", defaultValue = "") String sessionToken) {
        if (!sessions.containsKey(sessionToken)) {
            return "unauthorized";
        }
        return "newname";
    }


    @GetMapping("/unauthorized")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String unauthorized() {
        return "unauthorized";
    }

}
