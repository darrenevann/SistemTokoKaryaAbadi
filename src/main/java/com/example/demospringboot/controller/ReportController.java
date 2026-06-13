package com.example.demospringboot.controller;

import com.example.demospringboot.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/daily")
    public String dailyReport(
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        if (date == null) {
            date = LocalDate.now();
        }

        Map<String, Object> reportData = dashboardService.getReportData(date);
        reportData.forEach(model::addAttribute);
        model.addAttribute("activePage", "reports");
        return "reports/daily";
    }
}
