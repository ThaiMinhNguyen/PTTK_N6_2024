<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Books Store Application</title>
</head>
<body>
    <center>
        <h1>Books Management</h1>
    </center>

    <div align="center">
      
        <form action="${pageContext.request.contextPath}/search" method="GET">
            <input type="text" name="searchQuery" placeholder="Search by title" value="${param.searchQuery}" />
            <button type="submit">Search</button>
        </form>

        <br />

       
        <table border="1" cellpadding="5">
            <caption><h2>List of Books</h2></caption>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Author</th>
                <th>Price</th>
                <th>Select</th>
            </tr>
            <c:forEach var="book" items="${listBook}">
                <tr>
                    <td><c:out value="${book.id}" /></td>
                    <td><c:out value="${book.title}" /></td>
                    <td><c:out value="${book.author}" /></td>
                    <td><c:out value="${book.price}" /></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/addBookToOrder" method="POST">
                            <input type="hidden" name="bookId" value="${book.id}" />
                            <button type="submit">Add to Order</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>

    <br />

    <div align="center">
        <h3>Selected Books for Order:</h3>
        <c:if test="${not empty selectedBooks}">
            <ul>
                <c:forEach var="book" items="${selectedBooks}">
                    <li><c:out value="${book.title}" /> by <c:out value="${book.author}" /></li>
                </c:forEach>
            </ul>
        </c:if>
    </div>

    <br />

    <div align="center">
        <form action="${pageContext.request.contextPath}/createOrder" method="POST">
            <button type="submit">Create Order</button>
        </form>
    </div>
            
    <br/>
    
     <div align="center">
        <form action="${pageContext.request.contextPath}/logout" method="POST">
            <button type="submit">Log out</button>
        </form>
    </div>
</body>
</html>
