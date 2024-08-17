<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Form</title>
</head>
<body>
 <div align="center">
  <h1>User Login Form</h1>
  <form action="login" method="post">
   <table style="with: 100%">
    <tr>
     <td>UserName</td>
     <td><input type="text" name="username" /></td>
    </tr>
    <tr>
     <td>Password</td>
     <td><input type="password" name="password" /></td>
    </tr>
   </table>
   <input type="submit" value="LOGIN" />
  </form>
  <a href = "${pageContext.request.contextPath}/register">REGISTER</a>
 </div>
</body>
</html>