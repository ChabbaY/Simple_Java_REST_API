package com.chabbay.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorldServlet extends HttpServlet {
    private String msg;

    @Override
    public void init() {
        msg = "Hello World!";
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println("<h1>" + msg + "</h1>");
        out.println("<p>" + "Ahoy ahoy!" + "</p>");
    }
}