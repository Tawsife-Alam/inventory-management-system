package com.inventory.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.inventory.util.DBConnection;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // 1. GET: Fetch all products or search by keyword
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        StringBuilder json = new StringBuilder();
        json.append("[");

        String search = request.getParameter("search");
        boolean hasSearch = (search != null && !search.trim().isEmpty());

        String sql = "SELECT p.product_id, p.product_name, c.category_name, s.supplier_name, "
                   + "       p.quantity, p.price, (p.quantity * p.price) AS total_value, "
                   + "       p.category_id, p.supplier_id "
                   + "FROM products p "
                   + "LEFT JOIN categories c ON p.category_id = c.category_id "
                   + "LEFT JOIN suppliers s ON p.supplier_id = s.supplier_id ";

        if (hasSearch) {
            sql += "WHERE p.product_name LIKE ? ";
        }
        sql += "ORDER BY p.product_id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (hasSearch) {
                stmt.setString(1, "%" + search.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    first = false;

                    json.append("{");
                    json.append("\"productId\":").append(rs.getInt("product_id")).append(",");
                    json.append("\"productName\":\"").append(escapeJson(rs.getString("product_name"))).append("\",");
                    json.append("\"categoryName\":\"").append(escapeJson(rs.getString("category_name"))).append("\",");
                    json.append("\"supplierName\":\"").append(escapeJson(rs.getString("supplier_name"))).append("\",");
                    json.append("\"quantity\":").append(rs.getInt("quantity")).append(",");
                    json.append("\"price\":").append(rs.getDouble("price")).append(",");
                    json.append("\"totalValue\":").append(rs.getDouble("total_value")).append(",");
                    json.append("\"categoryId\":").append(rs.getInt("category_id")).append(",");
                    json.append("\"supplierId\":").append(rs.getInt("supplier_id"));
                    json.append("}");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        json.append("]");
        out.print(json.toString());
        out.flush();
    }

    // 2. POST: Handles both ADD and UPDATE
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String productIdStr = request.getParameter("productId");
        String productName = request.getParameter("productName");
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int supplierId = Integer.parseInt(request.getParameter("supplierId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        double price = Double.parseDouble(request.getParameter("price"));

        boolean isUpdate = (productIdStr != null && !productIdStr.trim().isEmpty());

        String sql;
        if (isUpdate) {
            sql = "UPDATE products SET product_name = ?, category_id = ?, supplier_id = ?, quantity = ?, price = ? WHERE product_id = ?";
        } else {
            sql = "INSERT INTO products (product_name, category_id, supplier_id, quantity, price) VALUES (?, ?, ?, ?, ?)";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productName);
            stmt.setInt(2, categoryId);
            stmt.setInt(3, supplierId);
            stmt.setInt(4, quantity);
            stmt.setDouble(5, price);

            if (isUpdate) {
                int productId = Integer.parseInt(productIdStr.trim());
                stmt.setInt(6, productId);
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                String msg = isUpdate ? "Product updated successfully!" : "Product added successfully!";
                out.print("{\"status\":\"success\",\"message\":\"" + msg + "\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Operation failed.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Database error: " + escapeJson(e.getMessage()) + "\"}");
        }

        out.flush();
    }

    // 3. DELETE: Removes a product by ID
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        String productIdStr = request.getParameter("productId");
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            out.print("{\"status\":\"error\",\"message\":\"Product ID is required for deletion.\"}");
            return;
        }

        int productId = Integer.parseInt(productIdStr.trim());
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                out.print("{\"status\":\"success\",\"message\":\"Product deleted successfully!\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Product not found or already deleted.\"}");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"status\":\"error\",\"message\":\"Database error: " + escapeJson(e.getMessage()) + "\"}");
        }

        out.flush();
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
