    -- ============================================================
    -- TOKO KARYA ABADI - Database Schema
    -- Modern Retail Management System
    -- ============================================================

    CREATE DATABASE IF NOT EXISTS toko_karya_abadi CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    USE toko_karya_abadi;

    -- ============================================================
    -- TABLE: roles
    -- ============================================================
    CREATE TABLE IF NOT EXISTS roles (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        name        VARCHAR(50) NOT NULL UNIQUE,
        description VARCHAR(255),
        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    -- ============================================================
    -- TABLE: users
    -- ============================================================
    CREATE TABLE IF NOT EXISTS users (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        username    VARCHAR(50) NOT NULL UNIQUE,
        password    VARCHAR(255) NOT NULL,
        full_name   VARCHAR(100) NOT NULL,
        email       VARCHAR(100) UNIQUE,
        phone       VARCHAR(20),
        role_id     BIGINT NOT NULL,
        is_active   BOOLEAN DEFAULT TRUE,
        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES roles(id)
    );

    -- ============================================================
    -- TABLE: categories
    -- ============================================================
    CREATE TABLE IF NOT EXISTS categories (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        name        VARCHAR(100) NOT NULL UNIQUE,
        description VARCHAR(255),
        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

    -- ============================================================
    -- TABLE: suppliers
    -- ============================================================
    CREATE TABLE IF NOT EXISTS suppliers (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        name        VARCHAR(100) NOT NULL,
        phone       VARCHAR(20),
        email       VARCHAR(100),
        address     TEXT,
        is_active   BOOLEAN DEFAULT TRUE,
        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

    -- ============================================================
    -- TABLE: products
    -- ============================================================
    CREATE TABLE IF NOT EXISTS products (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        barcode         VARCHAR(50) NOT NULL UNIQUE,
        name            VARCHAR(150) NOT NULL,
        category_id     BIGINT NOT NULL,
        buy_price       DECIMAL(15,2) NOT NULL DEFAULT 0,
        sell_price      DECIMAL(15,2) NOT NULL DEFAULT 0,
        stock           INT NOT NULL DEFAULT 0,
        min_stock       INT NOT NULL DEFAULT 5,
        unit            VARCHAR(20) DEFAULT 'pcs',
        is_active       BOOLEAN DEFAULT TRUE,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id)
    );

    -- ============================================================
    -- TABLE: stock_in (Penerimaan Stok Header)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS stock_in (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        invoice_number  VARCHAR(50) NOT NULL UNIQUE,
        supplier_id     BIGINT NOT NULL,
        user_id         BIGINT NOT NULL,
        total_amount    DECIMAL(15,2) NOT NULL DEFAULT 0,
        notes           TEXT,
        received_at     DATE NOT NULL,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_stock_in_supplier FOREIGN KEY (supplier_id) REFERENCES suppliers(id),
        CONSTRAINT fk_stock_in_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

    -- ============================================================
    -- TABLE: stock_in_details (Penerimaan Stok Detail)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS stock_in_details (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        stock_in_id     BIGINT NOT NULL,
        product_id      BIGINT NOT NULL,
        quantity        INT NOT NULL,
        buy_price       DECIMAL(15,2) NOT NULL,
        subtotal        DECIMAL(15,2) NOT NULL,
        expired_date    DATE,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_sid_stock_in FOREIGN KEY (stock_in_id) REFERENCES stock_in(id),
        CONSTRAINT fk_sid_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

    -- ============================================================
    -- TABLE: sales (Transaksi Penjualan Header)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS sales (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        invoice_number  VARCHAR(50) NOT NULL UNIQUE,
        user_id         BIGINT NOT NULL,
        total_amount    DECIMAL(15,2) NOT NULL DEFAULT 0,
        paid_amount     DECIMAL(15,2) NOT NULL DEFAULT 0,
        change_amount   DECIMAL(15,2) NOT NULL DEFAULT 0,
        discount        DECIMAL(15,2) NOT NULL DEFAULT 0,
        payment_method  VARCHAR(20) DEFAULT 'CASH',
        status          VARCHAR(20) DEFAULT 'COMPLETED',
        notes           TEXT,
        sold_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_sales_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

    -- ============================================================
    -- TABLE: sales_details (Transaksi Penjualan Detail)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS sales_details (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        sale_id         BIGINT NOT NULL,
        product_id      BIGINT NOT NULL,
        quantity        INT NOT NULL,
        sell_price      DECIMAL(15,2) NOT NULL,
        buy_price       DECIMAL(15,2) NOT NULL,
        subtotal        DECIMAL(15,2) NOT NULL,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_sld_sale FOREIGN KEY (sale_id) REFERENCES sales(id),
        CONSTRAINT fk_sld_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

    -- ============================================================
    -- TABLE: damaged_products (Barang Rusak/Kadaluarsa)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS damaged_products (
        id          BIGINT AUTO_INCREMENT PRIMARY KEY,
        product_id  BIGINT NOT NULL,
        user_id     BIGINT NOT NULL,
        quantity    INT NOT NULL,
        reason      VARCHAR(50) NOT NULL COMMENT 'DAMAGED, EXPIRED, LOST',
        loss_amount DECIMAL(15,2) NOT NULL DEFAULT 0,
        description TEXT,
        reported_at DATE NOT NULL,
        created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_dp_product FOREIGN KEY (product_id) REFERENCES products(id),
        CONSTRAINT fk_dp_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

    -- ============================================================
    -- TABLE: price_history (Riwayat Perubahan Harga)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS price_history (
        id              BIGINT AUTO_INCREMENT PRIMARY KEY,
        product_id      BIGINT NOT NULL,
        user_id         BIGINT NOT NULL,
        old_sell_price  DECIMAL(15,2) NOT NULL,
        new_sell_price  DECIMAL(15,2) NOT NULL,
        old_buy_price   DECIMAL(15,2),
        new_buy_price   DECIMAL(15,2),
        reason          VARCHAR(255),
        changed_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_ph_product FOREIGN KEY (product_id) REFERENCES products(id),
        CONSTRAINT fk_ph_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

    -- ============================================================
    -- TABLE: cash_reconciliation (Rekonsiliasi Kas)
    -- ============================================================
    CREATE TABLE IF NOT EXISTS cash_reconciliation (
        id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
        user_id             BIGINT NOT NULL,
        reconciliation_date DATE NOT NULL,
        expected_cash       DECIMAL(15,2) NOT NULL DEFAULT 0,
        actual_cash         DECIMAL(15,2) NOT NULL DEFAULT 0,
        difference          DECIMAL(15,2) NOT NULL DEFAULT 0,
        status              VARCHAR(20) DEFAULT 'PENDING' COMMENT 'MATCH, OVER, SHORT',
        notes               TEXT,
        created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_cr_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

    -- ============================================================
    -- INDEXES for performance
    -- ============================================================
    CREATE INDEX idx_products_barcode ON products(barcode);
    CREATE INDEX idx_products_category ON products(category_id);
    CREATE INDEX idx_sales_sold_at ON sales(sold_at);
    CREATE INDEX idx_sales_user ON sales(user_id);
    CREATE INDEX idx_stock_in_received ON stock_in(received_at);
    CREATE INDEX idx_damaged_reported ON damaged_products(reported_at);

    -- ============================================================
    -- SEED DATA: Roles
    -- ============================================================
    INSERT IGNORE INTO roles (name, description) VALUES
    ('ADMIN', 'Administrator - Akses penuh ke semua fitur'),
    ('KASIR', 'Kasir - Akses ke POS dan cetak struk'),
    ('OWNER', 'Owner/Manager - Akses ke dashboard dan laporan');

    -- ============================================================
    -- SEED DATA: Default Admin User
    -- Password: admin123 (BCrypt encoded)
    -- ============================================================
    INSERT IGNORE INTO users (username, password, full_name, email, phone, role_id, is_active)
    VALUES (
        'admin',
        '$2a$10$slYQmyNdgTY2Ij.t4z.RJ.YNxCfniHmHIbqMvk8dGjGJp71L6PJPG',
        'Administrator',
        'admin@tokokarya.com',
        '08123456789',
        (SELECT id FROM roles WHERE name = 'ADMIN'),
        TRUE
    );

    INSERT IGNORE INTO users (username, password, full_name, email, phone, role_id, is_active)
    VALUES (
        'kasir1',
        '$2a$10$slYQmyNdgTY2Ij.t4z.RJ.YNxCfniHmHIbqMvk8dGjGJp71L6PJPG',
        'Kasir Utama',
        'kasir@tokokarya.com',
        '08234567890',
        (SELECT id FROM roles WHERE name = 'KASIR'),
        TRUE
    );

    INSERT IGNORE INTO users (username, password, full_name, email, phone, role_id, is_active)
    VALUES (
        'owner',
        '$2a$10$slYQmyNdgTY2Ij.t4z.RJ.YNxCfniHmHIbqMvk8dGjGJp71L6PJPG',
        'Pemilik Toko',
        'owner@tokokarya.com',
        '08345678901',
        (SELECT id FROM roles WHERE name = 'OWNER'),
        TRUE
    );

    -- ============================================================
    -- SEED DATA: Categories
    -- ============================================================
    INSERT IGNORE INTO categories (name, description) VALUES
    ('Makanan & Minuman', 'Produk makanan dan minuman'),
    ('Kebutuhan Rumah Tangga', 'Produk rumah tangga'),
    ('Kebersihan & Perawatan', 'Produk kebersihan dan perawatan diri'),
    ('Elektronik', 'Produk elektronik dan aksesoris'),
    ('Alat Tulis', 'Alat tulis dan perlengkapan kantor'),
    ('Frozen Food', 'Makanan beku'),
    ('Snack & Permen', 'Makanan ringan dan permen'),
    ('Minuman Kemasan', 'Minuman botol dan kemasan');

    -- ============================================================
    -- SEED DATA: Suppliers
    -- ============================================================
    INSERT IGNORE INTO suppliers (name, phone, email, address) VALUES
    ('PT Indofood', '021-7534xxx', 'sales@indofood.co.id', 'Jakarta Selatan, DKI Jakarta'),
    ('PT Unilever Indonesia', '021-5299xxxx', 'contact@unilever.co.id', 'Tangerang, Banten'),
    ('CV Berkah Jaya', '0274-512xxx', 'berkah@email.com', 'Yogyakarta, DIY'),
    ('UD Sumber Rejeki', '031-845xxxx', 'sumber@email.com', 'Surabaya, Jawa Timur');

    -- ============================================================
    -- SEED DATA: Sample Products
    -- ============================================================
    INSERT IGNORE INTO products (barcode, name, category_id, buy_price, sell_price, stock, min_stock, unit) VALUES
    ('8999999820112', 'Indomie Goreng', 1, 2800, 3500, 100, 20, 'pcs'),
    ('8992388505018', 'Aqua 600ml', 1, 2000, 3000, 150, 30, 'pcs'),
    ('8991102100015', 'Sabun Lifebuoy 90gr', 3, 3500, 5000, 50, 10, 'pcs'),
    ('8997023440027', 'Chitato BBQ 55gr', 7, 6500, 9000, 80, 15, 'pcs'),
    ('8999999010028', 'Teh Botol Sosro 450ml', 8, 4000, 6000, 120, 25, 'pcs'),
    ('8991102150039', 'Pepsodent 120gr', 3, 8000, 12000, 40, 10, 'pcs'),
    ('8998866240040', 'Rinso Anti Noda 1kg', 2, 18000, 24000, 30, 8, 'pcs'),
    ('8997023110051', 'Oreo 119gr', 7, 9000, 13000, 60, 12, 'pcs');
