package com.example.demospringboot.config;

import com.example.demospringboot.entity.*;
import com.example.demospringboot.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedCategories();
        seedSuppliers();
        seedProducts();
        seedSales();
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ADMIN", "Administrator - Akses penuh ke semua fitur", null));
            roleRepository.save(new Role(null, "KASIR", "Kasir - Akses ke POS dan cetak struk", null));
            roleRepository.save(new Role(null, "OWNER", "Owner/Manager - Akses ke dashboard dan laporan", null));
        }
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            Role kasirRole = roleRepository.findByName("KASIR").orElseThrow();
            Role ownerRole = roleRepository.findByName("OWNER").orElseThrow();

            String encodedPassword = passwordEncoder.encode("admin123");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword);
            admin.setFullName("Administrator");
            admin.setEmail("admin@tokokarya.com");
            admin.setPhone("08123456789");
            admin.setRole(adminRole);
            admin.setIsActive(true);
            userRepository.save(admin);

            User kasir = new User();
            kasir.setUsername("kasir1");
            kasir.setPassword(encodedPassword);
            kasir.setFullName("Kasir Utama");
            kasir.setEmail("kasir@tokokarya.com");
            kasir.setPhone("08234567890");
            kasir.setRole(kasirRole);
            kasir.setIsActive(true);
            userRepository.save(kasir);

            User owner = new User();
            owner.setUsername("owner");
            owner.setPassword(encodedPassword);
            owner.setFullName("Pemilik Toko");
            owner.setEmail("owner@tokokarya.com");
            owner.setPhone("08345678901");
            owner.setRole(ownerRole);
            owner.setIsActive(true);
            userRepository.save(owner);
        }
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            String[][] cats = {
                    { "Makanan & Minuman", "Produk makanan dan minuman" },
                    { "Kebutuhan Rumah Tangga", "Produk rumah tangga" },
                    { "Kebersihan & Perawatan", "Produk kebersihan dan perawatan diri" },
                    { "Elektronik", "Produk elektronik dan aksesoris" },
                    { "Alat Tulis", "Alat tulis dan perlengkapan kantor" },
                    { "Frozen Food", "Makanan beku" },
                    { "Snack & Permen", "Makanan ringan dan permen" },
                    { "Minuman Kemasan", "Minuman botol dan kemasan" }
            };
            for (String[] cat : cats) {
                categoryRepository.save(new Category(null, cat[0], cat[1], null));
            }
        }
    }

    private void seedSuppliers() {
        if (supplierRepository.count() == 0) {
            Object[][] suppliers = {
                    { "PT Indofood", "021-7534000", "sales@indofood.co.id", "Jakarta Selatan" },
                    { "PT Unilever Indonesia", "021-5299000", "contact@unilever.co.id", "Tangerang" },
                    { "CV Berkah Jaya", "0274-512000", "berkah@email.com", "Yogyakarta" },
                    { "UD Sumber Rejeki", "031-845000", "sumber@email.com", "Surabaya" }
            };
            for (Object[] s : suppliers) {
                Supplier supplier = new Supplier();
                supplier.setName((String) s[0]);
                supplier.setPhone((String) s[1]);
                supplier.setEmail((String) s[2]);
                supplier.setAddress((String) s[3]);
                supplier.setIsActive(true);
                supplierRepository.save(supplier);
            }
        }
    }

    private void seedProducts() {
        if (productRepository.count() == 0) {
            Category cat1 = categoryRepository.findByName("Makanan & Minuman").orElseThrow();
            Category cat3 = categoryRepository.findByName("Kebersihan & Perawatan").orElseThrow();
            Category cat7 = categoryRepository.findByName("Snack & Permen").orElseThrow();
            Category cat8 = categoryRepository.findByName("Minuman Kemasan").orElseThrow();
            Category cat2 = categoryRepository.findByName("Kebutuhan Rumah Tangga").orElseThrow();

            Object[][] products = {
                    { "8999999820112", "Indomie Goreng", cat1, 2800.0, 3500.0, 100, 20 },
                    { "8992388505018", "Aqua 600ml", cat1, 2000.0, 3000.0, 150, 30 },
                    { "8991102100015", "Sabun Lifebuoy 90gr", cat3, 3500.0, 5000.0, 50, 10 },
                    { "8997023440027", "Chitato BBQ 55gr", cat7, 6500.0, 9000.0, 80, 15 },
                    { "8999999010028", "Teh Botol Sosro 450ml", cat8, 4000.0, 6000.0, 120, 25 },
                    { "8991102150039", "Pepsodent 120gr", cat3, 8000.0, 12000.0, 40, 10 },
                    { "8998866240040", "Rinso Anti Noda 1kg", cat2, 18000.0, 24000.0, 30, 8 },
                    { "8997023110051", "Oreo 119gr", cat7, 9000.0, 13000.0, 60, 12 }
            };

            for (Object[] p : products) {
                Product product = new Product();
                product.setBarcode((String) p[0]);
                product.setName((String) p[1]);
                product.setCategory((Category) p[2]);
                product.setBuyPrice(BigDecimal.valueOf((Double) p[3]));
                product.setSellPrice(BigDecimal.valueOf((Double) p[4]));
                product.setStock((Integer) p[5]);
                product.setMinStock((Integer) p[6]);
                product.setUnit("pcs");
                product.setIsActive(true);
                productRepository.save(product);
            }
        }
    }

    private void seedSales() {
        if (saleRepository.count() < 10) {
            List<User> users = userRepository.findAll();
            if (users.isEmpty())
                return;
            User kasir = users.stream().filter(u -> "KASIR".equals(u.getRole().getName())).findFirst()
                    .orElse(users.get(0));

            List<Product> products = productRepository.findAll();
            if (products.isEmpty())
                return;

            Random random = new Random();
            LocalDateTime now = LocalDateTime.now();

            // Generate about 90 days of data (last 3 months)
            for (int i = 90; i >= 0; i--) {
                // 1 to 5 sales per day
                int salesPerDay = random.nextInt(5) + 1;
                LocalDateTime currentDay = now.minusDays(i);

                for (int j = 0; j < salesPerDay; j++) {
                    Sale sale = new Sale();

                    // random time in the day
                    LocalDateTime soldAt = currentDay.withHour(8 + random.nextInt(12)).withMinute(random.nextInt(60));
                    sale.setSoldAt(soldAt);
                    sale.setCreatedAt(soldAt);

                    String invoiceStr = "TRX-" + soldAt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "-"
                            + random.nextInt(1000);
                    sale.setInvoiceNumber(invoiceStr);
                    sale.setUser(kasir);

                    String[] methods = { "CASH", "DEBIT", "QRIS" };
                    sale.setPaymentMethod(methods[random.nextInt(methods.length)]);
                    sale.setStatus("COMPLETED");

                    List<SaleDetail> details = new ArrayList<>();
                    int numItems = random.nextInt(3) + 1;
                    BigDecimal totalAmount = BigDecimal.ZERO;

                    for (int k = 0; k < numItems; k++) {
                        Product p = products.get(random.nextInt(products.size()));
                        int qty = random.nextInt(3) + 1;

                        SaleDetail detail = new SaleDetail();
                        detail.setSale(sale);
                        detail.setProduct(p);
                        detail.setQuantity(qty);
                        detail.setSellPrice(p.getSellPrice());
                        detail.setBuyPrice(p.getBuyPrice());

                        BigDecimal subtotal = p.getSellPrice().multiply(BigDecimal.valueOf(qty));
                        detail.setSubtotal(subtotal);
                        detail.setCreatedAt(soldAt);

                        details.add(detail);
                        totalAmount = totalAmount.add(subtotal);
                    }

                    sale.setDetails(details);
                    sale.setTotalAmount(totalAmount);
                    sale.setDiscount(BigDecimal.ZERO);

                    // Paid amount is totalAmount rounded up to nearest 5000 or 10000 or exact
                    BigDecimal paidAmount = totalAmount;
                    if (sale.getPaymentMethod().equals("CASH")) {
                        long totalLong = totalAmount.longValue();
                        long remainder = totalLong % 50000;
                        if (remainder > 0 && random.nextBoolean()) {
                            paidAmount = BigDecimal.valueOf(totalLong - remainder + 50000);
                        } else if (random.nextBoolean()) {
                            paidAmount = BigDecimal.valueOf(totalLong - (totalLong % 10000) + 10000);
                        }
                    }
                    sale.setPaidAmount(paidAmount);
                    sale.setChangeAmount(paidAmount.subtract(totalAmount));

                    saleRepository.save(sale);
                }
            }
        }
    }
}
