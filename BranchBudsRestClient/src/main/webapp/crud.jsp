<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<script
src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js">
</script>
<link rel="stylesheet" type="text/css" href="css/transaction.css">
<script src="js/transaction.js"></script>
<meta charset="ISO-8859-1">
<title>BranchBuds Rest Client</title>
</head>
<body>
<header>
<p>BranchBuds Rest Client</p>
</header>
<section id="row">
<aside>
<fieldset id="ListFS">
<legend>Accounts</legend>
<input type="button" name="submitBtn" value="Get Accounts" id="GetAccountsBtn">
<table id="accountsTable">
<tr>
<th>AccountName</th>
<th>Balance</th>
</tr>
<tbody id="accountsTableBody">
<tr>
<td colspan="2">No accounts loaded.</td>
</tr>
</tbody>
</table>
</fieldset>
</aside>
<section id="main">
<section id="content">
<article>
<fieldset id="PersonalFS">
<legend>Find transactions for specific account</legend>
Account Name:<br>
<input type="text" name="accountName" id="accountName" value=""><br>
<br>
<input type="button" name="submitBtn" value="Get Transactions" id="GetTransactionsBtn">
</fieldset>
</article>
<article>
<fieldset id="ListFS">
<legend>Transactions</legend>
<table id="transactionsTable">
<tr>
<th>Id</th>
<th>Category</th>
<th>Date</th>
<th>Amount</th>
<th>Note</th>
</tr>
<tbody id="transactionsTableBody">
<tr>
<td colspan="5">No transactions loaded.</td>
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