package pl.kukulka.kukushop.web.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kukulka.kukushop.model.Order;
import pl.kukulka.kukushop.model.Product;
import pl.kukulka.kukushop.model.User;
import pl.kukulka.kukushop.repository.OrderRepository;
import pl.kukulka.kukushop.repository.ProductRepository;
import pl.kukulka.kukushop.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

@Controller
@SessionAttributes("basket")
@RequestMapping("/user")
public class BasketController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @ModelAttribute("role")
    public String currentUserRole(Principal principal) {
        if (principal != null) {

            return userRepository.findByLogin(principal.getName()).getRole().toString();
        }
        return "anonymous";
    }

    @ModelAttribute("login")
    public String currentLogin(Principal principal) {
        if (principal != null) {
            return principal.getName();
        }
        return "anonymous";
    }

    @ModelAttribute("basket")
    public Set<Product> addProductToBasket() {
        return new HashSet<>();
    }

    @GetMapping("/products/add/{id}")
    public String showAddProductToBasket(@SessionAttribute("basket") Set<Product> basket,
                                         @PathVariable("id") Long productId) {

        basket.add(productRepository.findOne(productId));

        return "redirect:/user/products";
    }


    @GetMapping("/basket")
    public String showListFromBasket() {
        return "user/basket";
    }

    @GetMapping("/basket/delete/{id}")
    public String deleteProductFromBasket(@SessionAttribute("basket") Set<Product> basket, @PathVariable("id") Long productId) {
        basket.remove(productRepository.findOne(productId));
        return "redirect:/user/basket";
    }

    @ModelAttribute("products")
    public List<Product> getAllProductsInf() {
        return productRepository.findAll();
    }

    @GetMapping("/products")
    public String showAllProducts() {
        return "user/products";
    }


    @GetMapping("/orderForm")
    public String addOrder(Map<String, Object> model) {
        model.put("order", new Order());
        return "user/orderForm";
    }

    @PostMapping("/orderForm")
    public String showAddOrder(@Valid @ModelAttribute("order") Order order, BindingResult result,
                               @SessionAttribute("basket") Set<Product> basket,
                               Principal principal) {
        if (result.hasErrors()) {

            return "user/orderForm";
        }

        final User user = userRepository.findByLogin(principal.getName());
        order.setUser(user);
        order.setProducts(new ArrayList<>(basket));
        for (Product product : new ArrayList<>(basket)) {
            product.setAvailable(false);
            productRepository.save(product);
        }
        basket.clear();

        orderRepository.save(order);
        return "redirect:/user/userOrder";
    }

}

