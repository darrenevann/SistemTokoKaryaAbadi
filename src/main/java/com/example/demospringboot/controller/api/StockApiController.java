package com.example.demospringboot.controller.api;

import com.example.demospringboot.entity.StockIn;
import com.example.demospringboot.entity.StockInDetail;
import com.example.demospringboot.entity.Product;
import com.example.demospringboot.entity.Supplier;
import com.example.demospringboot.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockApiController {

    @Autowired
    private StockService stockService;

    @PostMapping("/receive")
    public ResponseEntity<Map<String, Object>> receiveStock(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();
        try {
            StockIn stockIn = new StockIn();

            Supplier supplier = new Supplier();
            supplier.setId(Long.parseLong(request.get("supplierId").toString()));
            stockIn.setSupplier(supplier);

            String dateStr = request.getOrDefault("receivedAt", LocalDate.now().toString()).toString();
            stockIn.setReceivedAt(LocalDate.parse(dateStr));
            stockIn.setNotes((String) request.getOrDefault("notes", ""));

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");
            List<StockInDetail> details = new ArrayList<>();

            for (Map<String, Object> item : items) {
                StockInDetail detail = new StockInDetail();
                Product product = new Product();
                product.setId(Long.parseLong(item.get("productId").toString()));
                detail.setProduct(product);
                detail.setQuantity(Integer.parseInt(item.get("quantity").toString()));
                detail.setBuyPrice(new BigDecimal(item.get("buyPrice").toString()));

                if (item.containsKey("expiredDate") && item.get("expiredDate") != null && !item.get("expiredDate").toString().isEmpty()) {
                    detail.setExpiredDate(LocalDate.parse(item.get("expiredDate").toString()));
                }
                details.add(detail);
            }
            stockIn.setDetails(details);

            StockIn saved = stockService.createStockIn(stockIn, authentication.getName());

            response.put("success", true);
            response.put("invoiceNumber", saved.getInvoiceNumber());
            response.put("message", "Penerimaan stok berhasil disimpan!");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
