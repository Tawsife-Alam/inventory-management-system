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

@WebServlet("/SummaryServlet")
public class SummaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // SQL Query with aggregate functions: COUNT, SUM, COALESCE
        String sql = "SELECT COUNT(*) AS total_products, "
                   + "       COALESCE(SUM(quantity), 0) AS total_quantity, "
                   + "       COALESCE(SUM(quantity * price), 0) AS total_value "
                   + "FROM products";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int totalProducts = rs.getInt("total_products");
                int totalQuantity = rs.getInt("total_quantity");
                double totalValue = rs.getDouble("total_value");

                String json = "{"
                            + "\"totalProducts\":" + totalProducts + ","
                            + "\"totalQuantity\":" + totalQuantity + ","
                            + "\"totalValue\":" + totalValue
                            + "}";
                out.print(json);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            out.print("{\"error\":\"Failed to fetch summary: " + e.getMessage() + "\"}");
        }

        out.flush();
    }
}
