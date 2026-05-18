<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
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
        .nav-links a {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 88px;
            padding: 10px 16px;
            border-radius: 999px;
            background: #111827;
            color: #ffffff;
            font-weight: 600;
        }
        .nav-links a:hover {
            background: #1f2937;
            text-decoration: none;
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
        .actions {
            white-space: nowrap;
            width: 1%;
        }
        .action-button {
            padding: 8px 12px;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            background: #ffffff;
            color: #1f2933;
            font-size: 13px;
            cursor: pointer;
        }
        .action-button:hover {
            background: #f8fafc;
        }
        .modal-backdrop {
            position: fixed;
            inset: 0;
            display: none;
            align-items: center;
            justify-content: center;
            padding: 20px;
            background: rgba(15, 23, 42, 0.45);
        }
        .modal-backdrop.is-open {
            display: flex;
        }
        .modal {
            width: 100%;
            max-width: 520px;
            padding: 20px;
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 20px 40px rgba(15, 23, 42, 0.18);
        }
        .modal h3 {
            margin: 0 0 16px;
            font-size: 20px;
        }
        .modal-grid {
            display: grid;
            gap: 14px;
        }
        .modal-field label {
            display: block;
            margin-bottom: 6px;
            font-size: 14px;
            font-weight: 600;
        }
        .modal-field input,
        .modal-field textarea {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #bcccdc;
            border-radius: 6px;
            font: inherit;
        }
        .modal-field textarea {
            min-height: 96px;
            resize: vertical;
        }
        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 18px;
        }
        .modal-close {
            background: #f8fafc;
        }
        .modal-save {
            background: #1f5f8b;
            border-color: #1f5f8b;
            color: #ffffff;
        }
        .modal-save:hover {
            background: #17496b;
        }
        #weatherBox h3 {
            margin: 0 0 12px;
            font-size: 18px;
        }
        #weatherText {
            margin: 0;
            color: #52606d;
            line-height: 1.5;
        }
        .weather-city {
            display: block;
            margin-bottom: 6px;
            font-size: 14px;
            color: #52606d;
        }
        .weather-temp {
            display: block;
            font-size: 28px;
            font-weight: 700;
            color: #1f2933;
            line-height: 1.1;
        }
        .weather-desc {
            display: block;
            margin-top: 6px;
            font-size: 14px;
            color: #52606d;
            text-transform: capitalize;
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
            <p>Overview of users, accounts and transactions</p>
        </div>
        <nav class="nav-links">
            <a href="${pageContext.request.contextPath}/MainViewServlet">Home</a>
            <a href="${pageContext.request.contextPath}/about.jsp">About</a>
        </nav>
    </header>

    <section class="toolbar">
        <form action="${pageContext.request.contextPath}/MainViewServlet" method="GET">
            <label for="userDropdown">Select user</label>
            <select name="selectedUserId" id="userDropdown" onchange="this.form.submit()">
                <option value="">-- Select a user --</option>
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
        <p class="toolbar-note">The page updates when you select a user.</p>
    </section>

    <main class="dashboard-grid">
        <section class="panel">
            <h2>Account overview</h2>

            <c:if test="${currentUser != null}">
                <div class="overview-list">
                    <div class="overview-item">
                        <span class="overview-label">User</span>
                        <span class="overview-value">${currentUser.userName}</span>
                    </div>
                    <c:if test="${currentAccount != null}">
                        <div class="overview-item">
                            <span class="overview-label">Account</span>
                            <span class="overview-value">${currentAccount.accountName}</span>
                        </div>
                        <div class="overview-item">
                            <span class="overview-label">Balance</span>
                            <span class="overview-value">${currentAccount.currentBalance} kr</span>
                        </div>
                    </c:if>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${currentUser == null}">
                    <p class="empty-state">Select a user to view account information.</p>
                </c:when>
                <c:when test="${currentAccount == null}">
                    <p class="empty-state">No account was found for the selected user.</p>
                </c:when>
            </c:choose>
        </section>

        <section class="panel">
            <h2>Available categories</h2>

            <table>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                </tr>

                <c:forEach var="cat" items="${categories}">
                    <tr>
                        <td>${cat.categoryName}</td>
                        <td>${cat.categoryType}</td>
                    </tr>
                </c:forEach>
            </table>
        </section>

        <section class="panel panel-wide">
            <h2>Transactions</h2>

            <table>
                <tr>
                    <th>Date</th>
                    <th>Category</th>
                    <th>Amount</th>
                    <th>Repeating</th>
                    <th>Comment</th>
                    <th>Action</th>
                </tr>

                <c:forEach var="transaction" items="${transactions}">
                    <tr>
                        <td>${fn:substring(transaction.transactionDate, 0, 10)}</td>
                        <td>${transaction.category.categoryName}</td>
                        <td class="amount">${transaction.amount}</td>
						<td>
    						<c:choose>
    				   			 <c:when test="${transaction.repeatingTransaction}">
        			 			   Yes
     				   			</c:when>
     				  			<c:otherwise>
    			       				No
     						   	</c:otherwise>
    						</c:choose>
						</td>
						<td>${transaction.note}</td>

                        <td class="actions">
                            <button
                                type="button"
                                class="action-button edit-transaction-button"
                                data-transaction-id="${transaction.transactionId}"
                                data-category-id="${transaction.category.categoryId}"
                                data-category-name="${transaction.category.categoryName}"
                                data-transaction-date="${fn:substring(transaction.transactionDate, 0, 10)}"
                                data-amount="${transaction.amount}"
                                data-note="${transaction.note}">
                                Edit
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty transactions}">
                <p class="empty-state">No transactions were found for the selected account.</p>
            </c:if>
        </section>

        <section class="panel panel-wide">
            <div id="weatherBox">
                <h3>Current Weather in Lund</h3>
                <p id="weatherText">Loading weather...</p>
            </div>
        </section>

    </main>

    <div id="editTransactionModal" class="modal-backdrop" aria-hidden="true">
        <div class="modal">
            <h3>Edit transaction</h3>
            <form>
            <input type="hidden" id="editTransactionId">
                <div class="modal-grid">
                    <div class="modal-field">
                        <label for="editCategoryName">Category</label>
                        <input type="text" id="editCategoryName" readonly>
                    </div>
                    <div class="modal-field">
                        <label for="editTransactionDate">Date</label>
                        <input type="date" id="editTransactionDate">
                    </div>
                    <div class="modal-field">
                        <label for="editAmount">Amount</label>
                        <input type="text" id="editAmount">
                    </div>
                    <div class="modal-field">
                        <label for="editNote">Comment</label>
                        <textarea id="editNote"></textarea>
                    </div>
                </div>
                <div class="modal-actions">
                    <button type="button" class="action-button modal-save">Save</button>
                    <button type="button" class="action-button modal-close" id="closeEditModalButton">Stäng</button>
                </div>
            </form>
        </div>
    </div>

    <footer>
        <p>BranchBuds</p>
    </footer>
    </div>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        $(document).ready(function () {
            const modal = $("#editTransactionModal");
            const categoryNameField = $("#editCategoryName");
            const transactionDateField = $("#editTransactionDate");
            const amountField = $("#editAmount");
            const noteField = $("#editNote");

            $(".edit-transaction-button").on("click", function () {
                const button = $(this);

                $("#editTransactionId").val(button.data("transaction-id"));
                categoryNameField.val(button.data("category-name"));
                transactionDateField.val(button.data("transaction-date"));
                amountField.val(button.data("amount"));
                noteField.val(button.data("note"));

                modal.addClass("is-open").attr("aria-hidden", "false");
            });
            
            $(".modal-save").on("click", function () {
                const transactionId = $("#editTransactionId").val();
                const editButton = $(".edit-transaction-button[data-transaction-id='" + transactionId + "']");

                $.ajax({
                    url: "${pageContext.request.contextPath}/Transactions/" + transactionId,
                    method: "PUT",
                    contentType: "application/json",
                    data: JSON.stringify({
                        categoryId: editButton.data("category-id").toString(),
                        transactionDate: transactionDateField.val() + "T00:00:00",
                        amount: amountField.val(),
                        note: noteField.val()
                    }),
                    success: function () {
                        location.reload();
                    },
                    error: function () {
                        alert("Could not save transaction.");
                    }
                });
            });


            $("#closeEditModalButton").on("click", function () {
                modal.removeClass("is-open").attr("aria-hidden", "true");
            });

            modal.on("click", function (event) {
                if (event.target === this) {
                    modal.removeClass("is-open").attr("aria-hidden", "true");
                }
            });

            const apiKey = "9ec94ba1b7ac698d786c045e4f638cdd";
            const city = "Lund,SE";

            if (!apiKey || apiKey === "DIN_API_KEY_HAR") {
                $("#weatherText").html("Add your OpenWeather API key to load weather.");
                return;
            }

            $.ajax({
                url: "https://api.openweathermap.org/data/2.5/weather",
                method: "GET",
                data: {
                    q: city,
                    appid: apiKey,
                    units: "metric"
                },
                success: function (data) {
                    const temp = Math.round(data.main.temp);
                    const description = data.weather[0].description;

                    $("#weatherText").html(
                        "<span class=\"weather-city\">Lund, Sweden</span>" +
                        "<span class=\"weather-temp\">" + temp + "°C</span>" +
                        "<span class=\"weather-desc\">" + description + "</span>"
                    );
                },
                error: function (xhr) {
                    let message = "Could not load weather";

                    if (xhr.responseJSON && xhr.responseJSON.message) {
                        message += " (" + xhr.status + ": " + xhr.responseJSON.message + ")";
                    } else if (xhr.status) {
                        message += " (" + xhr.status + ")";
                    }

                    $("#weatherText").html(message);
                }
            });
        });
    </script>
</body>

</html>