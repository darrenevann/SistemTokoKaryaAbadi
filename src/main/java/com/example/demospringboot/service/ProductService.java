package com.example.demospringboot.service;

import com.example.demospringboot.entity.Category;
import com.example.demospringboot.entity.PriceHistory;
import com.example.demospringboot.entity.Product;
import com.example.demospringboot.entity.User;
import com.example.demospringboot.repository.CategoryRepository;
import com.example.demospringboot.repository.PriceHistoryRepository;
import com.example.demospringboot.repository.ProductRepository;
import com.example.demospringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PriceHistoryRepository priceHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsActiveTrue();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.searchByKeyword(keyword);
    }

    public List<Product> getLowStockProducts() {
        return productRepository.findLowStockProducts();
    }

    public Product createProduct(Product product) {
        if (productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("Barcode sudah digunakan: " + product.getBarcode());
        }
        product.setStock(0);
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        Product existing = productRepository.findById(product.getId())
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        // Check if barcode changed and conflicts
        if (!existing.getBarcode().equals(product.getBarcode()) &&
                productRepository.existsByBarcode(product.getBarcode())) {
            throw new RuntimeException("Barcode sudah digunakan oleh produk lain");
        }

        existing.setBarcode(product.getBarcode());
        existing.setName(product.getName());
        existing.setCategory(product.getCategory());
        existing.setBuyPrice(product.getBuyPrice());
        existing.setMinStock(product.getMinStock());
        existing.setUnit(product.getUnit());
        existing.setIsActive(product.getIsActive());

        return productRepository.save(existing);
    }

    public Product updatePrice(Long productId, BigDecimal newSellPrice, BigDecimal newBuyPrice,
                               String reason, String updatedByUsername) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));

        User user = userRepository.findByUsername(updatedByUsername)
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan"));

        // Save price history
        PriceHistory history = new PriceHistory();
        history.setProduct(product);
        history.setUser(user);
        history.setOldSellPrice(product.getSellPrice());
        history.setNewSellPrice(newSellPrice);
        history.setOldBuyPrice(product.getBuyPrice());
        history.setNewBuyPrice(newBuyPrice);
        history.setReason(reason);
        priceHistoryRepository.save(history);

        // Update product
        product.setSellPrice(newSellPrice);
        if (newBuyPrice != null) {
            product.setBuyPrice(newBuyPrice);
        }

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produk tidak ditemukan"));
        product.setIsActive(false);
        productRepository.save(product);
    }

    public void addStock(Product product, int quantity) {
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

    public void reduceStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new RuntimeException("Stok tidak mencukupi untuk: " + product.getName());
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Kategori sudah ada: " + category.getName());
        }
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori tidak ditemukan"));
        
        if (!existing.getName().equals(category.getName()) && 
            categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Kategori dengan nama tersebut sudah ada");
        }
        
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        if (!productRepository.findByCategoryId(id).isEmpty()) {
             throw new RuntimeException("Kategori tidak bisa dihapus karena masih ada produk yang menggunakan kategori ini");
        }
        categoryRepository.deleteById(id);
    }

    public long countActiveProducts() {
        return productRepository.findByIsActiveTrue().size();
    }
}
