<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.functions" prefix="fn"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<title>BranchBuds Dashboard</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="page">
		<header class="topbar">
			<a class="brand brand-link" href="${pageContext.request.contextPath}/MainViewServlet">
	<img src="${pageContext.request.contextPath}/images/branchbuds-logo.png"
		alt="BranchBuds"
		class="brand-logo">
	<p>Personal financial overview</p>
</a>

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
									class="overview-value"><fmt:formatNumber value="${currentAccount.currentBalance}" maxFractionDigits="0"/> kr</span>
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
	<h2>${spendingChartHeading}</h2>

	<c:choose>
		<c:when test="${empty spendingByCategory}">
			<p class="empty-state">No expenses found.</p>
		</c:when>
		<c:otherwise>
			<div class="expense-list">
				<c:forEach var="entry" items="${spendingByCategory}">
					<div class="expense-item">
						<div class="expense-item-label">${entry.key}</div>
						<div class="expense-item-row">
							<div class="expense-track">
								<div class="expense-fill"
									style="width: ${(entry.value / chartMax) * 100}%;"></div>
							</div>
							<div class="expense-item-value"><fmt:formatNumber value="${entry.value}" maxFractionDigits="0"/> kr</div>
						</div>
					</div>
				</c:forEach>
			</div>
		</c:otherwise>
	</c:choose>
</section>


			<section class="panel panel-wide">
				<div class="section-heading">
					<h2>Transactions</h2>
					<button type="button" class="action-button btn-primary" 
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

				<div class="transactions-table-wrap">
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
								<td class="amount"><fmt:formatNumber value="${transaction.amount}" maxFractionDigits="0"/> kr</td>
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
								        class="action-button btn-primary edit-transaction-button"
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
								        <input type="hidden" name="transactionId" value="${transaction.transactionId}">
								        <button type="submit" class="action-button btn-destructive">Delete</button>
								    </form>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>

				<c:if test="${empty transactions}">
					<p class="empty-state">No transactions were found for the
						selected account.</p>
				</c:if>
			</section>
				<c:if test="${currentAccount != null}">
				<section class="panel panel-wide">
					<h2>Summary This Month</h2>
					<div class="summary-grid">
						<div class="summary-box">
							<span class="overview-label">Total Income</span>
							<span class="overview-value"><fmt:formatNumber value="${totalIncome}" maxFractionDigits="0"/> kr</span>
						</div>
						<div class="summary-box">
							<span class="overview-label">Total Expenses</span>
							<span class="overview-value"><fmt:formatNumber value="${totalExpenses}" maxFractionDigits="0"/> kr</span>
						</div>
					</div>
				</section>
			</c:if>

			<section class="panel panel-wide">
	<div id="weatherBox" class="weather-layout">
		<div>
			<h3>Current Weather in Lund</h3>
			<p id="weatherText">Loading weather...</p>
		</div>
		<img id="weatherImage"
			src="${pageContext.request.contextPath}/images/sun_behind_clouds.png"
			alt="Current weather"
			class="weather-image">
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
						<button type="submit" class="action-button btn-primary">Save</button>
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
								Repeating
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
	<p>BranchBuds · <em>Track spending. Spot patterns. Grow your budget.</em> · 2026</p>
</footer>

	</div>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	<script>
		$(document)
				.ready(
						function() {
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
											
											const mainWeather = data.weather[0].main;
											const descriptionLower = description.toLowerCase();

											let weatherImage = "${pageContext.request.contextPath}/images/cloudy.png";

											if (mainWeather === "Clear") {
												weatherImage = "${pageContext.request.contextPath}/images/sunny.png";
											} else if (mainWeather === "Rain" || mainWeather === "Drizzle" || mainWeather === "Thunderstorm") {
												weatherImage = "${pageContext.request.contextPath}/images/rain.png";
											} else if (mainWeather === "Clouds") {
												if (descriptionLower.includes("overcast")) {
													weatherImage = "${pageContext.request.contextPath}/images/cloudy.png";
												} else {
													weatherImage = "${pageContext.request.contextPath}/images/sun_behind_clouds.png";
												}
											}

											$("#weatherImage").attr("src", weatherImage);



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
