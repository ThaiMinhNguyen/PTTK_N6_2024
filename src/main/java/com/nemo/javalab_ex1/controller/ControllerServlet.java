/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nemo.javalab_ex1.controller;

import com.nemo.javalab_ex1.DAO.AccountDAO;
import com.nemo.javalab_ex1.entity.Account;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class ControllerServlet extends HttpServlet {

    private AccountDAO accountDAO;

    @Override
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        accountDAO = new AccountDAO(jdbcURL, jdbcUsername, jdbcPassword);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String action = request.getServletPath();

            switch (action) {
                case "/login":
                    loginAccount(request, response);
                    break;
                case "/register":
                    showRegister(request, response);
                    break;
                case "/addAccount":
                    registerAccount(request, response);
                    break;
                case "/loginSuccess":
                    showLoginSuccess(request, response);
                    break;
                default:
                    showLogin(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }

    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
        dispatcher.forward(request, response);
    }
    
    private void showLoginSuccess(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("LoginSuccessful.jsp");
        dispatcher.forward(request, response);
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Register.jsp");
        dispatcher.forward(request, response);
    }

    private void registerAccount(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (!accountDAO.accountExists(username)) {
            accountDAO.registerAccount(new Account(username, password));
            response.sendRedirect("loginSuccess"); 
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Username already exists! Please choose a different one.');");
            out.println("location='Register.jsp';");
            out.println("</script>");
        }
    }
    
    private void loginAccount(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (accountDAO.loginAccount(username, password) != null) {
            response.sendRedirect("loginSuccess"); 
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Login failed');");
            out.println("location='Login.jsp';");
            out.println("</script>");
        }
    }
}
