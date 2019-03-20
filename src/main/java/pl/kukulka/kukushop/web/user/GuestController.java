package pl.kukulka.kukushop.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.kukulka.kukushop.model.User;
import pl.kukulka.kukushop.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
public class GuestController {
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

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @GetMapping("/registry")
    public String registryUser(Map<String, Object> model) {
        model.put("user", new User());
        model.put("roles", User.Role.values());
        return "guest/registry";
    }

    @PostMapping("/registry")
    public String showRegistryUser(@Valid @ModelAttribute("user") User user, BindingResult result, Errors errors, Map<String, Object> model) {
        if (result.hasErrors()) {
            model.put("roles", User.Role.values());

            return "guest/registry";
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {

            errors.rejectValue("email", "pl.SznaKuKZie.project.user.Unique.message");
            model.put("roles", User.Role.values());

            return "guest/registry";
        }
        if (userRepository.findByLogin(user.getLogin()) != null) {

            errors.rejectValue("login", "pl.SznaKuKZie.project.user.Unique.message");
            model.put("roles", User.Role.values());

            return "guest/registry";
        }

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

}
