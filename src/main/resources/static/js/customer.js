const api = { reservations: "/api/reservations" };

document.addEventListener("DOMContentLoaded", () => {
    const t = new Date(Date.now() + 86400000);
    const input = document.getElementById("customerResTime");
    if (input) input.value = t.toISOString().slice(0, 16);
});

async function request(url, options = {}) {
    const response = await fetch(url, {
        headers: { "Content-Type": "application/json" },
        ...options
    });

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        const message = data?.message || "Request failed";
        notify(message);
        throw new Error(message);
    }

    return data;
}

async function submitCustomerReservation() {
    const payload = {
        customerName: value("customerName"),
        phone: value("customerPhone"),
        tableNumber: Number(value("customerTable")),
        reservationTime: value("customerResTime")
    };

    const result = await request(api.reservations + "/customer-request", {
        method: "POST",
        body: JSON.stringify(payload)
    });

    document.getElementById("customerResult").innerHTML = `
        <strong>Reservation request #${result.reservationId}</strong><br>
        Status: <span class="status-pill">${result.status}</span><br>
        ${result.customerMessage}<br>
        <small>Use your phone number to check confirmation status.</small>
    `;
    notify("Reservation request sent to waiters.");
}

async function checkCustomerStatus() {
    const phone = encodeURIComponent(value("statusPhone"));
    const results = await request(api.reservations + "/customer-status?phone=" + phone);
    const list = document.getElementById("customerStatusList");
    list.innerHTML = "";

    if (results.length === 0) {
        list.innerHTML = `<div class="list-card">No reservation found for this phone number.</div>`;
        return;
    }

    results.forEach(result => {
        const div = document.createElement("div");
        div.className = `list-card status-${String(result.status).toLowerCase()}`;
        div.innerHTML = `
            <strong>Reservation #${result.reservationId}</strong>
            <p>Table ${result.tableNumber} | <span class="status-pill">${result.status}</span></p>
            <small>${result.reservationTime}</small>
            <p>${result.customerMessage}</p>
        `;
        list.appendChild(div);
    });
}

function value(id) {
    return document.getElementById(id).value.trim();
}

function notify(message) {
    const list = document.getElementById("toastList");
    const toast = document.createElement("div");
    toast.className = "toast";
    toast.textContent = message;
    list.prepend(toast);

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateX(16px)";
        setTimeout(() => toast.remove(), 300);
    }, 4500);
}
