package com.emsi.cc_spring_boot.controller;

import com.emsi.cc_spring_boot.service.EtudiantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EtudiantService etudiantService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("totalEtudiants", etudiantService.countEtudiants());
        model.addAttribute("recentEtudiants", etudiantService.getAllEtudiants().stream().limit(5).toList());
        return "home";
    }
}
