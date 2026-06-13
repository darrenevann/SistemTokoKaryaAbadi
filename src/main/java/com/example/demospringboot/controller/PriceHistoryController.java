package com.example.demospringboot.controller;

import com.example.demospringboot.service.PriceHistoryService;
import com.example.demospringboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/price-history")
public class PriceHistoryController {

    @Autowired
    private PriceHistoryService priceHistoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listPriceHistory(Model model) {
        model.addAttribute("priceHistories", priceHistoryService.getAllPriceHistory());
        model.addAttribute("products", productService.getAllActiveProducts());
        model.addAttribute("activePage", "price-history");
        return "price-history/index";
    }

    @GetMapping("/product/{productId}")
    public String productPriceHistory(@PathVariable Long productId, Model model) {
        model.addAttribute("priceHistories", priceHistoryService.getPriceHistoryByProduct(productId));
        model.addAttribute("product", productService.findById(productId).orElse(null));
        model.addAttribute("activePage", "price-history");
        return "price-history/product";
    }
}
