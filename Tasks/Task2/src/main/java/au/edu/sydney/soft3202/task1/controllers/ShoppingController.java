package au.edu.sydney.soft3202.task1.controllers;

import au.edu.sydney.soft3202.task1.model.ShoppingBasket;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.Banner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.security.SecureRandom;
import java.util.*;
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
    public ModelAndView login(@RequestParam(value = "user", defaultValue = "") String user,
                              RedirectAttributes redirectAttributes,
                              HttpServletResponse response
    ) {

        // We are just checking the username, in the real world you would also check their password here
        // or authenticate the user some other way.
        if (!Arrays.asList(users).contains(user)) {
            redirectAttributes.addFlashAttribute("e", "Invalid User.");
            return new ModelAndView("redirect:/invalid");
        }

        // Generate the session token.
        byte[] sessionTokenBytes = new byte[16];
        randomNumberGenerator.nextBytes(sessionTokenBytes);
        String sessionToken = hexFormatter.formatHex(sessionTokenBytes);

        // Store the association of the session token with the user.
        sessions.put(sessionToken, user);

        // Create HTTP headers including the instruction for the browser to store the session token in a cookie.
        Cookie sessionCookie = new Cookie("session", sessionToken);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        response.addCookie(sessionCookie);

        // Redirect to the cart page, with the session-cookie-setting headers.
        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/update-cart-count")
    public ModelAndView updateMap(@RequestParam Map<String, String> values,
                            @CookieValue(value = "session", defaultValue = "") String sessionToken,
                            RedirectAttributes redirectAttributes) {
        ShoppingBasket cart = ShoppingBasket.getInstance(sessions.get(sessionToken));
        // Iterate over the values map and update the corresponding entries in myMap
        for (String key : values.keySet()) {
            Integer value = Integer.valueOf(values.get(key));
            try {
                cart.updateItemCount(key, value);
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("e", e);
                return new ModelAndView("redirect:/invalid");
            }
        }

        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/insert-new-item")
    public ModelAndView insertNewItem(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            RedirectAttributes redirectAttributes,
            @RequestParam (value = "new-name") String newName,
            @RequestParam (value = "new-cost") String newCount
    ) {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken));
        try {
            shoppingBasket.insertNewItem(newName, Double.valueOf(newCount));
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("e", e);
            return new ModelAndView("redirect:/invalid");
        }

        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/delete_item")
    public ModelAndView deleteItem(
            @CookieValue (value = "session", defaultValue = "") String sessionToken,
            @RequestParam Map<String, String> values
    )
    {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken));

        for (String key : values.keySet()) {
            if (Objects.equals(values.get(key), "false")) {
                shoppingBasket.deleteExistingItem(key);
            }
        }
        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/update_items")
    public ModelAndView updateItems(
            @CookieValue (value = "session", defaultValue = "") String sessionToken,
            @RequestParam Map<String, String> values
    )
    {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken));

        Map<String, String> updatedNameMap = new HashMap<>();
        for (String inputName : values.keySet()) {
            if (inputName.endsWith("_cost")) {
//                Deal with cost changing
                String item = inputName.split("_")[0];
//                Need to have most up to date name because name could change
                String upToDateName = updatedNameMap.get(item);
                Double cost = Double.valueOf(values.get(inputName));
                shoppingBasket.updateCost(upToDateName, cost);

            } else {
//                Deal with name changing
                String newName = values.get(inputName);
                updatedNameMap.put(inputName, newName);
                shoppingBasket.updateName(inputName, newName);
            }

        }
        return new ModelAndView("redirect:/cart");
    }


    @GetMapping("/cart")
    public ModelAndView cart(@CookieValue(value = "session", defaultValue = "") String sessionToken) {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken));

        ModelAndView mav = new ModelAndView("cart");
        mav.addObject("items", shoppingBasket.getItems());

        return mav;
    }

    @GetMapping("/")
    public ModelAndView accessSite(
            @CookieValue(value = "session", defaultValue = "") String sessionToken
    ) {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("login");
        }
        return new ModelAndView("redirect:/cart");
    }

    @GetMapping("/logout")
    public ModelAndView logout(
            @CookieValue(value = "session", defaultValue = "") String sessionToken
    ) {
        sessions.remove(sessionToken);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/newname")
    public ModelAndView newName(
            @CookieValue(value = "session", defaultValue = "") String sessionToken) {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }
        return new ModelAndView("newname");
    }

    @GetMapping("/delname")
    public ModelAndView delName(
            @CookieValue(value = "session", defaultValue = "") String sessionToken
    )
    {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        ModelAndView mav = new ModelAndView("delname");
        mav.addObject("items", ShoppingBasket.getInstance(sessions.get(sessionToken)).getItemsAndValues());
        return mav;
    }

    @GetMapping("/updatename")
    public ModelAndView updateName(
            @CookieValue(value = "session", defaultValue = "") String sessionToken
    )
    {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }
        ModelAndView mav = new ModelAndView("updatename");
        mav.addObject("items", ShoppingBasket.getInstance(sessions.get(sessionToken)).getItemsAndValues());
        return mav;
    }


    @GetMapping("/unauthorized")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView unauthorized() {
        return new ModelAndView("unauthorized");
    }

    @GetMapping("/invalid")
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView invalid() {
        return new ModelAndView("invalid");
    }

}
