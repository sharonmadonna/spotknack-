package com.studentssubmit.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "asm3009"; // use YOUR actual password here

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

        String selectSql = "SELECT password FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement(selectSql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(password)) {
                    outputJson.put("status", "success");
                    outputJson.put("message", "Login successful for " + username);
                } else {
                    outputJson.put("status", "failure");
                    outputJson.put("message", "Invalid username or password");
                }
            } else {
                outputJson.put("status", "failure");
                outputJson.put("message", "Invalid username or password");
            }

        } catch (SQLException e) {
            outputJson.put("status", "failure");
            outputJson.put("message", "Login failed: " + e.getMessage());
        }

        response.setContentType("application/json");
        response.getWriter().write(outputJson.toString());
    }
}