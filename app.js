

let currentProducts = [];

document.addEventListener("DOMContentLoaded", function () {
    loadProducts();
    loadSummary(); // Load live summary statistics

    // 1. Form submission listener (Handles both Add and Update)
    const form = document.getElementById("productForm");
    form.addEventListener("submit", function (e) {
        e.preventDefault();
        saveProduct();
    });

    // 2. Cancel Edit button listener
    const cancelButton = document.getElementById("cancelButton");
    cancelButton.addEventListener("click", function () {
        resetForm();
    });

    // 3. Real-time Search Box listener
    const searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("input", function () {
        loadProducts(this.value.trim());
    });
});

// -------------------------------------------------------
// 1. FETCH & DISPLAY SUMMARY STATISTICS (SQL Aggregates)
// -------------------------------------------------------
function loadSummary() {
    fetch("SummaryServlet")
        .then(response => response.json())
        .then(data => {
            document.getElementById("totalProducts").innerText = data.totalProducts;
            document.getElementById("totalQuantity").innerText = data.totalQuantity;
            
            // Format currency nicely with commas (e.g. $4,250.50)
            document.getElementById("totalValue").innerText = "$" + Number(data.totalValue).toLocaleString(undefined, {
                minimumFractionDigits: 2,
                maximumFractionDigits: 2
            });
        })
        .catch(error => console.error("Error loading summary:", error));
}

// -------------------------------------------------------
// 2. FETCH & DISPLAY PRODUCTS (With Optional Search)
// -------------------------------------------------------
function loadProducts(searchKeyword = "") {
    let url = "ProductServlet";
    if (searchKeyword) {
        url += "?search=" + encodeURIComponent(searchKeyword);
    }

    fetch(url)
        .then(response => response.json())
        .then(products => {
            currentProducts = products;
            const tableBody = document.getElementById("tableBody");
            tableBody.innerHTML = "";

            if (products.length === 0) {
                tableBody.innerHTML = `<tr><td colspan="8" style="text-align: center; color: #64748b;">No products found in inventory.</td></tr>`;
                return;
            }

            products.forEach(p => {
                const tr = document.createElement("tr");

                tr.innerHTML = `
                    <td>${p.productId}</td>
                    <td><strong>${p.productName}</strong></td>
                    <td>${p.categoryName || "N/A"}</td>
                    <td>${p.supplierName || "N/A"}</td>
                    <td>${p.quantity}</td>
                    <td>$${Number(p.price).toFixed(2)}</td>
                    <td>$${Number(p.totalValue).toFixed(2)}</td>
                    <td class="action-buttons">
                        <button class="btn-sm btn-edit" onclick="editProduct(${p.productId})">Edit</button>
                        <button class="btn-sm btn-delete" onclick="deleteProduct(${p.productId})">Delete</button>
                    </td>
                `;

                tableBody.appendChild(tr);
            });
        })
        .catch(error => console.error("Error loading products:", error));
}

// -------------------------------------------------------
// 3. EDIT PRODUCT (Populate form with existing values)
// -------------------------------------------------------
function editProduct(id) {
    const product = currentProducts.find(p => p.productId === id);
    if (!product) return;

    document.getElementById("productId").value = product.productId;
    document.getElementById("productName").value = product.productName;
    document.getElementById("categoryId").value = product.categoryId;
    document.getElementById("supplierId").value = product.supplierId;
    document.getElementById("quantity").value = product.quantity;
    document.getElementById("price").value = product.price;

    document.getElementById("formTitle").innerText = `Edit Product (ID: ${product.productId})`;
    document.getElementById("saveButton").innerText = "Update Product";
    document.getElementById("cancelButton").style.display = "inline-block";

    window.scrollTo({ top: 0, behavior: "smooth" });
}

// -------------------------------------------------------
// 4. RESET FORM (Back to Add Mode)
// -------------------------------------------------------
function resetForm() {
    document.getElementById("productForm").reset();
    document.getElementById("productId").value = "";
    document.getElementById("formTitle").innerText = "Add New Product";
    document.getElementById("saveButton").innerText = "Save Product";
    document.getElementById("cancelButton").style.display = "none";
}

// -------------------------------------------------------
// 5. SAVE PRODUCT (Add New or Update Existing)
// -------------------------------------------------------
function saveProduct() {
    const params = new URLSearchParams();
    const productId = document.getElementById("productId").value;

    if (productId) {
        params.append("productId", productId);
    }
    params.append("productName", document.getElementById("productName").value.trim());
    params.append("categoryId", document.getElementById("categoryId").value);
    params.append("supplierId", document.getElementById("supplierId").value);
    params.append("quantity", document.getElementById("quantity").value);
    params.append("price", document.getElementById("price").value);

    fetch("ProductServlet", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()
    })
    .then(response => response.json())
    .then(result => {
        if (result.status === "success") {
            alert(result.message);
            resetForm();
            loadProducts();
            loadSummary(); // Refresh cards
        } else {
            alert("Error: " + result.message);
        }
    })
    .catch(error => console.error("Error saving product:", error));
}

// -------------------------------------------------------
// 6. DELETE PRODUCT
// -------------------------------------------------------
function deleteProduct(id) {
    if (!confirm(`Are you sure you want to delete product ID ${id}?`)) {
        return;
    }

    fetch(`ProductServlet?productId=${id}`, {
        method: "DELETE"
    })
    .then(response => response.json())
    .then(result => {
        if (result.status === "success") {
            alert(result.message);

            const editingId = document.getElementById("productId").value;
            if (editingId == id) {
                resetForm();
            }

            loadProducts();
            loadSummary(); // Refresh cards
        } else {
            alert("Error: " + result.message);
        }
    })
    .catch(error => console.error("Error deleting product:", error));
}
