package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Supplier;
import com.example.demospringboot.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String listSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        model.addAttribute("activePage", "suppliers");
        return "suppliers/index";
    }

    @PostMapping("/add")
    public String addSupplier(@ModelAttribute Supplier supplier, RedirectAttributes redirectAttributes) {
        try {
            supplierService.createSupplier(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier berhasil ditambahkan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/edit/{id}")
    public String updateSupplier(@PathVariable Long id, @ModelAttribute Supplier supplier,
                                 RedirectAttributes redirectAttributes) {
        try {
            supplier.setId(id);
            supplierService.updateSupplier(supplier);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier berhasil diupdate!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/suppliers";
    }

    @PostMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supplierService.deleteSupplier(id);
            redirectAttributes.addFlashAttribute("successMessage", "Supplier berhasil dihapus!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/suppliers";
    }
}
