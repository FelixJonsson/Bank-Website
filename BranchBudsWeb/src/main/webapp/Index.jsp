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
        <a href="${pageContext.request.contextPath}/MainController">Hem</a>
        |
        <a href="#">Om</a>
    </nav>

    <main class="dashboard-grid">

        <section class="card">
            <h2>Kontoöversikt</h2>

            <c:choose>
                <c:when test="${currentAccount != null}">
                    <p><strong>Användare:</strong> ${currentUser.userName}</p>
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
                    <th>Återkommande</th>
                </tr>

                <c:forEach var="transaction" items="${transactions}">
                    <tr>
                        <td>${transaction.transactionDate}</td>
                        <td>${transaction.category.categoryName}</td>
                        <td>${transaction.amount}</td>
                        <td>${transaction.note}</td>
                        <td>${transaction.repeatingTransaction}</td>
                    </tr>
                </c:forEach>
            </table>
        </section>

    </main>

    <footer style="text-align:center; margin-top: 20px;">
        <p>BranchBuds</p>
    </footer>

</body>

</html>