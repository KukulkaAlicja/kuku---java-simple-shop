package pl.kukulka.kukushop.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.kukulka.kukushop.model.User;
import pl.kukulka.kukushop.repository.OrderRepository;
import pl.kukulka.kukushop.repository.ProductRepository;
import pl.kukulka.kukushop.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

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


    @ModelAttribute("admin")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("/admin")
    public String showUsers(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                            Map<String, Object> model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<User> usersPage = userRepository.findAll(new PageRequest(currentPage, pageSize));
        model.put("admin", usersPage);
        model.put("countUser", userRepository.count());
        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }
        return "admin/list";
    }

    @GetMapping("/admin/table")
    public String showUsersTable(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                                 Map<String, Object> model) {
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(5);
        Page<User> usersPage = userRepository.findAll(new PageRequest(currentPage, pageSize));
        model.put("admin", usersPage);
        int totalPages = usersPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.range(0, totalPages).boxed().collect(Collectors.toList());
            model.put("pageNumbers", pageNumbers);
        }
        return "admin/list :: usersTable";
    }


    @GetMapping("/admin/add")
    public String addUser(Map<String, Object> model) {
        model.put("user", new User());
        model.put("roles", User.Role.values());
        return "admin/add";
    }

    @PostMapping("/admin/add")
    public String showAddUser(@Valid @ModelAttribute("user") User user,
                              BindingResult result, Errors errors,
                              Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("roles", User.Role.values());
            return "admin/add";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {

            errors.rejectValue("email", "pl.SznaKuKZie.project.user.Unique.message");
            model.put("roles", User.Role.values());
            return "admin/add";
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {

            errors.rejectValue("login", "pl.SznaKuKZie.project.user.Unique.message");
            model.put("roles", User.Role.values());
            return "admin/add";
        }


        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(Map<String, Object> model, @PathVariable("id") long userId) {
        User user = userRepository.findByUserId(userId);
        model.put("user", user);
        model.put("roles", User.Role.values());
        return "admin/edit";
    }

    @PostMapping("/admin/edit/{id}")
    public String showEditUser(@ModelAttribute("user") User user, @PathVariable("id") long userId) {
        userRepository.findByUserId(userId);
        userRepository.save(user);
        return "redirect:/admin";
    }
    
    @GetMapping("/admin/delete")
    public String DeleteUser(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                             @RequestParam("admin") Long userId, Map<String, Object> model) {
        userRepository.delete(userId);
        return showUsersTable(page, size, model);
    }


    @GetMapping("/admin/approved")
    public String approveUser(@RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size,
                              @RequestParam("admin") Long userId, Map<String, Object> model) {
        User user = userRepository.findByUserId(userId);
        user.setApproved(true);
        userRepository.save(user);
        return showUsersTable(page, size, model);
    }


}
