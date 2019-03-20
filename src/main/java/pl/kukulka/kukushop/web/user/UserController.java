package pl.kukulka.kukushop.web.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.kukulka.kukushop.model.User;
import pl.kukulka.kukushop.repository.UserRepository;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

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


    @GetMapping("/edit")
    public String editUser(Map<String, Object> model) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();
        User user = userRepository.findByLogin(name);
        model.put("user", user);

        return "user/edit";
    }

    @PostMapping("/edit")
    public String showEditUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result, Errors errors,
                               Map<String, Object> model) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String name = authentication.getName();
        User user1 = userRepository.findByLogin(name);
        if (result.hasErrors()) {
            model.put("roles", User.Role.values());

            return "user/edit";
        }

        if ((userRepository.findByEmail(user.getEmail()) != null)
                && (!userRepository.findByEmail(user.getEmail()).equals(user1))) {

            errors.rejectValue("email", "pl.SznaKuKZie.project.user.Unique.message");
            model.put("roles", User.Role.values());
            return "user/edit";
        }

        user.setRole(userRepository.findByLogin(name).getRole());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/user/products";
    }


}
