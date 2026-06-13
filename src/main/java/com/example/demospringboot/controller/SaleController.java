package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Sale;
import com.example.demospringboot.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping
    public String listSales(Model model,
                            @RequestParam(value = "date", required = false) String date) {
        List<Sale> sales;
        if (date != null && !date.isEmpty()) {
            LocalDate localDate = LocalDate.parse(date);
            sales = saleService.getSalesByDateRange(
                    localDate.atStartOfDay(),
                    localDate.atTime(23, 59, 59)
            );
            model.addAttribute("selectedDate", date);
        } else {
            sales = saleService.getTodaySales();
            model.addAttribute("selectedDate", LocalDate.now().toString());
        }
        model.addAttribute("sales", sales);
        model.addAttribute("activePage", "sales");
        return "sales/index";
    }

    @GetMapping("/receipt/{id}")
    public String viewReceipt(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return saleService.findById(id).map(sale -> {
            model.addAttribute("sale", sale);
            model.addAttribute("storeName", "Toko Karya Abadi");
            model.addAttribute("storeAddress", "Jl. Contoh No. 123, Kota");
            model.addAttribute("storePhone", "08123456789");
            return "sales/receipt";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Transaksi tidak ditemukan");
            return "redirect:/sales";
        });
    }
}
