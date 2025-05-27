package com.emsi.cc_spring_boot.controller;

import com.emsi.cc_spring_boot.entity.Etudiant;
import com.emsi.cc_spring_boot.service.EtudiantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/etudiants")
public class EtudiantController {

    @Autowired
    private EtudiantService etudiantService;

    // Liste des étudiants avec recherche
    @GetMapping
    public String listEtudiants(@RequestParam(value = "nom", required = false) String nom,
                                @RequestParam(value = "prenom", required = false) String prenom,
                                @RequestParam(value = "email", required = false) String email,
                                Model model) {

        List<Etudiant> etudiants;

        // Si des critères de recherche sont fournis
        if ((nom != null && !nom.trim().isEmpty()) ||
                (prenom != null && !prenom.trim().isEmpty()) ||
                (email != null && !email.trim().isEmpty())) {
            etudiants = etudiantService.searchEtudiants(nom, prenom, email);
            model.addAttribute("searchPerformed", true);
        } else {
            etudiants = etudiantService.getAllEtudiants();
            model.addAttribute("searchPerformed", false);
        }

        model.addAttribute("etudiants", etudiants);
        model.addAttribute("nom", nom);
        model.addAttribute("prenom", prenom);
        model.addAttribute("email", email);

        return "etudiants/list";
    }

    // Afficher le formulaire d'ajout
    @GetMapping("/nouveau")
    public String showAddForm(Model model) {
        model.addAttribute("etudiant", new Etudiant());
        model.addAttribute("action", "Ajouter");
        return "etudiants/form";
    }

    // Traiter l'ajout d'un étudiant
    @PostMapping("/nouveau")
    public String addEtudiant(@Valid @ModelAttribute Etudiant etudiant,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        // Vérification des erreurs de validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "Ajouter");
            return "etudiants/form";
        }

        // Vérifier si l'email existe déjà
        if (etudiantService.emailExists(etudiant.getEmail())) {
            bindingResult.rejectValue("email", "error.etudiant", "Cet email est déjà utilisé");
            model.addAttribute("action", "Ajouter");
            return "etudiants/form";
        }

        // Sauvegarder l'étudiant
        Etudiant savedEtudiant = etudiantService.saveEtudiant(etudiant);
        if (savedEtudiant != null) {
            redirectAttributes.addFlashAttribute("success",
                    "L'étudiant " + etudiant.getNomComplet() + " a été ajouté avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de l'ajout de l'étudiant");
        }

        return "redirect:/etudiants";
    }

    // Afficher le formulaire de modification
    @GetMapping("/modifier/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Étudiant non trouvé");
            return "redirect:/etudiants";
        }

        model.addAttribute("etudiant", etudiant);
        model.addAttribute("action", "Modifier");
        return "etudiants/form";
    }

    // Traiter la modification d'un étudiant
    @PostMapping("/modifier/{id}")
    public String updateEtudiant(@PathVariable Long id,
                                 @Valid @ModelAttribute Etudiant etudiant,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        // Vérification des erreurs de validation
        if (bindingResult.hasErrors()) {
            model.addAttribute("action", "Modifier");
            return "etudiants/form";
        }

        // Vérifier si l'email existe déjà (en excluant l'étudiant current)
        if (etudiantService.emailExistsExcept(etudiant.getEmail(), id)) {
            bindingResult.rejectValue("email", "error.etudiant", "Cet email est déjà utilisé");
            model.addAttribute("action", "Modifier");
            return "etudiants/form";
        }

        // S'assurer que l'ID est défini
        etudiant.setId(id);

        // Sauvegarder les modifications
        Etudiant savedEtudiant = etudiantService.saveEtudiant(etudiant);
        if (savedEtudiant != null) {
            redirectAttributes.addFlashAttribute("success",
                    "L'étudiant " + etudiant.getNomComplet() + " a été modifié avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la modification de l'étudiant");
        }

        return "redirect:/etudiants";
    }

    // Afficher les détails d'un étudiant
    @GetMapping("/voir/{id}")
    public String viewEtudiant(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Étudiant non trouvé");
            return "redirect:/etudiants";
        }

        model.addAttribute("etudiant", etudiant);
        return "etudiants/view";
    }

    // Supprimer un étudiant
    @PostMapping("/supprimer/{id}")
    public String deleteEtudiant(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Etudiant etudiant = etudiantService.getEtudiantById(id);
        if (etudiant == null) {
            redirectAttributes.addFlashAttribute("error", "Étudiant non trouvé");
            return "redirect:/etudiants";
        }

        String nomComplet = etudiant.getNomComplet();

        if (etudiantService.deleteEtudiant(id)) {
            redirectAttributes.addFlashAttribute("success",
                    "L'étudiant " + nomComplet + " a été supprimé avec succès");
        } else {
            redirectAttributes.addFlashAttribute("error",
                    "Erreur lors de la suppression de l'étudiant");
        }

        return "redirect:/etudiants";
    }

    // API endpoint pour vérifier l'unicité de l'email (AJAX)
    @GetMapping("/check-email")
    @ResponseBody
    public boolean checkEmailAvailability(@RequestParam String email,
                                          @RequestParam(required = false) Long excludeId) {
        if (excludeId != null) {
            return !etudiantService.emailExistsExcept(email, excludeId);
        }
        return !etudiantService.emailExists(email);
    }
}
