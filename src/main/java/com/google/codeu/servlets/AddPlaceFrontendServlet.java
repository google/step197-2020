package com.google.codeu.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

@WebServlet("/place/add")
public class AddPlaceFrontendServlet extends HttpServlet{
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Properties props = ServletUtils.GetProperties();
        request.setAttribute("TITLE", "Add Place");
        request.setAttribute(
                "HEAD_HTML", "");

        ServletUtils.RenderReact("addPlace", request, response);
    }
}
