package pl.kukulka.kukushop.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kukulka.kukushop.model.Product;
import pl.kukulka.kukushop.repository.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/api")
public class RestGuestController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/home")
    public List<Product> listAllProducts() {
        return productRepository.findAllByAvailable(true);
    }

}
