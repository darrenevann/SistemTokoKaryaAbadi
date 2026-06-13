package com.example.demospringboot.controller;

import com.example.demospringboot.entity.Category;
import com.example.demospringboot.entity.Product;
import com.example.demospringboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(value = "keyword", required = false) String keyword) {
        List<Product> products;
        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProducts(keyword);
        } else {
            products = productService.getAllActiveProducts();
        }
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activePage", "products");
        return "products/index";
    }

    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("activePage", "products");
        return "products/form";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, RedirectAttributes redirectAttributes) {
        try {
            productService.createProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil ditambahkan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.findById(id).map(product -> {
            model.addAttribute("product", product);
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("activePage", "products");
            return "products/form";
        }).orElseGet(() -> {
            redirectAttributes.addFlashAttribute("errorMessage", "Produk tidak ditemukan");
            return "redirect:/products";
        });
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product,
                                RedirectAttributes redirectAttributes) {
        try {
            product.setId(id);
            productService.updateProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil diupdate!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produk berhasil dihapus!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    @PostMapping("/update-price/{id}")
    public String updatePrice(@PathVariable Long id,
                              @RequestParam BigDecimal newSellPrice,
                              @RequestParam(required = false) BigDecimal newBuyPrice,
                              @RequestParam(required = false) String reason,
                              Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        try {
            productService.updatePrice(id, newSellPrice, newBuyPrice, reason, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Harga berhasil diupdate!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    // Category management
    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("activePage", "products");
        return "products/categories";
    }

    @PostMapping("/categories/add")
    public String addCategory(@ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            productService.createCategory(category);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil ditambahkan!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products/categories";
    }

    @PostMapping("/categories/edit/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute Category category, RedirectAttributes redirectAttributes) {
        try {
            productService.updateCategory(id, category);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil diupdate!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("successMessage", "Kategori berhasil dihapus!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products/categories";
    }
}
