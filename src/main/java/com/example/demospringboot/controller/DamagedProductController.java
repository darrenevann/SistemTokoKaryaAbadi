package com.example.demospringboot.controller;

import com.example.demospringboot.service.DamagedProductService;
import com.example.demospringboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/damaged")
public class DamagedProductController {

    @Autowired
    private DamagedProductService damagedProductService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listDamagedProducts(Model model) {
        model.addAttribute("damagedProducts", damagedProductService.getAllDamagedProducts());
        model.addAttribute("products", productService.getAllActiveProducts());
        model.addAttribute("activePage", "damaged");
        return "damaged/index";
    }

    @PostMapping("/report")
    public String reportDamaged(
            @RequestParam Long productId,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportedAt,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            damagedProductService.reportDamagedProduct(productId, quantity, reason, description,
                    reportedAt, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Laporan barang rusak berhasil disimpan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/damaged";
    }
}
