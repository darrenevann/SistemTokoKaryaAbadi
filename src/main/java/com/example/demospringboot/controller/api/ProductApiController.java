package com.example.demospringboot.controller.api;

import com.example.demospringboot.entity.Product;
import com.example.demospringboot.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    @Autowired
    private ProductService productService;

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<Map<String, Object>> getByBarcode(@PathVariable String barcode) {
        Map<String, Object> response = new HashMap<>();
        return productService.findByBarcode(barcode).map(product -> {
            if (!product.getIsActive()) {
                response.put("success", false);
                response.put("message", "Produk tidak aktif");
                return ResponseEntity.ok(response);
            }
            if (product.getStock() <= 0) {
                response.put("success", false);
                response.put("message", "Stok produk habis");
                return ResponseEntity.ok(response);
            }
            response.put("success", true);
            response.put("id", product.getId());
            response.put("barcode", product.getBarcode());
            response.put("name", product.getName());
            response.put("sellPrice", product.getSellPrice());
            response.put("buyPrice", product.getBuyPrice());
            response.put("stock", product.getStock());
            response.put("unit", product.getUnit());
            response.put("category", product.getCategory().getName());
            return ResponseEntity.ok(response);
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "Produk dengan barcode " + barcode + " tidak ditemukan");
            return ResponseEntity.ok(response);
        });
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        return ResponseEntity.ok(productService.searchProducts(keyword));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }
}
