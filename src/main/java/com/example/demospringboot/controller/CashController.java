package com.example.demospringboot.controller;

import com.example.demospringboot.service.CashReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
@RequestMapping("/cash")
public class CashController {

    @Autowired
    private CashReconciliationService cashReconciliationService;

    @GetMapping("/reconciliation")
    public String reconciliationPage(Model model) {
        model.addAttribute("reconciliations", cashReconciliationService.getAllReconciliations());
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("activePage", "cash");
        return "cash/reconciliation";
    }

    @PostMapping("/reconciliation")
    public String submitReconciliation(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam BigDecimal actualCash,
            @RequestParam(required = false) String notes,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            cashReconciliationService.createReconciliation(date, actualCash, notes, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Rekonsiliasi kas berhasil disimpan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cash/reconciliation";
    }
}
