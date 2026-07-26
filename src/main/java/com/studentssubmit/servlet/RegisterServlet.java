package com.studentssubmit.servlet;



import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "asm3009"; 

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        JSONObject inputJson = new JSONObject(requestBody.toString());
        String username = inputJson.getString("username");
        String password = inputJson.getString("password");

        JSONObject outputJson = new JSONObject();
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            outputJson.put("status", "failure");
            outputJson.put("message", "Driver not found: " + e.getMessage());
            response.setContentType("application/json");
            response.getWriter().write(outputJson.toString());
            return;
        }
        String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(insertSql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();

            outputJson.put("status", "success");
            outputJson.put("message", "User registered: " + username);

        } catch (SQLException e) {
            
            outputJson.put("status", "failure");
            outputJson.put("message", "Registration failed: " + e.getMessage());
        }

        response.setContentType("application/json");
        response.getWriter().write(outputJson.toString());
    }
}