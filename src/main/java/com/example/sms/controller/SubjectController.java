package com.example.sms.controller;

import com.example.sms.model.Subject;
import com.example.sms.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("subjects", service.findAll());
        model.addAttribute("subject", new Subject());
        return "subjects/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("subjects", service.findAll());
        model.addAttribute("subject", service.get(id));
        return "subjects/list";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("subject") Subject subject, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (!result.hasFieldErrors("code") && service.codeTaken(subject)) {
            result.rejectValue("code", "duplicate", "This subject code already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("subjects", service.findAll());
            return "subjects/list";
        }
        service.save(subject);
        ra.addFlashAttribute("success", "Subject saved.");
        return "redirect:/subjects";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("success", "Subject deleted.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/subjects";
    }
}
