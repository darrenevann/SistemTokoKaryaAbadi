package com.example.demospringboot;

import com.example.demospringboot.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CheckDb {

    @Autowired
    private SaleRepository saleRepository;

    @Test
    void checkSalesCount() {
        System.out.println("====== SALES COUNT IN DB: " + saleRepository.count() + " ======");
    }
}
