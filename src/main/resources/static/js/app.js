const api={dashboard:"/api/dashboard",tables:"/api/tables",menu:"/api/menu",reservations:"/api/reservations",orders:"/api/orders",kitchen:"/api/kitchen",billing:"/api/billing",notifications:"/api/notifications",audit:"/api/audit"};
const rolePages={MANAGER:"/manager-dashboard.html",WAITER:"/waiter-dashboard.html",HEAD_CHEF:"/chef-dashboard.html",CASHIER:"/cashier-dashboard.html"};
let currentUser=null;
document.addEventListener("DOMContentLoaded",()=>{const saved=localStorage.getItem("bpUser");if(!saved){window.location.href="/login.html";return}currentUser=JSON.parse(saved);const required=document.body.dataset.requiredRole;if(required&&currentUser.role!==required&&currentUser.role!=="MANAGER"){notify("This dashboard is not assigned to your role. Redirecting...");setTimeout(()=>window.location.href=rolePages[currentUser.role]||"/login.html",800);return}text("userRole",currentUser.role);text("welcomeText",`Logged in as ${currentUser.name} (${currentUser.role})`);const resTime=el("resTime");if(resTime){const t=new Date(Date.now()+86400000);resTime.value=t.toISOString().slice(0,16)}refreshAll();setInterval(loadDashboard,7000)});
async function req(url,opt={}){const r=await fetch(url,{headers:{"Content-Type":"application/json"},...opt});const d=await r.json().catch(()=>null);if(!r.ok){const m=d?.message||"Request failed";notify(m);throw new Error(m)}return d}
function logout(){localStorage.removeItem("bpUser");window.location.href="/login.html"}
async function refreshAll(){await Promise.allSettled([loadDashboard(),loadTables(),loadMenu(),loadReservations(),loadOrders(),loadKitchen(),loadNotifications(),loadAudit()])}
async function loadDashboard(){if(!el("totalTables"))return;const s=await req(api.dashboard);text("totalTables",s.totalTables);text("reservedTables",s.reservedTables);text("occupiedTables",s.occupiedTables);text("activeOrders",s.activeOrders);text("reservationsCount",s.reservations);text("notificationsCount",s.unreadNotifications);text("totalRevenue",money(s.totalRevenue))}
async function loadTables(){if(!el("tableGrid"))return;const data=await req(api.tables),grid=el("tableGrid");grid.innerHTML="";data.sort((a,b)=>a.tableNumber-b.tableNumber).forEach(t=>{const div=document.createElement("div");div.className=`table-card ${t.status.toLowerCase()}`;div.innerHTML=`<h4>Table ${t.tableNumber}</h4><p>Capacity: ${t.capacity}</p><p>Status: <strong>${t.status}</strong></p><div class="small-actions"><button onclick="seatTable(${t.tableNumber})">Seat</button><button onclick="clearTable(${t.tableNumber})">Clear</button></div>`;grid.appendChild(div)})}
async function seatTable(n){await req(`${api.tables}/${n}/seat`,{method:"POST"});notify(`Table ${n} seated`);refreshAll()}
async function clearTable(n){await req(`${api.tables}/${n}/clear`,{method:"POST"});notify(`Table ${n} cleared`);refreshAll()}
async function loadMenu(){if(!el("menuList"))return;const data=await req(api.menu),list=el("menuList");list.innerHTML="";data.forEach(i=>{const div=document.createElement("div");div.className="item-card";div.innerHTML=`<div><strong>${i.id}</strong><p>${i.name}</p><small>${i.category}</small></div><strong>${money(i.price)}</strong>`;list.appendChild(div)})}
async function createReservation(){
    const payload={
        customerName:val("resCustomer"),
        phone:val("resPhone"),
        tableNumber:num("resTable"),
        reservationTime:val("resTime"),
        staffId:val("resStaff")
    };
    const r=await req(api.reservations,{method:"POST",body:JSON.stringify(payload)});
    notify(`Reservation #${r.id} created and confirmed by staff`);
    refreshAll()
}
async function loadReservations(){
    if(!el("reservationList"))return;
    const data=await req(api.reservations),list=el("reservationList");
    list.innerHTML="";
    data.slice(-10).reverse().forEach(r=>{
        const div=document.createElement("div");
        div.className=`list-card status-${String(r.status).toLowerCase()}`;
        const actions = r.status==="REQUESTED"
            ? `<div class="button-row"><button class="primary-btn" onclick="confirmReservation(${r.id})">Confirm</button><button onclick="rejectReservation(${r.id})">Reject</button></div>`
            : `<button onclick="sendReminder(${r.id})">Send reminder</button>`;
        div.innerHTML=`<strong>#${r.id} ${r.customerName}</strong><p>Table ${r.tableNumber} | <span class="status-pill">${r.status}</span></p><small>${r.reservationTime}</small><p>${r.customerMessage||""}</p>${actions}`;
        list.appendChild(div)
    })
}
async function confirmReservation(id){
    const staffId=currentUser?.staffId || val("resStaff") || "W001";
    const r=await req(`${api.reservations}/${id}/confirm`,{method:"POST",body:JSON.stringify({staffId})});
    notify(`Reservation #${r.reservationId} confirmed. Customer can now see confirmation.`);
    refreshAll()
}
async function rejectReservation(id){
    const staffId=currentUser?.staffId || val("resStaff") || "W001";
    const r=await req(`${api.reservations}/${id}/reject`,{method:"POST",body:JSON.stringify({staffId})});
    notify(`Reservation #${r.reservationId} rejected`);
    refreshAll()
}
async function sendReminder(id){const r=await req(`${api.reservations}/${id}/reminder`,{method:"POST"});notify(r.message);refreshAll()}
async function createOrder(){const payload={tableNumber:num("orderTable"),staffId:val("staffId"),items:[{menuItemId:val("item1"),quantity:num("qty1"),addOns:val("addon1"),substitutions:val("sub1"),allergenNotes:val("allergy1")},{menuItemId:val("item2"),quantity:num("qty2"),addOns:"",substitutions:"",allergenNotes:""}].filter(x=>x.menuItemId&&x.quantity>0)};const o=await req(api.orders,{method:"POST",body:JSON.stringify(payload)});setInput("billOrderId",o.orderId);setInput("modifyOrderId",o.orderId);setInput("priorityOrderId",o.orderId);notify(`Order ${o.orderId} created`);refreshAll()}
async function addItemToOrder(){const id=val("modifyOrderId");const payload={menuItemId:val("modifyItem"),quantity:num("modifyQty"),addOns:"",substitutions:"",allergenNotes:""};await req(`${api.orders}/${id}/items`,{method:"POST",body:JSON.stringify(payload)});notify("Item added before preparation");refreshAll()}
async function removeLastItem(){const id=val("modifyOrderId");await req(`${api.orders}/${id}/items/last`,{method:"DELETE"});notify("Last item removed");refreshAll()}
async function loadOrders(){if(!el("ordersTable"))return;const data=await req(api.orders),tb=el("ordersTable");tb.innerHTML="";data.forEach(o=>{const tr=document.createElement("tr");tr.innerHTML=`<td>${o.orderId}</td><td>${o.tableNumber}</td><td>${o.status}</td><td>${money(o.subtotal)}</td>`;tb.appendChild(tr)})}
async function loadKitchen(){if(!el("queueList"))return;const q=await req(`${api.kitchen}/queue`),list=el("queueList");list.innerHTML="";if(q.length===0){list.innerHTML=`<div class="list-card">Kitchen queue is empty.</div>`;return}q.forEach(x=>{const div=document.createElement("div");div.className="list-card";div.textContent=x;list.appendChild(div)})}
async function processKitchen(){const r=await req(`${api.kitchen}/process-next`,{method:"POST"});notify(r.message);refreshAll()}
async function undoKitchen(){const r=await req(`${api.kitchen}/undo`,{method:"POST"});notify(r.message);refreshAll()}
async function reprioritiseOrder(){const r=await req(`${api.kitchen}/reprioritise`,{method:"POST",body:JSON.stringify({orderId:val("priorityOrderId")})});notify(r.message);refreshAll()}
async function cancelOrder(){const id=val("priorityOrderId");const r=await req(`${api.kitchen}/cancel/${id}`,{method:"POST"});notify(r.message);refreshAll()}
async function generateBill(){const id=val("billOrderId");const payload={pricingType:val("pricingType"),tipAmount:Number(val("tipAmount")),splitCount:num("splitCount")};const b=await req(`${api.billing}/${id}`,{method:"POST",body:JSON.stringify(payload)});if(el("billResult"))el("billResult").innerHTML=`<strong>Receipt ${b.orderId}</strong><br>Table: ${b.tableNumber}<br>Strategy: ${b.pricingStrategy}<br>Subtotal: ${money(b.subtotal)}<br>After discount: ${money(b.discountedTotal)}<br>Tax: ${money(b.taxAmount)}<br>Tip: ${money(b.tipAmount)}<br>Split: ${b.splitCount} guests = ${money(b.splitAmount)} each<br><strong>Total: ${money(b.finalTotal)}</strong>`;notify("Bill generated");refreshAll()}
async function loadNotifications(){if(!el("notificationList"))return;const data=await req(api.notifications),list=el("notificationList");list.innerHTML="";data.forEach(n=>{const div=document.createElement("div");div.className="list-card";div.innerHTML=`<strong>${n.title}</strong><p>${n.message}</p><small>${n.recipientRole} | ${n.createdAt}</small>`;list.appendChild(div)})}
async function loadAudit(){if(!el("auditList"))return;const data=await req(api.audit),list=el("auditList");list.innerHTML="";data.slice(0,25).forEach(a=>{const div=document.createElement("div");div.className="list-card";div.innerHTML=`<strong>${a.actionType}</strong><p>${a.details}</p><small>${a.referenceId} | ${a.createdAt}</small>`;list.appendChild(div)})}
function el(id){return document.getElementById(id)}function val(id){return el(id)?.value?.trim()||""}function num(id){return Number(val(id))}function text(id,v){if(el(id))el(id).textContent=v}function setInput(id,v){if(el(id))el(id).value=v}function money(v){return `$${Number(v||0).toFixed(2)}`}
function notify(m){const list=el("toastList");if(!list)return;const t=document.createElement("div");t.className="toast";t.textContent=m;list.prepend(t);setTimeout(()=>{t.style.opacity=0;t.style.transform="translateX(16px)";setTimeout(()=>t.remove(),300)},4500)}
