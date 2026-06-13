package com.example.demospringboot.controller;

import com.example.demospringboot.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping({ "/", "/dashboard" })
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model,
            @RequestParam(value = "filter", defaultValue = "month") String filter) {
        Map<String, Object> data = dashboardService.getDashboardData(filter);
        data.forEach(model::addAttribute);
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("currentFilter", filter);
        return "dashboard/index";
    }
}
