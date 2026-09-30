# Toko Karya Abadi - Modern Retail Management System

Sistem Manajemen Retail Modern (Point of Sale & Inventory) berbasis Web yang dikembangkan dengan **Spring Boot 3.5**, **Java 21**, **MySQL**, dan **Thymeleaf**. Sistem ini dirancang untuk operasional toko retail dengan tampilan UI/UX yang modern, bersih, dan responsif.

## Fitur Utama

### Point of Sale (Kasir)

* Tampilan kasir yang modern dan responsif.
* Integrasi *barcode scanner* untuk input produk instan.
* Kalkulasi otomatis total belanja, diskon, dan kembalian.
* Manajemen keranjang belanja.
* Cetak struk ke PDF/Printer.

### Manajemen Inventori

* Master data produk (harga beli, harga jual, stok minimum).
* Manajemen kategori dan supplier.
* Pencatatan penerimaan stok dari supplier.
* Laporan barang rusak/kadaluarsa.
* Peringatan stok menipis (*Low Stock Alert*).

### Laporan & Keuangan

* Dashboard interaktif dengan grafik penjualan.
* Laporan harian dan statistik produk terlaris.
* Rekonsiliasi kas fisik.
* Audit trail riwayat perubahan harga produk.

### Manajemen Pengguna

* Role-Based Access Control (RBAC): `ADMIN`, `KASIR`, dan `OWNER`.
* Keamanan password menggunakan Bcrypt.
* Manajemen pengguna (CRUD) oleh Admin.

## Tech Stack

* **Backend:** Java 21, Spring Boot 3.5, Spring MVC, Spring Data JPA, Spring Security
* **Database:** MySQL
* **Frontend:** HTML5, Vanilla CSS, Vanilla JavaScript, Thymeleaf
* **Data Visualization:** Chart.js
* **Build Tool:** Maven

## Cara Menjalankan Aplikasi

1. **Database:**
* Pastikan MySQL server berjalan di port 3306.
* Buat database dengan nama `tokokaryaabadi`.
* Konfigurasi `application.properties` sesuai dengan kredensial database Anda.


2. **Kompilasi & Jalankan:**
Buka terminal di root direktori proyek, jalankan:
```bash
# Kompilasi
.\mvnw.cmd clean compile

# Jalankan
.\mvnw.cmd spring-boot:run


3. **Akses:**
* Buka browser dan arahkan ke: `http://localhost:8080`


4. **Login Default:**
* Admin: `admin` / `admin123`
