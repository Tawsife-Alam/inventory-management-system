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


