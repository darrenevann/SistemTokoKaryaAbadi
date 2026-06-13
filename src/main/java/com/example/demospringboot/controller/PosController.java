package com.example.demospringboot.controller;

import com.example.demospringboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pos")
public class PosController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String posPage(Model model) {
        model.addAttribute("products", productService.getAllActiveProducts());
        model.addAttribute("activePage", "pos");
        return "pos/index";
    }
}
