package com.example.demospringboot.controller.api;

import com.example.demospringboot.entity.Sale;
import com.example.demospringboot.entity.SaleDetail;
import com.example.demospringboot.entity.Product;
import com.example.demospringboot.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sales")
public class SaleApiController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processSale(
            @RequestBody Map<String, Object> saleRequest,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        try {
            Sale sale = new Sale();

            // Parse request
            String paidAmountStr = saleRequest.get("paidAmount").toString();
            String discountStr = saleRequest.getOrDefault("discount", "0").toString();
            String paymentMethod = saleRequest.getOrDefault("paymentMethod", "CASH").toString();
            String notes = (String) saleRequest.getOrDefault("notes", "");

            sale.setPaidAmount(new BigDecimal(paidAmountStr));
            sale.setDiscount(new BigDecimal(discountStr));
            sale.setPaymentMethod(paymentMethod);
            sale.setNotes(notes);

            // Parse items
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) saleRequest.get("items");
            List<SaleDetail> details = new ArrayList<>();

            for (Map<String, Object> item : items) {
                SaleDetail detail = new SaleDetail();
                Product product = new Product();
                product.setId(Long.parseLong(item.get("productId").toString()));
                detail.setProduct(product);
                detail.setQuantity(Integer.parseInt(item.get("quantity").toString()));
                details.add(detail);
            }
            sale.setDetails(details);

            Sale savedSale = saleService.processSale(sale, authentication.getName());

            response.put("success", true);
            response.put("saleId", savedSale.getId());
            response.put("invoiceNumber", savedSale.getInvoiceNumber());
            response.put("totalAmount", savedSale.getTotalAmount());
            response.put("paidAmount", savedSale.getPaidAmount());
            response.put("changeAmount", savedSale.getChangeAmount());
            response.put("message", "Transaksi berhasil diproses!");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/today/stats")
    public ResponseEntity<Map<String, Object>> getTodayStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("count", saleService.getTodaySaleCount());
        stats.put("revenue", saleService.getTodayRevenue());
        return ResponseEntity.ok(stats);
    }
}
