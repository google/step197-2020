package com.google.codeu.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/addPlace")
public class AddPlaceServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Properties props = ServletUtils.GetProperties();
        request.setAttribute("TITLE", "Add Place");
        request.setAttribute(
                "HEAD_HTML", "<link rel='stylesheet' href='/css/main.css'>"
                        + "<link rel='stylesheet' href='/css/user-page.css'>");

        ServletUtils.RenderReact("addPlace", request, response);
    }
}
