package com.nemo.javalab_ex3.controller;

import com.nemo.javalab_ex3.DAO.AccountDAO;
import com.nemo.javalab_ex3.DAO.BookDAO;
import com.nemo.javalab_ex3.DAO.OrderDAO;
import com.nemo.javalab_ex3.entity.Account;
import com.nemo.javalab_ex3.entity.Book;
import com.nemo.javalab_ex3.entity.Customer;
import com.nemo.javalab_ex3.entity.Order;
import java.io.IOException;
import java.sql.SQLException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ControllerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BookDAO bookDAO;
    private AccountDAO accountDAO;
    private OrderDAO orderDAO;

    @Override
    public void init() {
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        bookDAO = new BookDAO(jdbcURL, jdbcUsername, jdbcPassword);
        accountDAO = new AccountDAO(jdbcURL, jdbcUsername, jdbcPassword);
        orderDAO = new OrderDAO(jdbcURL, jdbcUsername, jdbcPassword); 

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();

        try {
            switch (action) {
                case "/register":
                    showRegister(request, response);
                    break;
                case "/login":
                    loginAccount(request, response);
                    break;
                case "/logout":
                    logout(request, response);
                    break;
                case "/list":
                    showList(request, response);
                    break;
                case "/addBookToOrder": 
                    addBookToOrder(request, response);
                    break;
                case "/search": 
                    searchBooks(request, response);
                    break;
                case "/createOrder": 
                    createOrder(request, response);
                    break;
                default:
                    showLogin(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void searchBooks(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String searchQuery = request.getParameter("searchQuery");
        List<Book> listBook;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            listBook = bookDAO.searchBooksByTitle(searchQuery);
        } else {
            listBook = new ArrayList<>();  
        }

        request.setAttribute("listBook", listBook);
        request.setAttribute("searchQuery", searchQuery); 

        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void showLogin(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Login.jsp");
        dispatcher.forward(request, response);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("BookList.jsp");
        dispatcher.forward(request, response);
    }

    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("Register.jsp");
        dispatcher.forward(request, response);
    }

    private void loginAccount(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("loggedInAccount") != null) {
            response.sendRedirect("list");
            return;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Account account = accountDAO.loginAccount(username, password);

        if (account != null) {
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute("loggedInAccount", account);
            response.sendRedirect("list");
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Login failed');");
            out.println("location='Login.jsp';");
            out.println("</script>");
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect("Login.jsp");
    }

    private void addBookToOrder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession(false);
        List<Book> selectedBooks = (List<Book>) session.getAttribute("selectedBooks");
        if (selectedBooks == null) {
            selectedBooks = new ArrayList<>();
        }
        int bookId = Integer.parseInt(request.getParameter("bookId"));
        Book selectedBook = bookDAO.getBookById(bookId);
        selectedBooks.add(selectedBook);
        session.setAttribute("selectedBooks", selectedBooks);
        response.sendRedirect("list");
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession(false);
        Account account = (Account) session.getAttribute("loggedInAccount");
        Customer customer = account != null ? account.getCustomer() : null;
        List<Book> selectedBooks = (List<Book>) session.getAttribute("selectedBooks");

        if (customer != null && selectedBooks != null && !selectedBooks.isEmpty()) {
            Order order = new Order();
            order.setOrderDate(new Date());
            order.setCustomer(customer);

            boolean orderCreated = orderDAO.createOrder(order, selectedBooks);

            if (orderCreated) {
                session.removeAttribute("selectedBooks");
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Order created');");
                out.println("location='BookList.jsp';");
                out.println("</script>");
            } else {
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                out.println("<script type=\"text/javascript\">");
                out.println("alert('Create Order failed');");
                out.println("location='BookList.jsp';");
                out.println("</script>");
            }
        } else {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("alert('No Book have been selected');");
            out.println("location='BookList.jsp';");
            out.println("</script>");
        }
    }

}
