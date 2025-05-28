package com.emsi.cc_spring_boot.controller;

import com.emsi.cc_spring_boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        }
        if (logout != null) {
            model.addAttribute("message", "Vous avez été déconnecté avec succès");
        }
        return "auth/login";
    }
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               RedirectAttributes redirectAttributes) {
        // Add your login processing logic here
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  RedirectAttributes redirectAttributes) {

        // Validation
        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Le nom d'utilisateur est obligatoire");
            return "redirect:/register";
        }

        if (password == null || password.length() < 6) {
            redirectAttributes.addFlashAttribute("error", "Le mot de passe doit contenir au moins 6 caractères");
            return "redirect:/register";
        }

        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Les mots de passe ne correspondent pas");
            return "redirect:/register";
        }

        if (userService.usernameExists(username)) {
            redirectAttributes.addFlashAttribute("error", "Ce nom d'utilisateur existe déjà");
            return "redirect:/register";
        }

        // Enregistrement
        if (userService.registerUser(username, password)) {
            redirectAttributes.addFlashAttribute("success", "Compte créé avec succès. Vous pouvez maintenant vous connecter.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création du compte");
            return "redirect:/register";
        }
    }
}

