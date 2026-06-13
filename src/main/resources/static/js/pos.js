/**
 * Toko Karya Abadi - POS (Point of Sale) JavaScript
 * Handles cart management, barcode scanning, payment processing
 */

const POS = {
    cart: [],
    products: [], // Loaded from Thymeleaf model

    // ============================================================
    // INIT
    // ============================================================
    init(productsData) {
        this.products = productsData || [];
        this.setupBarcodeInput();
        this.setupProductSearch();
        this.renderCart();
        this.updateTotals();

        // Focus barcode input on load
        const barcodeInput = document.getElementById('barcode-input');
        if (barcodeInput) {
            barcodeInput.focus();
        }
    },

    // ============================================================
    // BARCODE INPUT
    // ============================================================
    setupBarcodeInput() {
        const input = document.getElementById('barcode-input');
        if (!input) return;

        input.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                const barcode = input.value.trim();
                if (barcode) {
                    this.lookupByBarcode(barcode);
                    input.value = '';
                }
            }
        });
    },

    async lookupByBarcode(barcode) {
        try {
            const response = await fetch(`/api/products/barcode/${encodeURIComponent(barcode)}`);
            const data = await response.json();

            if (data.success) {
                this.addToCart({
                    id: data.id,
                    barcode: data.barcode,
                    name: data.name,
                    sellPrice: data.sellPrice,
                    buyPrice: data.buyPrice,
                    stock: data.stock,
                    unit: data.unit
                });
            } else {
                Toast.error('Produk Tidak Ditemukan', data.message);
            }
        } catch (err) {
            Toast.error('Error', 'Gagal mencari produk. Periksa koneksi.');
        }
    },

    // ============================================================
    // PRODUCT SEARCH
    // ============================================================
    setupProductSearch() {
        const searchInput = document.getElementById('product-search');
        if (!searchInput) return;

        let timeout;
        searchInput.addEventListener('input', (e) => {
            clearTimeout(timeout);
            timeout = setTimeout(() => {
                this.filterProductGrid(e.target.value.toLowerCase().trim());
            }, 200);
        });
    },

    filterProductGrid(keyword) {
        const cards = document.querySelectorAll('.product-card[data-product]');
        cards.forEach(card => {
            const name = card.getAttribute('data-name') || '';
            const barcode = card.getAttribute('data-barcode') || '';
            const match = name.toLowerCase().includes(keyword) || barcode.includes(keyword);
            card.style.display = match ? '' : 'none';
        });
    },

    // ============================================================
    // CART MANAGEMENT
    // ============================================================
    addToCart(product) {
        const existingIndex = this.cart.findIndex(item => item.id === product.id);

        if (existingIndex >= 0) {
            const item = this.cart[existingIndex];
            if (item.qty >= product.stock) {
                Toast.warning('Stok Tidak Cukup', `Stok ${product.name} hanya ${product.stock} ${product.unit}`);
                return;
            }
            item.qty++;
        } else {
            if (product.stock <= 0) {
                Toast.error('Stok Habis', `${product.name} tidak tersedia`);
                return;
            }
            this.cart.push({
                id: product.id,
                barcode: product.barcode,
                name: product.name,
                sellPrice: parseFloat(product.sellPrice),
                buyPrice: parseFloat(product.buyPrice),
                stock: product.stock,
                unit: product.unit,
                qty: 1
            });
        }

        this.renderCart();
        this.updateTotals();
        Toast.success('Ditambahkan!', `${product.name} masuk keranjang`);
    },

    removeFromCart(productId) {
        this.cart = this.cart.filter(item => item.id !== productId);
        this.renderCart();
        this.updateTotals();
    },

    updateQty(productId, newQty) {
        const item = this.cart.find(i => i.id === productId);
        if (!item) return;

        if (newQty <= 0) {
            this.removeFromCart(productId);
            return;
        }
        if (newQty > item.stock) {
            Toast.warning('Stok Tidak Cukup', `Stok maksimal: ${item.stock} ${item.unit}`);
            return;
        }
        item.qty = newQty;
        this.renderCart();
        this.updateTotals();
    },

    clearCart() {
        if (this.cart.length === 0) return;
        if (confirm('Apakah Anda yakin ingin menghapus semua item di keranjang?')) {
            this.cart = [];
            this.renderCart();
            this.updateTotals();
            Toast.info('Keranjang', 'Keranjang berhasil dikosongkan');
        }
    },

    // ============================================================
    // RENDER CART
    // ============================================================
    renderCart() {
        const container = document.getElementById('cart-items');
        if (!container) return;

        const cartCount = document.getElementById('cart-count');
        if (cartCount) cartCount.textContent = this.cart.length;

        if (this.cart.length === 0) {
            container.innerHTML = `
                <div class="cart-empty">
                    <div class="cart-empty-icon">🛒</div>
                    <p>Keranjang kosong</p>
                    <small>Scan barcode atau klik produk</small>
                </div>
            `;
            return;
        }

        container.innerHTML = this.cart.map(item => `
            <div class="cart-item" id="cart-item-${item.id}">
                <div class="cart-item-info">
                    <div class="cart-item-name" title="${item.name}">${item.name}</div>
                    <div class="cart-item-price">${Formatter.currency(item.sellPrice)} / ${item.unit}</div>
                </div>
                <div class="cart-item-qty">
                    <button class="qty-btn" onclick="POS.updateQty(${item.id}, ${item.qty - 1})">−</button>
                    <span class="qty-val">${item.qty}</span>
                    <button class="qty-btn" onclick="POS.updateQty(${item.id}, ${item.qty + 1})">+</button>
                </div>
                <div class="cart-item-subtotal">${Formatter.currency(item.sellPrice * item.qty)}</div>
                <button class="delete-cart-item" onclick="POS.removeFromCart(${item.id})" title="Hapus">🗑</button>
            </div>
        `).join('');
    },

    // ============================================================
    // TOTALS
    // ============================================================
    updateTotals() {
        const subtotal = this.cart.reduce((sum, item) => sum + (item.sellPrice * item.qty), 0);
        const discount = parseFloat(document.getElementById('discount-input')?.value || 0);
        const total = Math.max(0, subtotal - discount);

        const elSubtotal = document.getElementById('cart-subtotal');
        const elDiscount = document.getElementById('cart-discount');
        const elTotal = document.getElementById('cart-total');
        const elItemCount = document.getElementById('item-count');

        if (elSubtotal) elSubtotal.textContent = Formatter.currency(subtotal);
        if (elDiscount) elDiscount.textContent = Formatter.currency(discount);
        if (elTotal) elTotal.textContent = Formatter.currency(total);
        if (elItemCount) elItemCount.textContent = this.cart.reduce((s, i) => s + i.qty, 0);

        this.calculateChange();
    },

    calculateChange() {
        const total = this.cart.reduce((sum, item) => sum + (item.sellPrice * item.qty), 0);
        const discount = parseFloat(document.getElementById('discount-input')?.value || 0);
        const grandTotal = Math.max(0, total - discount);
        const paid = parseFloat(document.getElementById('paid-amount')?.value || 0);
        const change = paid - grandTotal;

        const elChange = document.getElementById('change-amount');
        if (elChange) {
            elChange.textContent = Formatter.currency(Math.max(0, change));
            elChange.classList.toggle('text-danger', change < 0);
            elChange.classList.toggle('text-success', change >= 0);
        }
    },

    // ============================================================
    // PROCESS PAYMENT
    // ============================================================
    async processPayment() {
        if (this.cart.length === 0) {
            Toast.error('Keranjang Kosong', 'Tambahkan produk terlebih dahulu');
            return;
        }

        const paidAmount = parseFloat(document.getElementById('paid-amount')?.value || 0);
        const discount = parseFloat(document.getElementById('discount-input')?.value || 0);
        const total = this.cart.reduce((sum, item) => sum + (item.sellPrice * item.qty), 0) - discount;

        if (paidAmount < total) {
            Toast.error('Pembayaran Kurang', `Kurang: ${Formatter.currency(total - paidAmount)}`);
            return;
        }

        const paymentMethod = document.getElementById('payment-method')?.value || 'CASH';
        const notes = document.getElementById('sale-notes')?.value || '';

        const payload = {
            paidAmount: paidAmount,
            discount: discount,
            paymentMethod: paymentMethod,
            notes: notes,
            items: this.cart.map(item => ({
                productId: item.id,
                quantity: item.qty
            }))
        };

        // Show processing state
        const payBtn = document.getElementById('pay-btn');
        if (payBtn) {
            payBtn.disabled = true;
            payBtn.innerHTML = '<span class="spinner"></span> Memproses...';
        }

        try {
            const response = await fetch('/api/sales/process', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            const result = await response.json();

            if (result.success) {
                this.showSuccessModal(result);
            } else {
                Toast.error('Transaksi Gagal', result.message);
            }
        } catch (err) {
            Toast.error('Error', 'Gagal memproses transaksi. Silakan coba lagi.');
        } finally {
            if (payBtn) {
                payBtn.disabled = false;
                payBtn.innerHTML = '💳 Bayar';
            }
        }
    },

    showSuccessModal(result) {
        const modal = document.getElementById('success-modal');
        if (modal) {
            modal.querySelector('#result-invoice').textContent = result.invoiceNumber;
            modal.querySelector('#result-total').textContent = Formatter.currency(result.totalAmount);
            modal.querySelector('#result-paid').textContent = Formatter.currency(result.paidAmount);
            modal.querySelector('#result-change').textContent = Formatter.currency(result.changeAmount);

            const receiptLink = modal.querySelector('#receipt-link');
            if (receiptLink) receiptLink.href = `/sales/receipt/${result.saleId}`;

            Modal.open('success-modal');
            this.cart = [];
            this.renderCart();
            this.updateTotals();

            // Reset payment inputs
            const paidInput = document.getElementById('paid-amount');
            if (paidInput) paidInput.value = '';
        }
    }
};
