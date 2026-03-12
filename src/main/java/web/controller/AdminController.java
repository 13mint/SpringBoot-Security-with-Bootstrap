package web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.AppUser;
import web.repository.RoleRepository;
import web.service.RoleService;
import web.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {

        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/newUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        model.addAttribute("roles", roleService.findAll());
        return "addUser";
    }

    @PostMapping("/newUser")
    public String createUser(@Valid @ModelAttribute("user") AppUser user, BindingResult bindingResult, Model model){
        if (userService.findByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "", "Username already exists");
        }

        if (userService.findByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Email already exists");
        }

        if(bindingResult.hasErrors()){
            model.addAttribute("roles", roleService.findAll());
            return "addUser";
        }

        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(Model model, @PathVariable Long id) {
        model.addAttribute("user", userService.findById(id));
        model.addAttribute("roles", roleService.findAll());
        return "editUser";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid @ModelAttribute("user") AppUser user, BindingResult  bindingResult, Model model) {
        AppUser existingUser = userService.findById(user.getId()).orElseThrow();

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            bindingResult.rejectValue("roles", "error.roles", "User must have at least one role");
        }

        if (!existingUser.getUsername().equals(user.getUsername())
                && userService.findByUsername(user.getUsername())) {

            bindingResult.rejectValue("username", "", "Username already exists");
        }

        if (!existingUser.getEmail().equals(user.getEmail())
                && userService.findByEmail(user.getEmail())) {

            bindingResult.rejectValue("email", "", "Email already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            return "editUser";
        }
        userService.update(user);
        return "redirect:/admin";
    }
}
