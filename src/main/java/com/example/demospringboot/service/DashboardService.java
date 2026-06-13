package com.example.demospringboot.service;

import com.example.demospringboot.entity.Product;
import com.example.demospringboot.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SaleRepository saleRepository;

    public Map<String, Object> getDashboardData(String filter) {
        Map<String, Object> data = new HashMap<>();

        // Today stats
        data.put("todaySaleCount", saleService.getTodaySaleCount());
        data.put("todayRevenue", saleService.getTodayRevenue());

        // Product stats
        data.put("totalProducts", productService.countActiveProducts());
        data.put("totalSuppliers", supplierService.countActiveSuppliers());
        data.put("lowStockProducts", productService.getLowStockProducts());

        // Monthly profit
        LocalDateTime monthStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime monthEnd = LocalDate.now().atTime(23, 59, 59);
        BigDecimal monthlyProfit = saleRepository.sumProfitBetween(monthStart, monthEnd);
        data.put("monthlyProfit", monthlyProfit != null ? monthlyProfit : BigDecimal.ZERO);

        // Top 5 products this month
        List<Object[]> topProducts = saleRepository.findTopProductsBetween(monthStart, monthEnd);
        data.put("topProducts", topProducts.size() > 5 ? topProducts.subList(0, 5) : topProducts);

        // Chart data based on filter
        LocalDateTime chartStart;
        LocalDateTime chartEnd = LocalDate.now().atTime(23, 59, 59);
        String chartTitle = "Grafik Penjualan";
        
        if ("day".equals(filter)) {
            chartStart = LocalDate.now().minusDays(6).atStartOfDay();
            chartTitle = "Grafik Penjualan 7 Hari";
        } else if ("year".equals(filter)) {
            chartStart = LocalDate.now().minusDays(364).atStartOfDay();
            chartTitle = "Grafik Penjualan 1 Tahun";
        } else {
            // default to month
            chartStart = LocalDate.now().minusDays(29).atStartOfDay();
            chartTitle = "Grafik Penjualan 30 Hari";
        }
        
        List<Object[]> chartData = saleRepository.findDailySalesSummary(chartStart, chartEnd);
        data.put("weeklySales", chartData);
        data.put("chartTitle", chartTitle);

        return data;
    }

    public Map<String, Object> getReportData(LocalDate date) {
        Map<String, Object> data = new HashMap<>();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);

        data.put("date", date);
        data.put("totalTransactions", saleRepository.countCompletedSalesBetween(start, end));
        data.put("totalRevenue", saleRepository.sumTotalAmountBetween(start, end));
        data.put("totalProfit", saleRepository.sumProfitBetween(start, end));

        List<Object[]> topProducts = saleRepository.findTopProductsBetween(start, end);
        data.put("topProducts", topProducts.size() > 5 ? topProducts.subList(0, 5) : topProducts);

        List<Product> lowStock = productService.getLowStockProducts();
        data.put("lowStockProducts", lowStock);

        return data;
    }
}
