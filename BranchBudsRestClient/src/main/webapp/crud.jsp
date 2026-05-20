<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="css/transaction.css?v=20260520b">
<script src="js/transaction.js?v=20260520b"></script>
<meta charset="UTF-8">
<title>BranchBuds Rest Client</title>
</head>
<body>
<header>
<p>BranchBuds Rest Client</p>
</header>
<section id="row">
<aside>
<fieldset id="AccountFS">
<legend>Account</legend>
<label for="accountSelect">Choose account</label>
<select id="accountSelect">
<option value="">Loading accounts...</option>
</select>
<input type="button" value="Refresh transactions" id="RefreshTransactionsBtn">
<div id="accountInfo">
<p><strong>Account:</strong> <span id="selectedAccountName">-</span></p>
<p><strong>Balance:</strong> <span id="selectedAccountBalance">-</span></p>
</div>
</fieldset>
</aside>
<section id="main">
<section id="content">
<article>
<fieldset id="TransactionFS">
<legend>Transaction</legend>
<label for="categoryId">Category</label>
<select id="categoryId" name="categoryId">
<option value="">Loading categories...</option>
</select>

<label for="transactionDate">Date</label>
<input type="date" name="transactionDate" id="transactionDate" value="">

<label for="amount">Amount</label>
<input type="number" name="amount" id="amount" step="0.01" value="">

<label for="note">Note</label>
<input type="text" name="note" id="note" value="">

<label class="checkbox-row" for="repeatingTransaction">
<input type="checkbox" name="repeatingTransaction" id="repeatingTransaction">
Repeating transaction
</label>

<div class="button-row">
<input type="button" value="Add" id="AddBtn">
<input type="button" value="Update" id="UpdateBtn">
<input type="button" value="Delete" id="DeleteBtn">
<input type="button" value="Clear" id="ClearBtn">
</div>
<p id="formStatusLabel" class="form-status"></p>
</fieldset>
</article>
<article>
<fieldset id="ListFS">
<legend>Transactions for selected account</legend>
<table id="transactionsTable">
<thead>
<tr>
<th>Category</th>
<th>Date</th>
<th>Amount</th>
<th>Repeating</th>
<th>Note</th>
</tr>
</thead>
<tbody id="transactionsTableBody">
<tr>
<td colspan="5">Choose an account to load transactions.</td>
</tr>
</tbody>
</table>
</fieldset>
</article>
<p id="statusLabel"></p>
</section>
</section>
</section>
<footer>
<p>BranchBuds</p>
</footer>
</body>
</html>
