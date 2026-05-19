<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>BranchBuds Dashboard</title>
<style>
* {
	box-sizing: border-box;
}

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
	grid-column: 1/-1;
}

.panel h2 {
	margin: 0 0 16px;
	font-size: 18px;
}

.section-heading {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 16px;
    margin-bottom: 16px;
}

.section-heading h2 {
    margin: 0;
}


.section-heading {
	display: flex;
	justify-content: space-between;
	align-items: center;
	gap: 16px;
	margin-bottom: 16px;
}

.section-heading h2 {
	margin: 0;
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

.inline-form {
	display: inline;
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

.modal-field input, .modal-field textarea {
	width: 100%;
	padding: 10px 12px;
	border: 1px solid #bcccdc;
	border-radius: 6px;
	font: inherit;
}

.modal-field select {
	width: 100%;
	padding: 10px 12px;
	border: 1px solid #bcccdc;
	border-radius: 6px;
	background: #ffffff;
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

.summary-grid {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 16px;
}

.summary-box {
	padding: 20px;
	background: #f8fafc;
	border: 1px solid #e4e7eb;
	border-radius: 8px;
	text-align: center;
}

.summary-box .overview-label {
	font-size: 13px;
	color: #52606d;
}

.summary-box .overview-value {
	display: block;
	font-size: 28px;
	font-weight: 700;
	color: #1f5f8b; 
	margin-top: 8px;
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

.status-message {
	margin: 0 0 16px;
	padding: 12px 14px;
	border-radius: 6px;
	font-size: 14px;
}

.status-message.success {
	background: #ecfdf3;
	border: 1px solid #a7f3d0;
	color: #166534;
}

.status-message.error {
	background: #fef2f2;
	border: 1px solid #fecaca;
	color: #991b1b;
}

.status-message.delete {
	background: #fef2f2;
	border: 1px solid #fecaca;
	color: #991b1b;
}

@media ( max-width : 800px) {
	.topbar, .toolbar {
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
				<p>Personal financial overview</p>
			</div>
			<nav class="nav-links">
				<a href="${pageContext.request.contextPath}/MainViewServlet">Home</a>
				<a href="${pageContext.request.contextPath}/about.jsp">About</a>
			</nav>
		</header>

		<section class="toolbar">
			<div>
				<span class="overview-label">Demo user</span> <strong>${currentUser.userName}</strong>
			</div>
		</section>


		<main class="dashboard-grid">
			<section class="panel">
				<h2>Account overview</h2>

				<c:if test="${currentUser != null}">
					<div class="overview-list">
						<div class="overview-item">
							<span class="overview-label">User</span> <span
								class="overview-value">${currentUser.userName}</span>
						</div>
						<c:if test="${currentAccount != null}">
							<div class="overview-item">
								<span class="overview-label">Account</span> <span
									class="overview-value">${currentAccount.accountName}</span>
							</div>
							<div class="overview-item">
								<span class="overview-label">Balance</span> <span
									class="overview-value">${currentAccount.currentBalance}
									kr</span>
							</div>
						</c:if>
					</div>
				</c:if>

				<c:choose>
					<c:when test="${currentUser == null}">
						<p class="empty-state">Select a user to view account
							information.</p>
					</c:when>
					<c:when test="${currentAccount == null}">
						<p class="empty-state">No account was found for the selected
							user.</p>
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
				<div class="section-heading">
					<h2>Transactions</h2>
					<button type="button" class="action-button"
						id="openAddTransactionButton">Add transaction</button>
				</div>

				<c:if test="${status == 'added'}">
					<p class="status-message success">Transaction added.</p>
				</c:if>
				<c:if test="${status == 'addError'}">
					<p class="status-message error">Could not add transaction.</p>
				</c:if>
				<c:if test="${status == 'updated'}">
					<p class="status-message success">Transaction updated.</p>
				</c:if>
				<c:if test="${status == 'updateError'}">
					<p class="status-message error">Could not update transaction.</p>
				</c:if>
				<c:if test="${status == 'deleted'}">
					<p class="status-message delete">Transaction deleted.</p>
				</c:if>
				<c:if test="${status == 'deleteError'}">
					<p class="status-message error">Could not delete transaction.</p>
				</c:if>

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
							<td><c:choose>
									<c:when test="${transaction.repeatingTransaction}">
        			 			   Yes
     				   			</c:when>
									<c:otherwise>
    			       				No
     						   	</c:otherwise>
								</c:choose></td>
							<td>${transaction.note}</td>

							<td class="actions">
								<button type="button"
									class="action-button edit-transaction-button"
									data-transaction-id="${transaction.transactionId}"
									data-category-id="${transaction.category.categoryId}"
									data-category-name="${transaction.category.categoryName}"
									data-transaction-date="${fn:substring(transaction.transactionDate, 0, 10)}"
									data-amount="${transaction.amount}"
									data-note="${transaction.note}"
									data-repeating-transaction="${transaction.repeatingTransaction}">Edit</button>
								<form class="inline-form"
									action="${pageContext.request.contextPath}/MainViewServlet"
									method="post"
									onsubmit="return confirm('Delete this transaction?');">
									<input type="hidden" name="action" value="deleteTransaction">
									<input type="hidden" name="transactionId"
										value="${transaction.transactionId}">
									<button type="submit" class="action-button">Delete</button>
								</form>
									
							</td>
						</tr>
					</c:forEach>
				</table>

				<c:if test="${empty transactions}">
					<p class="empty-state">No transactions were found for the
						selected account.</p>
				</c:if>
			</section>
				<c:if test="${currentAccount != null}">
				<section class="panel panel-wide">
					<h2>Financial Summary</h2>
					<div class="summary-grid">
						<div class="summary-box">
							<span class="overview-label">Total Income</span>
							<span class="overview-value" id="valTotalIncome">Laddar...</span>
						</div>
						<div class="summary-box">
							<span class="overview-label">Total Expenses</span>
							<span class="overview-value" id="valTotalExpenses">Laddar...</span>
						</div>
						<div class="summary-box">
							<span class="overview-label">Recurring Expenses</span>
							<span class="overview-value" id="valRecurringExpenses">Laddar...</span>
						</div>
					</div>
				</section>
			</c:if>
			
			<section class="panel panel-wide">
				<div id="weatherBox">
					<h3>Current Weather in Lund</h3>
					<p id="weatherText">Loading weather...</p>
				</div>
			</section>

		</main>
		
		<div id="editTransactionModal" class="modal-backdrop"
			aria-hidden="true">
			<div class="modal">
				<h3>Edit transaction</h3>
				<form action="${pageContext.request.contextPath}/MainViewServlet"
					method="post">
					<input type="hidden" name="action" value="updateTransaction">
					<input type="hidden" id="editTransactionId" name="transactionId">
					<div class="modal-grid">
						<div class="modal-field">
							<label for="editCategoryId">Category</label>
							<select id="editCategoryId" name="categoryId" required>
								<option value="">Select category</option>
								<c:forEach var="category" items="${categories}">
									<option value="${category.categoryId}">${category.categoryName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="modal-field">
							<label for="editTransactionDate">Date</label> <input type="date"
								id="editTransactionDate" name="transactionDate" required>
						</div>
						<div class="modal-field">
							<label for="editAmount">Amount</label> <input type="number"
								id="editAmount" name="amount" step="0.01" required>
						</div>
						<div class="modal-field">
							<label for="editNote">Comment</label>
							<textarea id="editNote" name="note"></textarea>
						</div>
						<div class="modal-field">
							<label>
								<input type="checkbox" id="editRepeatingTransaction"
									name="repeatingTransaction">
								Repeating transaction
							</label>
						</div>
					</div>
					<div class="modal-actions">
						<button type="submit" class="action-button modal-save">Save</button>
						<button type="button" class="action-button modal-close"
							id="closeEditModalButton">close</button>
					</div>
				</form>
			</div>
		</div>
		<div id="addTransactionModal" class="modal-backdrop" aria-hidden="true">
			<div class="modal">
				<h3>Add transaction</h3>
				<form action="${pageContext.request.contextPath}/MainViewServlet"
					method="post">
					<input type="hidden" name="action" value="addTransaction">
					<div class="modal-grid">
						<div class="modal-field">
							<label for="addCategoryId">Category</label>
							<select id="addCategoryId" name="categoryId" required>
								<option value="">Select category</option>
								<c:forEach var="category" items="${categories}">
									<option value="${category.categoryId}">${category.categoryName}</option>
								</c:forEach>
							</select>
						</div>
						<div class="modal-field">
							<label for="addTransactionDate">Date</label>
							<input type="date" id="addTransactionDate" name="transactionDate" required>
						</div>
						<div class="modal-field">
							<label for="addAmount">Amount</label>
							<input type="number" id="addAmount" name="amount" step="0.01" required>
						</div>
						<div class="modal-field">
							<label for="addNote">Comment</label>
							<textarea id="addNote" name="note"></textarea>
						</div>
						<div class="modal-field">
							<label>
								<input type="checkbox" name="repeatingTransaction">
								Repeating transaction
							</label>
						</div>
					</div>
					<div class="modal-actions">
						<button type="submit" class="action-button modal-save">Save</button>
						<button type="button" class="action-button modal-close"
							id="closeAddModalButton">Close</button>
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
		$(document)
				.ready(
						function() {
						
								<c:if test="${currentAccount != null}">
								    const accountId = ${currentAccount.accountId};
								    
								    const baseUrl = "${pageContext.request.contextPath}/Accounts/" + accountId;
								
								    $.ajax({
								        url: baseUrl + "/totalIncome",
								        method: "GET",
								        success: function(data) {
								            let value = data.totalIncome !== undefined ? data.totalIncome : data;
								            $("#valTotalIncome").text(value + " kr");
								        },
								        error: function(xhr, status, error) {
								            console.error("Inkomst-fel: URL = " + baseUrl + "/totalIncome");
								            console.error("Statuskod: " + xhr.status + " " + error);
								            $("#valTotalIncome").text("- kr");
								        }
								    });
								
								    $.ajax({
								        url: baseUrl + "/totalExpenses",
								        method: "GET",
								        success: function(data) {
								            let value = data.totalExpenses !== undefined ? data.totalExpenses : data;
								            $("#valTotalExpenses").text(value + " kr");
								        },
								        error: function(xhr, status, error) {
								            console.error("Utgifts-fel:", xhr.status, error);
								            $("#valTotalExpenses").text("- kr");
								        }
								    });
								
								    $.ajax({
								        url: baseUrl + "/recurringExpenses",
								        method: "GET",
								        success: function(data) {
								            let value = data.recurringExpenses !== undefined ? data.recurringExpenses : data;
								            $("#valRecurringExpenses").text(value + " kr");
								        },
								        error: function(xhr, status, error) {
								            console.error("Återkommande-fel:", xhr.status, error);
								            $("#valRecurringExpenses").text("- kr");
								        }
								    });
								</c:if>
							const modal = $("#editTransactionModal");
							const addModal = $("#addTransactionModal");
							const categoryIdField = $("#editCategoryId");
							const transactionDateField = $("#editTransactionDate");
							const amountField = $("#editAmount");
							const noteField = $("#editNote");
							const repeatingTransactionField = $("#editRepeatingTransaction");

							$("#openAddTransactionButton").on("click", function() {
								addModal.addClass("is-open").attr("aria-hidden", "false");
							});

							$("#closeAddModalButton").on("click", function() {
								addModal.removeClass("is-open").attr("aria-hidden", "true");
							});

							$(".edit-transaction-button").on(
									"click",
									function() {
										const button = $(this);

										$("#editTransactionId").val(
												button.data("transaction-id"));
										categoryIdField.val(button
												.data("category-id"));
										transactionDateField.val(button
												.data("transaction-date"));
										amountField.val(button.data("amount"));
										noteField.val(button.data("note"));
										repeatingTransactionField.prop("checked",
												button.data("repeating-transaction") === true
														|| button.data("repeating-transaction") === "true");

										modal.addClass("is-open").attr(
												"aria-hidden", "false");
									});

							$("#closeEditModalButton").on(
									"click",
									function() {
										modal.removeClass("is-open").attr(
												"aria-hidden", "true");
									});

							modal.on("click", function(event) {
								if (event.target === this) {
									modal.removeClass("is-open").attr(
											"aria-hidden", "true");
								}
							});

							addModal.on("click", function(event) {
								if (event.target === this) {
									addModal.removeClass("is-open").attr(
											"aria-hidden", "true");
								}
							});


							const apiKey = "9ec94ba1b7ac698d786c045e4f638cdd";
							const city = "Lund,SE";

							if (!apiKey || apiKey === "DIN_API_KEY_HAR") {
								$("#weatherText")
										.html(
												"Add your OpenWeather API key to load weather.");
								return;
							}

							$
									.ajax({
										url : "https://api.openweathermap.org/data/2.5/weather",
										method : "GET",
										data : {
											q : city,
											appid : apiKey,
											units : "metric"
										},
										success : function(data) {
											const temp = Math
													.round(data.main.temp);
											const description = data.weather[0].description;

											$("#weatherText")
													.html(
															"<span class=\"weather-city\">Lund, Sweden</span>"
																	+ "<span class=\"weather-temp\">"
																	+ temp
																	+ "°C</span>"
																	+ "<span class=\"weather-desc\">"
																	+ description
																	+ "</span>");
										},
										error : function(xhr) {
											let message = "Could not load weather";

											if (xhr.responseJSON
													&& xhr.responseJSON.message) {
												message += " ("
														+ xhr.status
														+ ": "
														+ xhr.responseJSON.message
														+ ")";
											} else if (xhr.status) {
												message += " (" + xhr.status
														+ ")";
											}

											$("#weatherText").html(message);
										}
									});
						});
	</script>
</body>

</html>