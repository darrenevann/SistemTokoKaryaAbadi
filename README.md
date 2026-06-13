
# Toko Karya Abadi - Modern Retail Management System

Sistem Manajemen Retail (Point of Sale & Inventory) berbasis web yang dibangun untuk efisiensi operasional toko. Proyek ini mengimplementasikan arsitektur MVC dengan teknologi Spring Boot 3.5, Java 21, dan Thymeleaf.

## 🎯 Fokus Utama

* **Kecepatan**: Alur transaksi kasir yang dioptimalkan untuk meminimalkan waktu tunggu.
* **Integritas Data**: Sinkronisasi stok otomatis antara penjualan dan inventori.
* **Keamanan**: Kontrol akses berbasis peran (RBAC) untuk melindungi data sensitif.

## 🚀 Fitur Utama

### 🛒 Point of Sale (POS)

* Antarmuka kasir yang responsif dan mobile-friendly.
* Dukungan barcode scanner untuk input produk cepat.
* Pencetakan struk instan.

### 📦 Manajemen Inventori

* Manajemen produk, kategori, dan supplier.
* Pelacakan stok masuk dan pelaporan barang rusak (damaged goods).
* Low Stock Alert untuk menghindari kehabisan stok.

### 📈 Analitik & Keuangan

* Dashboard interaktif dengan grafik penjualan (Chart.js).
* Rekonsiliasi kas untuk audit harian.
* Riwayat perubahan harga (Audit Trail).

## 🛠️ Stack Teknologi

* **Framework**: Spring Boot 3.5.0
* **Bahasa**: Java 21
* **Database**: MySQL
* **Template Engine**: Thymeleaf
* **Keamanan**: Spring Security 6
* **Build Tool**: Maven

## 💻 Cara Menjalankan Aplikasi

1. **Konfigurasi Database**
* Buat database MySQL baru dengan nama `tokokaryaabadi`.
* Sesuaikan konfigurasi di `src/main/resources/application.properties` (username/password database Anda).


2. **Kompilasi & Jalankan**
Di terminal pada direktori root, jalankan perintah:
```bash
./mvnw clean compile
./mvnw spring-boot:run

```


3. **Akses Aplikasi**
* Buka browser dan akses `http://localhost:8080`.
* Sistem akan melakukan seeding data awal secara otomatis saat pertama kali dijalankan.



## 🔐 Kredensial Default (Data Seeder)

| Role | Username | Password |
| --- | --- | --- |
| **Admin** | `admin` | `admin123` |
| **Kasir** | `kasir` | `admin123` |
| **Owner** | `owner` | `admin123` |

## 🎨 Design System

UI dibangun dengan **Vanilla CSS** untuk performa maksimal.

* **Primary**: `#0B2B26`
* **Accent**: `#235347`
* **Success**: `#8EB69B`
