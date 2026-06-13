/**
 * Toko Karya Abadi - Global App JS
 * Common utilities: Toast, Modal, Clock, Sidebar
 */

// ============================================================
// TOAST NOTIFICATIONS
// ============================================================
const Toast = {
    container: null,

    init() {
        this.container = document.getElementById('toast-container');
        if (!this.container) {
            this.container = document.createElement('div');
            this.container.id = 'toast-container';
            document.body.appendChild(this.container);
        }
    },

    show(title, message, type = 'success', duration = 4000) {
        if (!this.container) this.init();

        const icons = { success: '✅', error: '❌', warning: '⚠️', info: 'ℹ️' };
        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        toast.innerHTML = `
            <span class="toast-icon">${icons[type] || icons.info}</span>
            <div class="toast-content">
                <div class="toast-title">${title}</div>
                ${message ? `<div class="toast-message">${message}</div>` : ''}
            </div>
            <button class="toast-close" onclick="Toast.remove(this.parentElement)">×</button>
        `;

        this.container.appendChild(toast);

        if (duration > 0) {
            setTimeout(() => this.remove(toast), duration);
        }
        return toast;
    },

    success(title, message) { return this.show(title, message, 'success'); },
    error(title, message) { return this.show(title, message, 'error'); },
    warning(title, message) { return this.show(title, message, 'warning'); },
    info(title, message) { return this.show(title, message, 'info'); },

    remove(toast) {
        if (!toast) return;
        toast.classList.add('removing');
        setTimeout(() => toast.remove(), 300);
    }
};

// ============================================================
// MODAL MANAGEMENT
// ============================================================
const Modal = {
    open(id) {
        const overlay = document.getElementById(id);
        if (overlay) {
            overlay.classList.add('active');
            document.body.style.overflow = 'hidden';
        }
    },

    close(id) {
        const overlay = document.getElementById(id);
        if (overlay) {
            overlay.classList.remove('active');
            document.body.style.overflow = '';
        }
    },

    closeAll() {
        document.querySelectorAll('.modal-overlay.active').forEach(m => {
            m.classList.remove('active');
        });
        document.body.style.overflow = '';
    }
};

// Close modal when clicking overlay backdrop
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('modal-overlay')) {
        Modal.closeAll();
    }
});

// Close modal on Escape key
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') Modal.closeAll();
});

// ============================================================
// LIVE CLOCK
// ============================================================
function updateClock() {
    const el = document.getElementById('live-clock');
    if (el) {
        const now = new Date();
        el.textContent = now.toLocaleTimeString('id-ID', {
            hour: '2-digit', minute: '2-digit', second: '2-digit'
        });
    }
}
setInterval(updateClock, 1000);
updateClock();

// ============================================================
// CONFIRM DELETE
// ============================================================
function confirmDelete(formId, itemName = 'item ini') {
    if (confirm(`Apakah Anda yakin ingin menghapus ${itemName}?\nTindakan ini tidak dapat dibatalkan.`)) {
        document.getElementById(formId).submit();
    }
}

// ============================================================
// NUMBER FORMATTER
// ============================================================
const Formatter = {
    currency(amount) {
        return new Intl.NumberFormat('id-ID', {
            style: 'currency',
            currency: 'IDR',
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        }).format(amount);
    },
    number(num) {
        return new Intl.NumberFormat('id-ID').format(num);
    }
};

// ============================================================
// SEARCH FILTER (Client-Side)
// ============================================================
function setupTableSearch(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;

    input.addEventListener('input', function() {
        const query = this.value.toLowerCase().trim();
        const rows = table.querySelectorAll('tbody tr');
        rows.forEach(row => {
            const text = row.textContent.toLowerCase();
            row.style.display = text.includes(query) ? '' : 'none';
        });
    });
}

// ============================================================
// AUTO-DISMISS ALERTS
// ============================================================
document.querySelectorAll('.alert[data-auto-dismiss]').forEach(alert => {
    const delay = parseInt(alert.getAttribute('data-auto-dismiss')) || 5000;
    setTimeout(() => {
        alert.style.opacity = '0';
        alert.style.transition = 'opacity 0.5s';
        setTimeout(() => alert.remove(), 500);
    }, delay);
});

// ============================================================
// FLASH MESSAGES AS TOAST
// ============================================================
document.addEventListener('DOMContentLoaded', function() {
    Toast.init();

    const successFlash = document.querySelector('[data-flash-success]');
    if (successFlash) {
        Toast.success('Berhasil!', successFlash.getAttribute('data-flash-success'));
    }

    const errorFlash = document.querySelector('[data-flash-error]');
    if (errorFlash) {
        Toast.error('Error!', errorFlash.getAttribute('data-flash-error'));
    }

    // Setup mobile sidebar toggle
    const toggleBtn = document.getElementById('sidebar-toggle');
    const sidebar = document.querySelector('.sidebar');
    if (toggleBtn && sidebar) {
        toggleBtn.addEventListener('click', () => {
            sidebar.classList.toggle('mobile-open');
        });
    }
});

// ============================================================
// POPULATE EDIT MODAL
// ============================================================
function populateModal(modalId, data) {
    const modal = document.getElementById(modalId);
    if (!modal) return;
    Object.entries(data).forEach(([key, value]) => {
        const input = modal.querySelector(`[name="${key}"]`);
        if (input) {
            if (input.type === 'checkbox') {
                input.checked = value === true || value === 'true';
            } else {
                input.value = value;
            }
        }
    });
    Modal.open(modalId);
}
