package com.example.demospringboot.controller;

import com.example.demospringboot.service.StockService;
import com.example.demospringboot.service.ProductService;
import com.example.demospringboot.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @GetMapping
    public String listStockIn(Model model) {
        model.addAttribute("stockIns", stockService.getAllStockIn());
        model.addAttribute("products", productService.getAllActiveProducts());
        model.addAttribute("suppliers", supplierService.getAllActiveSuppliers());
        model.addAttribute("activePage", "stock");
        return "stock/index";
    }

    @GetMapping("/receive")
    public String receiveStockForm(Model model) {
        model.addAttribute("products", productService.getAllActiveProducts());
        model.addAttribute("suppliers", supplierService.getAllActiveSuppliers());
        model.addAttribute("activePage", "stock");
        return "stock/receive";
    }
}
