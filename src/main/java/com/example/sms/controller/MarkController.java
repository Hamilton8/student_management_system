package com.example.sms.controller;

import com.example.sms.dto.MarkForm;
import com.example.sms.service.MarkService;
import com.example.sms.service.StudentService;
import com.example.sms.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/marks")
@RequiredArgsConstructor
public class MarkController {

    private final MarkService markService;
    private final StudentService studentService;
    private final SubjectService subjectService;

    /** Marks entry page. ?studentId=1 selects a student; &subjectId=2 pre-fills an existing mark for editing. */
    @GetMapping
    public String entry(@RequestParam(required = false) Long studentId,
                        @RequestParam(required = false) Long subjectId, Model model) {
        MarkForm form = new MarkForm();
        form.setStudentId(studentId);
        form.setSubjectId(subjectId);
        if (studentId != null && subjectId != null) {
            markService.find(studentId, subjectId).ifPresent(m -> form.setMarksObtained(m.getMarksObtained()));
        }
        model.addAttribute("markForm", form);
        populate(model, studentId);
        return "marks/entry";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("markForm") MarkForm form, BindingResult result,
                       Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            populate(model, form.getStudentId());
            return "marks/entry";
        }
        try {
            markService.save(form);
            ra.addFlashAttribute("success", "Marks saved.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/marks?studentId=" + form.getStudentId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long studentId, RedirectAttributes ra) {
        markService.delete(id);
        ra.addFlashAttribute("success", "Mark removed.");
        return "redirect:/marks?studentId=" + studentId;
    }

    private void populate(Model model, Long studentId) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("subjects", subjectService.findAll());
        if (studentId != null) {
            model.addAttribute("selected", studentService.get(studentId));
            model.addAttribute("marks", markService.forStudent(studentId));
        }
    }
}
