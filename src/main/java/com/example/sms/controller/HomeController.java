package com.example.sms.controller;

import com.example.sms.service.MarkService;
import com.example.sms.service.StudentService;
import com.example.sms.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final StudentService studentService;
    private final SubjectService subjectService;
    private final MarkService markService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("studentCount", studentService.count());
        model.addAttribute("subjectCount", subjectService.count());
        model.addAttribute("markCount", markService.count());
        return "index";
    }
}
