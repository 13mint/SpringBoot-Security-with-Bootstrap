package web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.AppUser;
import web.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin";
    }

    @GetMapping("/newUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new AppUser());
        return "addUser";
    }

    @PostMapping()
    public String createUser(@Valid @ModelAttribute("user") AppUser user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "addUser";
        }

        if (userService.findByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "", "Username already exists");
        }

        if (userService.findByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "", "Email already exists");
        }

        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/newUser")
    public String saveUser(@ModelAttribute AppUser user) {
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
        return "editUser";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute AppUser user) {
        userService.save(user);
        return "redirect:/admin";
    }
}
