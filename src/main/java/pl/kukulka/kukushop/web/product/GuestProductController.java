package pl.kukulka.kukushop.web.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kukulka.kukushop.model.Product;
import pl.kukulka.kukushop.repository.ProductRepository;
import pl.kukulka.kukushop.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class GuestProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @ModelAttribute("role")
    public String currentUserRole(Principal principal) {
        if (principal != null) {

            String role = userRepository.findByLogin(principal.getName()).getRole().toString();
            return role;
        }
        return "anonymous";
    }

    @GetMapping("/home")
    public String showList(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                           Map<String, Object> model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<Product> productsPage = productRepository.findAllByAvailableTrue(new PageRequest(currentPage, pageSize));
        model.put("products", productsPage);
        model.put("count", productRepository.findAllByAvailable(true).size());
        int totalPages = productsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }
        return "guest/home";
    }

    @GetMapping("/home/table")
    public String showTable(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                            Map<String, Object> model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<Product> productsPage = productRepository.findAllByAvailableTrue(new PageRequest(currentPage, pageSize));
        model.put("products", productsPage);
        int totalPages = productsPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }
        return "guest/home :: productsTable";
    }


}
