package com.example.demospringboot.service;

import com.example.demospringboot.entity.Supplier;
import com.example.demospringboot.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllActiveSuppliers() {
        return supplierRepository.findByIsActiveTrue();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Supplier supplier) {
        Supplier existing = supplierRepository.findById(supplier.getId())
                .orElseThrow(() -> new RuntimeException("Supplier tidak ditemukan"));
        existing.setName(supplier.getName());
        existing.setPhone(supplier.getPhone());
        existing.setEmail(supplier.getEmail());
        existing.setAddress(supplier.getAddress());
        existing.setIsActive(supplier.getIsActive());
        return supplierRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier tidak ditemukan"));
        supplier.setIsActive(false);
        supplierRepository.save(supplier);
    }

    public long countActiveSuppliers() {
        return supplierRepository.findByIsActiveTrue().size();
    }
}
