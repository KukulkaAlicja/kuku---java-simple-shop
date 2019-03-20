package pl.kukulka.kukushop.web.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.kukulka.kukushop.model.Product;
import pl.kukulka.kukushop.model.User;
import pl.kukulka.kukushop.repository.ProductRepository;
import pl.kukulka.kukushop.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserProductsController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor ste = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, ste);
    }

    @ModelAttribute("role")
    public String currentUserRole(Principal principal) {
        if (principal != null) {

            return userRepository.findByLogin(principal.getName()).getRole().toString();
        }
        return "anonymous";
    }

    @ModelAttribute("userProducts")
    public List<Product> getUserProducts(Principal principal) {
        return productRepository.findByUserLogin(principal.getName());
    }


    @GetMapping("/userProducts")
    public String showUserProducts() {
        return "user/userProducts";
    }

    @GetMapping("/userProducts/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId) {
        if (productRepository.findByProductId(productId).isAvailable()) {
            productRepository.delete(productId);
        }

        return "redirect:/user/userProducts";
    }

    @GetMapping("/userProducts/addProduct")
    public String addProduct(Map<String, Object> model2) {
        model2.put("product", new Product());
        return "user/addProduct";
    }

    @PostMapping("/userProducts/addProduct")
    public String showAddProduct(@Valid @ModelAttribute("product") Product product,
                                 BindingResult result, Errors errors,
                                 Principal principal) {
        if (result.hasErrors()) {

            return "user/addProduct";
        }
        final User user = userRepository.findByLogin(principal.getName());
        product.setUser(user);
        product.setDate(new Date());
        productRepository.save(product);
        return "redirect:/user/userProducts";
    }

    @GetMapping("/userProducts/edit/{id}")
    public String editProduct(Map<String, Object> model2, @PathVariable("id") long productId) {
        Product product = productRepository.findByProductId(productId);
        if (product.isAvailable()) {
            model2.put("product", product);
        }
        return "user/addProduct";
    }

    @PutMapping("/userProducts/edit/{id}")
    public String showEditProduct(@ModelAttribute("product") Product product, @PathVariable("id") long productId) {
        productRepository.findByProductId(productId);
        return "redirect:/user/userProducts";
    }

}

