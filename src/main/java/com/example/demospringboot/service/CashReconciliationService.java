package com.example.demospringboot.service;

import com.example.demospringboot.entity.CashReconciliation;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.repository.CashReconciliationRepository;
import com.example.demospringboot.repository.SaleRepository;
import com.example.demospringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CashReconciliationService {

    @Autowired
    private CashReconciliationRepository reconciliationRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CashReconciliation> getAllReconciliations() {
        return reconciliationRepository.findAllByOrderByReconciliationDateDesc();
    }

    public Optional<CashReconciliation> findById(Long id) {
        return reconciliationRepository.findById(id);
    }

    public CashReconciliation createReconciliation(LocalDate date, BigDecimal actualCash,
                                                    String notes, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Calculate expected cash from sales
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(23, 59, 59);
        BigDecimal expectedCash = saleRepository.sumTotalAmountBetween(start, end);
        if (expectedCash == null) expectedCash = BigDecimal.ZERO;

        BigDecimal difference = actualCash.subtract(expectedCash);

        String status;
        if (difference.compareTo(BigDecimal.ZERO) == 0) {
            status = "MATCH";
        } else if (difference.compareTo(BigDecimal.ZERO) > 0) {
            status = "OVER";
        } else {
            status = "SHORT";
        }

        CashReconciliation reconciliation = new CashReconciliation();
        reconciliation.setUser(user);
        reconciliation.setReconciliationDate(date);
        reconciliation.setExpectedCash(expectedCash);
        reconciliation.setActualCash(actualCash);
        reconciliation.setDifference(difference);
        reconciliation.setStatus(status);
        reconciliation.setNotes(notes);

        return reconciliationRepository.save(reconciliation);
    }
}
