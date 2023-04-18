package au.edu.sydney.soft3202.task1.controllers;

import au.edu.sydney.soft3202.task1.DatabaseHelper;
import au.edu.sydney.soft3202.task1.model.Item;
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

import javax.sound.midi.SysexMessage;
import java.net.URI;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class ShoppingController {
    private final SecureRandom randomNumberGenerator = new SecureRandom();
    private final HexFormat hexFormatter = HexFormat.of();

    private final AtomicLong counter = new AtomicLong();
//    ShoppingBasket shoppingBasket = new ShoppingBasket();

    Map<String, String> sessions = new HashMap<>();

    DatabaseHelper dbHelper = null;

    @PostMapping("/login")
    public ModelAndView login(@RequestParam(value = "user", defaultValue = "") String user,
                              RedirectAttributes redirectAttributes,
                              HttpServletResponse response
    ) {

        // We are just checking the username, in the real world you would also check their password here
        // or authenticate the user some other way.
        try {
            dbHelper = new DatabaseHelper();
            if (!user.equals("Admin")) {
                user = dbHelper.getUser(user);
            }
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Unable to connect: " + e.getMessage() + ".\n");
            return new ModelAndView("redirect:/invalid");
        }

        if (user == null) {
            return new ModelAndView("redirect:/unauthorized");
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
        String user = sessions.get(sessionToken);
        ShoppingBasket cart = ShoppingBasket.getInstance(user);
        // Iterate over the values map and update the corresponding entries in myMap
        for (String key : values.keySet()) {
            Integer value = Integer.valueOf(values.get(key));
            try {
                cart.updateItemCount(key, value);
                dbHelper.updateCartItemCount(key, value, user);
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("e", e);
                return new ModelAndView("redirect:/invalid");
            } catch (SQLException e) {
                redirectAttributes.addFlashAttribute("e", "Error while updating cart counts: " + e.getMessage() + ".\n");
                return new ModelAndView("redirect:/invalid");
            }
        }


        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/insert-new-item")
    public ModelAndView insertNewItem(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "new-name") String newName,
            @RequestParam(value = "new-cost") String newCount
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
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            @RequestParam Map<String, String> values
    ) {
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
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            @RequestParam Map<String, String> values
    ) {
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

    @PostMapping("/insert-new-name")
    public ModelAndView addNewUser(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            @RequestParam(value = "username") String newUsername,
            RedirectAttributes redirectAttributes
    ) {
        if (!Objects.equals(sessions.get(sessionToken), "Admin")) {
            return new ModelAndView("redirect:/unauthorized");
        }
        List<String> users;
        try {
            users = dbHelper.getUsers();
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Unable to connect: " + e.getMessage() + ".\n");
            return new ModelAndView("redirect:/invalid");
        }

        if (users.contains(newUsername)) {
            redirectAttributes.addFlashAttribute("e", "Cannot add a username that already exists\n");
            return new ModelAndView("redirect:/invalid");
        }

        try {
            dbHelper.addUser(newUsername);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Problem when adding user: " + e.getMessage() + ".\n");
            return new ModelAndView("redirect:/invalid");
        }

        return new ModelAndView("redirect:/cart");
    }

    @PostMapping("/updateusers")
    public ModelAndView updateUsers(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            @RequestParam(value = "userToDelete") String[] usersToDelete,
            RedirectAttributes redirectAttributes
    ) {
        if (!Objects.equals(sessions.get(sessionToken), "Admin")) {
            return new ModelAndView("redirect:/unauthorized");
        }
        if (usersToDelete != null) {
            try {
                dbHelper.deleteUserList(usersToDelete);
            } catch (SQLException e) {
                redirectAttributes.addFlashAttribute("e", "Problem when deleting user: " + e.getMessage() + ".\n");
                return new ModelAndView("redirect:/invalid");
            }
        }
        return new ModelAndView("redirect:/cart");
    }


    @GetMapping("/cart")
    public ModelAndView cart(@CookieValue(value = "session", defaultValue = "") String sessionToken,
                             RedirectAttributes redirectAttributes)
    {
        if (!sessions.containsKey(sessionToken)) {
            return new ModelAndView("redirect:/unauthorized");
        }

        String user = sessions.get(sessionToken);
        if (user.equals("Admin")) {
            return new ModelAndView("redirect:/users");
        }

        List<Item> items;
        try {
            items = dbHelper.getUserCart(user);
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Problem when getting user cart: " + e.getMessage() + ".\n");
            return new ModelAndView("redirect:/invalid");
        }

        ShoppingBasket shoppingBasket = ShoppingBasket.getInstance(sessions.get(sessionToken), items);
        if (items.size() == 0) {
            for (Item item : shoppingBasket.getItemsAsItemList()) {
                try {
                    dbHelper.addItem(user, item);
                } catch (SQLException e) {
                    redirectAttributes.addFlashAttribute("e", "Problem when adding default items to user cart: " + e.getMessage() + ".\n");
                    return new ModelAndView("redirect:/invalid");
                }
            }
        }

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

    @GetMapping("/users")
    public ModelAndView users(
            @CookieValue(value = "session", defaultValue = "") String sessionToken,
            RedirectAttributes redirectAttributes

    ) {
        if (!Objects.equals(sessions.get(sessionToken), "Admin")) {
            return new ModelAndView("redirect:/unauthorized");
        }
        ModelAndView mav = new ModelAndView("users");
        List<String> users;
        try{
            users = dbHelper.getUsers();
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Unable to connect: " + e.getMessage()+ ".\n");
            return new ModelAndView("redirect:/invalid");
        }
        users.sort(Comparator.comparing(String::toString));
        mav.addObject("users", users);
        return mav;
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

    @GetMapping("/newuser")
    public ModelAndView newUsername(
        @CookieValue(value = "session", defaultValue = "") String sessionToken,
        RedirectAttributes redirectAttributes
    ) {
        if (!Objects.equals(sessions.get(sessionToken), "Admin")) {
            return new ModelAndView("redirect:/unauthorized");
        }
        List<String> users;
        try {
            users = dbHelper.getUsers();
        } catch (SQLException e) {
            redirectAttributes.addFlashAttribute("e", "Unable to connect: " + e.getMessage()+ ".\n");
            return new ModelAndView("redirect:/invalid");
        }
        ModelAndView mav = new ModelAndView("newuser");
        mav.addObject("users", users);
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
