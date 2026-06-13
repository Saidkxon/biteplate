const api = { auth: "/api/auth" };

const rolePages = {
    MANAGER: "/manager-dashboard.html",
    WAITER: "/waiter-dashboard.html",
    HEAD_CHEF: "/chef-dashboard.html",
    CASHIER: "/cashier-dashboard.html"
};

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

async function login() {
    const payload = {
        username: value("loginUsername"),
        password: value("loginPassword")
    };

    const user = await request(api.auth + "/login", {
        method: "POST",
        body: JSON.stringify(payload)
    });

    localStorage.setItem("bpUser", JSON.stringify(user));
    notify("Login successful. Opening role dashboard...");
    setTimeout(() => window.location.href = rolePages[user.role] || "/", 700);
}

async function registerStaff() {
    const payload = {
        staffId: value("regStaffId"),
        username: value("regUsername"),
        password: value("regPassword"),
        name: value("regName"),
        role: value("regRole")
    };

    await request(api.auth + "/register", {
        method: "POST",
        body: JSON.stringify(payload)
    });

    notify("Registration successful. Redirecting to login...");
    setTimeout(() => window.location.href = "/login.html", 900);
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
