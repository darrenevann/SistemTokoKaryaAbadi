/**
 * Toko Karya Abadi - Stock Receive JavaScript
 */
const StockManager = {
    items: [],
    products: [],

    init(productsData) {
        this.products = productsData || [];
        this.renderItems();
    },

    addItem() {
        this.items.push({ productId: '', quantity: 1, buyPrice: 0, expiredDate: '' });
        this.renderItems();
    },

    removeItem(index) {
        this.items.splice(index, 1);
        this.renderItems();
    },

    updateItem(index, field, value) {
        this.items[index][field] = value;
    },

    renderItems() {
        const container = document.getElementById('stock-items-container');
        if (!container) return;

        if (this.items.length === 0) {
            container.innerHTML = `<p class="text-muted text-center" style="padding:16px">Klik "+ Tambah Barang" untuk mulai</p>`;
            return;
        }

        const productOptions = this.products.map(p =>
            `<option value="${p.id}" data-price="${p.buyPrice}">${p.name} (${p.barcode})</option>`
        ).join('');

        container.innerHTML = this.items.map((item, i) => `
            <div class="stock-item-row" style="display:grid; grid-template-columns: 2fr 1fr 1fr 1fr auto; gap:10px; margin-bottom:10px; align-items:center;">
                <select class="form-control" onchange="StockManager.updateItem(${i}, 'productId', this.value)" required>
                    <option value="">Pilih Produk...</option>
                    ${productOptions}
                </select>
                <input type="number" class="form-control" placeholder="Jumlah" min="1" value="${item.quantity}"
                    onchange="StockManager.updateItem(${i}, 'quantity', parseInt(this.value))" required>
                <input type="number" class="form-control" placeholder="Harga Beli" min="0" value="${item.buyPrice}"
                    onchange="StockManager.updateItem(${i}, 'buyPrice', parseFloat(this.value))" required>
                <input type="date" class="form-control"
                    onchange="StockManager.updateItem(${i}, 'expiredDate', this.value)">
                <button type="button" class="btn btn-danger btn-sm btn-icon" onclick="StockManager.removeItem(${i})">🗑</button>
            </div>
        `).join('');
    },

    async submit(event) {
        event.preventDefault();
        if (this.items.length === 0) {
            Toast.error('Error', 'Tambahkan minimal satu barang');
            return;
        }

        const supplierId = document.getElementById('supplier-id').value;
        const receivedAt = document.getElementById('received-at').value;
        const notes = document.getElementById('stock-notes')?.value || '';

        const payload = {
            supplierId: parseInt(supplierId),
            receivedAt: receivedAt,
            notes: notes,
            items: this.items
        };

        try {
            const response = await fetch('/api/stock/receive', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
            const result = await response.json();

            if (result.success) {
                Toast.success('Berhasil!', result.message);
                setTimeout(() => window.location.href = '/stock', 1500);
            } else {
                Toast.error('Gagal', result.message);
            }
        } catch (err) {
            Toast.error('Error', 'Gagal menyimpan data stok');
        }
    }
};
