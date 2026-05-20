var accountsCache = [];
var categoriesCache = [];
var selectedAccountId = "";
var selectedTransactionId = "";
var originalTransactionSnapshot = null;

$(document).ready(function() {
    setStatus("Loading accounts and categories...", "info");
    updateActionStates();

    loadAccounts();
    loadCategories();

    $("#accountSelect").change(function() {
        selectedAccountId = $(this).val();
        syncSelectedAccount();
        clearTransactionForm();

        if (selectedAccountId !== "") {
            loadTransactionsForAccount(selectedAccountId);
        } else {
            renderTransactions([]);
            setStatus("Choose an account", "info");
        }
    });

    $("#RefreshTransactionsBtn").click(function() {
        if (selectedAccountId !== "") {
            loadTransactionsForAccount(selectedAccountId);
        } else {
            setStatus("Choose an account first", "error");
        }
    });

    $("#AddBtn").click(function() {
        if (!validateTransactionForm(false)) {
            return;
        }

        $.ajax({
            method: "POST",
            url: buildApiUrl("/Transactions/"),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: JSON.stringify(buildTransactionPayload()),
            error: ajaxAddReturnError,
            success: ajaxAddReturnSuccess
        });
    });

    $("#UpdateBtn").click(function() {
        if (!validateTransactionForm(true)) {
            return;
        }

        $.ajax({
            method: "PUT",
            url: buildApiUrl("/Transactions/" + encodeURIComponent(selectedTransactionId)),
            contentType: "application/json; charset=UTF-8",
            dataType: "json",
            data: JSON.stringify(buildTransactionPayload()),
            error: ajaxUpdateReturnError,
            success: ajaxUpdateReturnSuccess
        });
    });

    $("#DeleteBtn").click(function() {
        if (selectedTransactionId === "") {
            setStatus("Choose a transaction to delete");
            return;
        }

        $.ajax({
            method: "DELETE",
            url: buildApiUrl("/Transactions/" + encodeURIComponent(selectedTransactionId)),
            error: ajaxDeleteReturnError,
            success: ajaxDeleteReturnSuccess
        });
    });

    $("#ClearBtn").click(function() {
        clearTransactionForm();
        setStatus("Form cleared");
    });

    $("#categoryId, #transactionDate, #amount, #note, #repeatingTransaction").on("input change", function() {
        updateActionStates();
    });
});

function loadAccounts(preferredAccountId) {
    $.ajax({
        method: "GET",
        url: buildApiUrl("/Accounts/"),
        error: ajaxAccountsReturnError,
        success: function(result) {
            ajaxAccountsReturnSuccess(result, preferredAccountId);
        }
    });
}

function loadCategories() {
    $.ajax({
        method: "GET",
        url: buildApiUrl("/Transactions/categories"),
        error: ajaxCategoriesReturnError,
        success: ajaxCategoriesReturnSuccess
    });
}

function loadTransactionsForAccount(accountId) {
    $.ajax({
        method: "GET",
        url: buildApiUrl("/Transactions/account/" + encodeURIComponent(accountId)),
        error: ajaxTransactionsReturnError,
        success: ajaxTransactionsReturnSuccess
    });
}

function ajaxAccountsReturnSuccess(result, preferredAccountId) {
    accountsCache = result || [];
    renderAccounts(accountsCache, preferredAccountId);

    if (accountsCache.length === 0) {
        renderTransactions([]);
        setStatus("No accounts found", "error");
        return;
    }

    if (preferredAccountId) {
        $("#accountSelect").val(String(preferredAccountId));
    }

    if ($("#accountSelect").val() === "") {
        $("#accountSelect").val(String(accountsCache[0].accountId));
    }

    selectedAccountId = $("#accountSelect").val();
    syncSelectedAccount();

    if (selectedAccountId !== "") {
        loadTransactionsForAccount(selectedAccountId);
    }
}

function ajaxAccountsReturnError(xhr, status) {
    $("#accountSelect").html("<option value=\"\">Could not load accounts</option>");
    setStatus("Could not load accounts (" + getErrorCode(xhr, status) + ")", "error");
}

function ajaxCategoriesReturnSuccess(result) {
    categoriesCache = result || [];
    renderCategories(categoriesCache);
    if (categoriesCache.length === 0) {
        setStatus("No categories found", "error");
    }
}

function ajaxCategoriesReturnError(xhr, status) {
    $("#categoryId").html("<option value=\"\">Could not load categories</option>");
    setStatus("Could not load categories (" + getErrorCode(xhr, status) + ")", "error");
}

function ajaxTransactionsReturnSuccess(result) {
    renderTransactions(result || []);
    setStatus("Transactions loaded", "success");
}

function ajaxTransactionsReturnError(xhr, status) {
    renderTransactions([]);
    setStatus("Could not load transactions (" + getErrorCode(xhr, status) + ")", "error");
}

function ajaxAddReturnSuccess(result) {
    clearTransactionForm();
    if (result && result.transactionId) {
        selectedTransactionId = "";
    }
    setFormStatus("Transaction added");
    refreshAfterWrite("Transaction added");
}

function ajaxAddReturnError(xhr, status) {
    setFormStatus("Could not add transaction");
    setStatus("Could not add transaction (" + getErrorCode(xhr, status) + ")", "error");
}

function ajaxUpdateReturnSuccess() {
    clearTransactionForm();
    setFormStatus("Transaction updated");
    refreshAfterWrite("Transaction updated");
}

function ajaxUpdateReturnError(xhr, status) {
    setFormStatus("Could not update transaction");
    setStatus("Could not update transaction (" + getErrorCode(xhr, status) + ")", "error");
}

function ajaxDeleteReturnSuccess() {
    clearTransactionForm();
    setFormStatus("Transaction deleted");
    refreshAfterWrite("Transaction deleted");
}

function ajaxDeleteReturnError(xhr, status) {
    setFormStatus("Could not delete transaction");
    setStatus("Could not delete transaction (" + getErrorCode(xhr, status) + ")", "error");
}

function refreshAfterWrite(message) {
    var currentAccountId = selectedAccountId;
    loadAccounts(currentAccountId);
    setStatus(message, "success");
}

function buildApiUrl(path) {
    return window.location.protocol + "//" + window.location.hostname + ":8080/BranchBudsWeb" + path;
}

function renderAccounts(accounts, preferredAccountId) {
    var options = "<option value=\"\">Choose account</option>";

    $.each(accounts, function(index, account) {
        var selected = "";
        if (preferredAccountId && String(account.accountId) === String(preferredAccountId)) {
            selected = " selected";
        }
        options += "<option value=\"" + account.accountId + "\"" + selected + ">" +
            escapeHtml(account.accountName) + "</option>";
    });

    $("#accountSelect").html(options);
}

function renderCategories(categories) {
    var options = "<option value=\"\">Choose category</option>";

    if (!categories || categories.length === 0) {
        $("#categoryId").html("<option value=\"\">No categories found</option>");
        return;
    }

    $.each(categories, function(index, category) {
        options += "<option value=\"" + category.categoryId + "\">" +
            escapeHtml(category.categoryName) + "</option>";
    });

    $("#categoryId").html(options);
}

function renderTransactions(transactions) {
    var rows = "";

    if (!transactions || transactions.length === 0) {
        rows = "<tr><td colspan=\"5\">No transactions loaded.</td></tr>";
    } else {
        $.each(transactions, function(index, transaction) {
            var formattedDate = formatDate(transaction.transactionDate);
            var repeatingLabel = formatRepeating(transaction.repeatingTransaction);
            var rowClass = "transaction-row";
            if (String(transaction.transactionId) === String(selectedTransactionId)) {
                rowClass += " is-selected";
            }
            rows += "<tr class=\"" + rowClass + "\" data-transaction='" + escapeAttribute(JSON.stringify(transaction)) + "'>" +
                "<td>" + escapeHtml(transaction.categoryName) + "</td>" +
                "<td>" + escapeHtml(formattedDate) + "</td>" +
                "<td>" + escapeHtml(transaction.amount) + "</td>" +
                "<td>" + repeatingLabel + "</td>" +
                "<td>" + escapeHtml(transaction.note || "") + "</td>" +
                "</tr>";
        });
    }

    $("#transactionsTableBody").html(rows);

    $(".transaction-row").click(function() {
        $(".transaction-row").removeClass("is-selected");
        $(this).addClass("is-selected");
        var transaction = JSON.parse($(this).attr("data-transaction"));
        fillTransactionForm(transaction);
        setFormStatus("");
        setStatus("Transaction selected for update/delete", "info");
    });
}

function syncSelectedAccount() {
    var account = null;

    $.each(accountsCache, function(index, currentAccount) {
        if (String(currentAccount.accountId) === String(selectedAccountId)) {
            account = currentAccount;
            return false;
        }
    });

    if (account) {
        $("#selectedAccountName").text(account.accountName);
        $("#selectedAccountBalance").text(formatMoney(account.currentBalance));
    } else {
        $("#selectedAccountName").text("-");
        $("#selectedAccountBalance").text("-");
    }
}

function fillTransactionForm(transaction) {
    selectedTransactionId = String(transaction.transactionId || "");
    $("#categoryId").val(transaction.categoryId || "");
    $("#transactionDate").val(formatDate(transaction.transactionDate));
    $("#amount").val(normalizeAmountForInput(transaction.amount));
    $("#note").val(transaction.note || "");
    $("#repeatingTransaction").prop("checked", transaction.repeatingTransaction === true || transaction.repeatingTransaction === "true");
    originalTransactionSnapshot = captureFormSnapshot();
    updateActionStates();
}

function clearTransactionForm() {
    selectedTransactionId = "";
    $("#categoryId").val("");
    $("#transactionDate").val("");
    $("#amount").val("");
    $("#note").val("");
    $("#repeatingTransaction").prop("checked", false);
    originalTransactionSnapshot = null;
    $("#transactionsTableBody tr").removeClass("is-selected");
    setFormStatus("");
    updateActionStates();
}

function buildTransactionPayload() {
    return {
        accountId: $("#accountSelect").val(),
        categoryId: $("#categoryId").val(),
        transactionDate: $("#transactionDate").val() + "T00:00:00",
        amount: $("#amount").val(),
        note: $("#note").val(),
        repeatingTransaction: $("#repeatingTransaction").is(":checked")
    };
}

function validateTransactionForm(requireId) {
    if ($("#accountSelect").val() === "") {
        setStatus("Choose an account first", "error");
        return false;
    }

    if (requireId && selectedTransactionId === "") {
        setStatus("Choose a transaction first", "error");
        return false;
    }

    if ($("#categoryId").val() === "" || $("#transactionDate").val() === "" || $("#amount").val().trim() === "") {
        setStatus("Fill in category, date and amount", "error");
        return false;
    }

    return true;
}

function captureFormSnapshot() {
    return JSON.stringify({
        accountId: $("#accountSelect").val(),
        categoryId: $("#categoryId").val(),
        transactionDate: $("#transactionDate").val(),
        amount: $("#amount").val().trim(),
        note: $("#note").val(),
        repeatingTransaction: $("#repeatingTransaction").is(":checked")
    });
}

function hasTransactionChanged() {
    if (selectedTransactionId === "" || originalTransactionSnapshot === null) {
        return false;
    }
    return captureFormSnapshot() !== originalTransactionSnapshot;
}

function updateActionStates() {
    var hasSelection = selectedTransactionId !== "";
    $("#DeleteBtn").prop("disabled", !hasSelection);
    $("#UpdateBtn").prop("disabled", !hasSelection || !hasTransactionChanged());
}

function setFormStatus(message) {
    $("#formStatusLabel").text(message || "");
}

function setStatus(message, type) {
    var label = $("#statusLabel");
    label.removeClass("status-success status-error status-info");
    if (type === "success") {
        label.addClass("status-success");
    } else if (type === "error") {
        label.addClass("status-error");
    } else {
        label.addClass("status-info");
    }
    label.text(message);
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

function formatRepeating(value) {
    if (value === true || value === "true" || value === 1 || value === "1") {
        return "Yes";
    }
    return "No";
}

function formatMoney(value) {
    if (value === null || value === undefined || value === "") {
        return "-";
    }
    return value + " kr";
}

function normalizeAmountForInput(value) {
    if (value === null || value === undefined || value === "") {
        return "";
    }
    return String(Math.abs(Number(value)));
}

function escapeHtml(value) {
    return $("<div>").text(value == null ? "" : value).html();
}

function escapeAttribute(value) {
    return escapeHtml(value).replace(/'/g, "&#39;");
}
