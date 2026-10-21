package com.example.sms.controller;

import com.example.sms.dto.StudentReport;
import com.example.sms.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /** Individual report card (printable). */
    @GetMapping("/student/{id}")
    public String studentReport(@PathVariable Long id, Model model) {
        model.addAttribute("report", reportService.studentReport(id));
        model.addAttribute("passPercent", ReportService.PASS_PERCENT);
        return "reports/student";
    }

    /** Ranked class / course summary. */
    @GetMapping("/class")
    public String classReport(@RequestParam(required = false) String course, Model model) {
        model.addAttribute("reports", reportService.classReport(course));
        model.addAttribute("courses", reportService.courses());
        model.addAttribute("course", course);
        return "reports/class";
    }

    /** CSV download of the class report. */
    @GetMapping("/class/export")
    public void exportCsv(@RequestParam(required = false) String course, HttpServletResponse response) throws IOException {
        List<StudentReport> reports = reportService.classReport(course);
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"class-report.csv\"");
        PrintWriter w = response.getWriter();
        w.println("Rank,Roll Number,Name,Course,Total,Max Total,Percentage,Grade,Result");
        for (StudentReport r : reports) {
            w.println(String.join(",",
                    String.valueOf(r.getRank()),
                    csv(r.getStudent().getRollNumber()),
                    csv(r.getStudent().getFullName()),
                    csv(r.getStudent().getCourse()),
                    String.format(Locale.ROOT, "%.2f", r.getTotal()),
                    String.format(Locale.ROOT, "%.2f", r.getMaxTotal()),
                    String.format(Locale.ROOT, "%.2f", r.getPercentage()),
                    r.getGrade(),
                    r.getResult()));
        }
        w.flush();
    }

    private static String csv(String v) {
        return "\"" + (v == null ? "" : v.replace("\"", "\"\"")) + "\"";
    }
}
