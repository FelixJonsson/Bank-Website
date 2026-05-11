<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>BranchBuds Dashboard</title>
    <style>
        .dashboard-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; padding: 20px; }
        .card { border: 1px solid #ddd; padding: 15px; border-radius: 5px; background: white; }
        table { width: 100%; border-collapse: collapse; }
        th, td { text-align: left; padding: 8px; border-bottom: 1px solid #eee; }
    </style>
</head>
<body style="background-color: #f4f7f6;">

    <h1 style="text-align:center;">BranchBuds Systemöversikt</h1>

    <div class="dashboard-grid">
        <div class="card">
            <h2>Registrerade Användare</h2>
            <table>
                <tr><th>Namn</th><th>E-post</th></tr>
                <c:forEach var="u" items="${userList}">
                    <tr><td>${u.userName}</td><td>${u.email}</td></tr>
                </c:forEach>
            </table>
        </div>

        <div class="card">
            <h2>Tillgängliga Kategorier</h2>
            <table>
                <tr><th>Namn</th><th>Typ</th></tr>
                <c:forEach var="cat" items="${categoryList}">
                    <tr><td>${cat.categoryName}</td><td>${cat.categoryType}</td></tr>
                </c:forEach>
            </table>
        </div>
    </div>

</body>
</html>