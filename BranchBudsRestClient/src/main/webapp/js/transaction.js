$(document).ready(function(){
setStatus("Ready");

$("#GetAccountsBtn").click(function() {
$.ajax({
method: "GET",
url: buildApiUrl("/Accounts/"),
error: ajaxAccountsReturnError,
success: ajaxAccountsReturnSuccess
});
});

$("#GetTransactionsBtn").click(function() {
var accountName = $("#accountName").val();
if (accountName !== "") {
$.ajax({
method: "GET",
url: buildApiUrl("/Transactions/account-name/" + encodeURIComponent(accountName)),
error: ajaxTransactionsReturnError,
success: ajaxTransactionsReturnSuccess
});
} else {
setStatus("Enter an account name");
}
});
});//End ready function

function ajaxAccountsReturnSuccess(result, status, xhr) {
renderAccounts(result);
setStatus("Accounts loaded");
}

function ajaxAccountsReturnError(xhr, status, error) {
setStatus("Could not load accounts (" + getErrorCode(xhr, status) + ")");
}

function ajaxTransactionsReturnSuccess(result, status, xhr) {
renderTransactions(result);
setStatus("Transactions loaded");
}

function ajaxTransactionsReturnError(xhr, status, error) {
setStatus("Could not load transactions (" + getErrorCode(xhr, status) + ")");
}

function buildApiUrl(path) {
return window.location.protocol + "//" + window.location.hostname + ":8080/BranchBudsWeb" + path;
}

function renderAccounts(accounts) {
var rows = "";
if (!accounts || accounts.length === 0) {
rows = "<tr><td colspan=\"2\">No accounts loaded.</td></tr>";
} else {
$.each(accounts, function(index, account) {
rows += "<tr>" +
"<td>" + account.accountName + "</td>" +
"<td>" + account.currentBalance + "</td>" +
"</tr>";
});
}
$("#accountsTableBody").html(rows);
}

function renderTransactions(transactions) {
var rows = "";
if (!transactions || transactions.length === 0) {
rows = "<tr><td colspan=\"5\">No transactions loaded.</td></tr>";
} else {
$.each(transactions, function(index, transaction) {
var formattedDate = formatDate(transaction.transactionDate);
rows += "<tr>" +
"<td>" + transaction.id + "</td>" +
"<td>" + transaction.categoryName + "</td>" +
"<td>" + formattedDate + "</td>" +
"<td>" + transaction.amount + "</td>" +
"<td>" + transaction.note + "</td>" +
"</tr>";
});
}
$("#transactionsTableBody").html(rows);
}

function setStatus(message) {
$("#statusLabel").text(message);
}

function getErrorCode(xhr, status) {
if (xhr && xhr.status) {
return xhr.status;
}
return status;
}

function formatDate(value) {
if (!value) {
return "";
}
return value.substring(0, 10);
}
