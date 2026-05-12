<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>BranchBuds Dashboard</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f3f5f7;
            color: #1f2933;
        }
        a {
            color: #1f5f8b;
            text-decoration: none;
        }
        a:hover {
            text-decoration: underline;
        }
        .page {
            max-width: 1120px;
            margin: 0 auto;
            padding: 24px 20px 40px;
        }
        .topbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
            margin-bottom: 24px;
            padding-bottom: 16px;
            border-bottom: 1px solid #d8dee4;
        }
        .brand h1 {
            margin: 0 0 4px;
            font-size: 28px;
        }
        .brand p {
            margin: 0;
            color: #52606d;
            font-size: 14px;
        }
        .nav-links {
            display: flex;
            gap: 14px;
            font-size: 14px;
        }
        .toolbar {
            display: flex;
            justify-content: space-between;
            align-items: end;
            gap: 16px;
            margin-bottom: 20px;
            padding: 16px 18px;
            background: #ffffff;
            border: 1px solid #d8dee4;
            border-radius: 8px;
        }
        .toolbar label {
            display: block;
            margin-bottom: 8px;
            font-size: 14px;
            font-weight: 600;
        }
        .toolbar select {
            min-width: 240px;
            padding: 10px 12px;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            background: #fff;
            font-size: 14px;
        }
        .toolbar-note {
            margin: 0;
            font-size: 14px;
            color: #52606d;
        }
        .dashboard-grid {
            display: grid;
            grid-template-columns: minmax(0, 1fr) minmax(0, 1fr);
            gap: 20px;
        }
        .panel {
            background: #ffffff;
            border: 1px solid #d8dee4;
            border-radius: 8px;
            padding: 18px;
        }
        .panel-wide {
            grid-column: 1 / -1;
        }
        .panel h2 {
            margin: 0 0 16px;
            font-size: 18px;
        }
        .overview-list {
            display: grid;
            grid-template-columns: 1fr;
            gap: 12px;
        }
        .overview-item {
            padding: 12px 14px;
            background: #f8fafc;
            border: 1px solid #e4e7eb;
            border-radius: 6px;
        }
        .overview-label {
            display: block;
            margin-bottom: 4px;
            font-size: 12px;
            font-weight: 700;
            color: #52606d;
            text-transform: uppercase;
        }
        .overview-value {
            font-size: 16px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            text-align: left;
            padding: 10px 8px;
            border-bottom: 1px solid #e4e7eb;
            vertical-align: top;
        }
        th {
            font-size: 13px;
            color: #52606d;
            font-weight: 700;
        }
        .empty-state {
            margin: 0;
            color: #52606d;
        }
        .amount {
            white-space: nowrap;
            font-weight: 600;
        }
        footer {
            margin-top: 24px;
            text-align: center;
            color: #7b8794;
            font-size: 13px;
        }
        @media (max-width: 800px) {
            .topbar,
            .toolbar {
                flex-direction: column;
                align-items: stretch;
            }
            .dashboard-grid {
                grid-template-columns: 1fr;
            }
            .panel-wide {
                grid-column: auto;
            }
            .toolbar select {
                width: 100%;
                min-width: 0;
            }
        }
    </style>
</head>
<body>
    <div class="page">
    <header class="topbar">
        <div class="brand">
            <h1>BranchBuds</h1>
            <p>Översikt över användare, konton och transaktioner</p>
        </div>
   		<nav class="nav-links">
            <a href="${pageContext.request.contextPath}/MainViewServlet">Hem</a>
            <a href="#">Om</a>
        </nav>
    </header>

    <section class="toolbar">
        <form action="${pageContext.request.contextPath}/MainViewServlet" method="GET">
            <label for="userDropdown">Välj användare</label>
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
        <p class="toolbar-note">När du väljer en användare uppdateras sidan direkt.</p>
    </section>

    <main class="dashboard-grid">
        <section class="panel">
            <h2>Kontoöversikt</h2>

            <c:if test="${currentUser != null}">
                <div class="overview-list">
                    <div class="overview-item">
                        <span class="overview-label">Användare</span>
                        <span class="overview-value">${currentUser.userName}</span>
                    </div>
                    <c:if test="${currentAccount != null}">
                        <div class="overview-item">
                            <span class="overview-label">Konto</span>
                            <span class="overview-value">${currentAccount.accountName}</span>
                        </div>
                        <div class="overview-item">
                            <span class="overview-label">Saldo</span>
                            <span class="overview-value">${currentAccount.currentBalance} kr</span>
                        </div>
                    </c:if>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${currentUser == null}">
                    <p class="empty-state">Välj en användare för att visa kontoinformation.</p>
                </c:when>
                <c:when test="${currentAccount == null}">
                    <p class="empty-state">Inget konto hittades för aktuell användare.</p>
                </c:when>
            </c:choose>
        </section>

			<section class="panel">
			    <h2>Redigera transaktion</h2>
			
			    <form action="${pageContext.request.contextPath}/EditTransactionServlet" method="POST">
			        
			        <input type="hidden" name="transactionId" value="${transactionToEdit.id}">
			
			        <label for="desc">Beskrivning:</label>
			        <input type="text" id="desc" name="description" value="${transactionToEdit.description}" required>
			        <br><br>
			
			        <label for="amount">Belopp:</label>
			        <input type="number" id="amount" name="amount" value="${transactionToEdit.amount}" step="0.01" required>
			        <br><br>
			
			        <label for="category">Kategori:</label>
			        <select id="category" name="categoryId" required>
			            <option value="">-- Välj en kategori --</option>
			            <c:forEach var="cat" items="${categories}">
			                <option value="${cat.categoryId}" ${cat.categoryId == transactionToEdit.category.categoryId ? 'selected' : ''}>
			                    ${cat.categoryName} (${cat.categoryType})
			                </option>
			            </c:forEach>
			        </select>
			        <br><br>
			
			        <button type="submit">Spara ändringar</button>
			    </form>
			</section>

        <section class="panel panel-wide">
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
                        <td class="amount">${transaction.amount}</td>
                        <td>${transaction.note}</td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty transactions}">
                <p class="empty-state">Inga transaktioner hittades för valt konto.</p>
            </c:if>
        </section>

    </main>

    <footer>
        <p></p>
    </footer>
    </div>
</body>

</html>
