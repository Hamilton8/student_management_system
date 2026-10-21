package com.example.sms.controller;

import com.example.sms.model.Student;
import com.example.sms.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    @GetMapping
    public String list(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("students", service.findAll(keyword));
        model.addAttribute("keyword", keyword);
        return "students/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("title", "Add Student");
        return "students/form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("student", service.get(id));
        model.addAttribute("title", "Edit Student");
        return "students/form";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("student") Student student, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (!result.hasFieldErrors("rollNumber") && service.rollNumberTaken(student)) {
            result.rejectValue("rollNumber", "duplicate", "This roll number already exists");
        }
        if (result.hasErrors()) {
            model.addAttribute("title", student.getId() == null ? "Add Student" : "Edit Student");
            return "students/form";
        }
        service.save(student);
        ra.addFlashAttribute("success", "Student saved successfully.");
        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("success", "Student deleted.");
        return "redirect:/students";
    }
}
