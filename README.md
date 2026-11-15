# Simple Inventory Management System (DBMS Lab Project)

A beginner-friendly, single-page Inventory Management System built with Java Servlets, JDBC, MySQL, HTML5, CSS3, and JavaScript Fetch API.

---

## 📁 Project File Structure

```text
c:\Inventory Management System Project\
├── index.html                  # Frontend single-page UI layout
├── style.css                   # Custom responsive CSS styling
├── app.js                      # Asynchronous Fetch API controller (CRUD & Live Stats)
├── database_setup.sql          # Complete MySQL schema, tables, FKs & sample data
├── PROJECT_REPORT.md           # 2–3 page university lab project report & viva preparation
└── src/
    └── com/
        └── inventory/
            ├── util/
            │   └── DBConnection.java     # JDBC connection manager
            └── servlet/
                ├── ProductServlet.java   # Handles GET (all/search), POST (add/update), DELETE
                └── SummaryServlet.java   # Handles GET (SQL aggregate functions)
```

---

## 🚀 How to Run the Project

1. **Database Setup:**
   - Open **MySQL Workbench**.
   - Open and execute `database_setup.sql` using the ⚡ (Lightning Bolt) button.

2. **Backend & Server Setup:**
   - Open **Eclipse IDE for Enterprise Java**.
   - Start **Tomcat v10.1 Server**.
   - Ensure `mysql-connector-j-8.4.0.jar` is inside `src/main/webapp/WEB-INF/lib`.

3. **Launch the Web Application:**
   - Open any web browser and visit:
     👉 `http://localhost:8080/InventorySystem1/`

---

## 📸 Recommended Screenshots for Your Submission

Capture these 6 clean screenshots to include in your printed or PDF report:
1. **Full Dashboard:** Showing summary cards (Total Products, Total Stock, Total Value) and table with all 10 sample items.
2. **Add Product:** Form filled with a new item and the *"Product added successfully!"* popup.
3. **Live Search Filter:** Showing the table filtered when typing a keyword (e.g. `mouse` or `keyboard`).
4. **Edit Product Mode:** Form loaded in edit mode with yellow *"Update Product"* and gray *"Cancel"* buttons.
5. **Delete Confirmation Dialog:** Browser alert asking *"Are you sure you want to delete product ID X?"*.
6. **MySQL Workbench Verification:** Query tab showing `SELECT * FROM products;` with all database records.

---

## 🗺️ How to Generate an ER Diagram in MySQL Workbench

You can automatically generate an official ER Diagram visual:
1. In MySQL Workbench, click **Database** $\rightarrow$ **Reverse Engineer...** (or press `Ctrl + R`).
2. Click **Next** $\rightarrow$ **Next** (enter your root password if prompted).
3. Select `inventory_management` and click **Next** $\rightarrow$ **Execute** $\rightarrow$ **Next** $\rightarrow$ **Finish**.
4. An interactive **EER Diagram** will open showing `categories`, `suppliers`, and `products` with connector lines for Foreign Keys!
5. Click **File** $\rightarrow$ **Export** $\rightarrow$ **Export as PNG / PDF** to save the diagram image for your report!
