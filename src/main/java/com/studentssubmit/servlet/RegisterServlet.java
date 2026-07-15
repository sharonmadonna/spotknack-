package com.studentssubmit.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Read the JSON body sent by the client
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }

        // Parse it into a JSON object
        JSONObject inputJson = new JSONObject(requestBody.toString());
        String username = inputJson.getString("username");
        String password = inputJson.getString("password");

        // Build a JSON response (no DB yet, just echoing back for now)
        JSONObject outputJson = new JSONObject();
        outputJson.put("status", "success");
        outputJson.put("message", "User registered: " + username);

        response.setContentType("application/json");
        response.getWriter().write(outputJson.toString());
    }
}