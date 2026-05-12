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

    <header>
        <h1 style="text-align:center;">BranchBuds</h1>
    </header>

    <nav style="text-align:center; margin-bottom: 20px;">
        <a href="${pageContext.request.contextPath}/MainViewServlet">Hem</a>
        |
        <a href="#">Om</a>
    </nav>

    <main class="dashboard-grid">
		    <form action="${pageContext.request.contextPath}/MainViewServlet" method="GET">
		    <label for="userDropdown">Välj användare:</label>
		    
		    <select name="selectedUserId" id="userDropdown" onchange="this.form.submit()">
		        <option value="">-- Välj en användare --</option>
		        
		        <c:forEach items="${userList}" var="user">
		            <c:choose>
		                <c:when test="${selectedUserId == user.userId}">
		                    <option value="${user.userId}" selected>${user.userName}</option>
		                </c:when>
		                <c:otherwise>
		                    <option value="${user.userId}">${user.userName}</option>
		                </c:otherwise>
		            </c:choose>
		        </c:forEach>
		        
		    </select>
		</form>

        <section class="card">
            <h2>Kontoöversikt</h2>

            <c:if test="${currentUser != null}">
                <p><strong>Användare:</strong> ${currentUser.userName}</p>
            </c:if>

            <c:choose>
                <c:when test="${currentAccount != null}">
                    <p><strong>Konto:</strong> ${currentAccount.accountName}</p>
                    <p><strong>Saldo:</strong> ${currentAccount.currentBalance} kr</p>
                </c:when>
                <c:otherwise>
                    <p>Inget konto hittades för aktuell användare.</p>
                </c:otherwise>
            </c:choose>
        </section>

        <section class="card">
            <h2>Tillgängliga kategorier</h2>

            <table>
                <tr>
                    <th>Namn</th>
                    <th>Typ</th>
                </tr>

                <c:forEach var="cat" items="${categories}">
                    <tr>
                        <td>${cat.categoryName}</td>
                        <td>${cat.categoryType}</td>
                    </tr>
                </c:forEach>
            </table>
        </section>

        <section class="card" style="grid-column: 1 / -1;">
            <h2>Transaktioner</h2>

            <table>
                <tr>
                    <th>Datum</th>
                    <th>Kategori</th>
                    <th>Belopp</th>
                    <th>Kommentar</th>
                </tr>

                <c:forEach var="transaction" items="${transactions}">
                    <tr>
                        <td>${transaction.transactionDate}</td>
                        <td>${transaction.category.categoryName}</td>
                        <td>${transaction.amount}</td>
                        <td>${transaction.note}</td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty transactions}">
                <p>Inga transaktioner hittades för valt konto.</p>
            </c:if>
        </section>

    </main>

    <footer style="text-align:center; margin-top: 20px;">
        <p>BranchBuds</p>
    </footer>

</body>

</html>
