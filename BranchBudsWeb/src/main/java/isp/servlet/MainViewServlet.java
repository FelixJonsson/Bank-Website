package isp.servlet;

import java.io.IOException; 
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.Locale;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import isp.entity.Account;
import isp.entity.Category;
import isp.entity.Transaction;
import isp.entity.User;
import isp.facade.SystemFacadeLocal;

@WebServlet("/MainViewServlet")
public class MainViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	private SystemFacadeLocal systemFacade;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Category> categories = systemFacade.findAllCategories();

		User currentUser = systemFacade.findCurrentUser();
		Account currentAccount = systemFacade.findCurrentUserAccount();
		List<Transaction> transactions = systemFacade.findTransactionsForCurrentUser();
		double totalIncome = 0;
		double totalExpenses = 0;

		if (currentAccount != null) {
			totalIncome = systemFacade.calculateTotalIncome(currentAccount.getAccountId());
			totalExpenses = systemFacade.calculateTotalExpenses(currentAccount.getAccountId());
		}

		Map<String, Double> spendingByCategory = new LinkedHashMap<>();
		double maxCategorySpending = 0;
		YearMonth latestExpenseMonth = null;

		if (transactions != null) {
			for (Transaction transaction : transactions) {
				if (transaction.getAmount() < 0) {
					latestExpenseMonth = YearMonth.from(transaction.getTransactionDate().toLocalDateTime());
					break;
				}
			}

			for (Transaction transaction : transactions) {
				if (transaction.getAmount() < 0) {
					YearMonth transactionMonth = YearMonth.from(transaction.getTransactionDate().toLocalDateTime());

					if (!transactionMonth.equals(latestExpenseMonth)) {
						continue;
					}

					String categoryName = transaction.getCategory().getCategoryName();
					double expenseAmount = Math.abs(transaction.getAmount());

					Double currentTotal = spendingByCategory.get(categoryName);
					if (currentTotal == null) {
						currentTotal = 0.0;
					}

					double newTotal = currentTotal + expenseAmount;
					spendingByCategory.put(categoryName, newTotal);

					if (newTotal > maxCategorySpending) {
						maxCategorySpending = newTotal;
					}
				}
			}
		}

		Map<String, Double> sortedSpendingByCategory = new LinkedHashMap<>();
		spendingByCategory.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue(Comparator.reverseOrder()))
				.forEachOrdered(entry -> sortedSpendingByCategory.put(entry.getKey(), entry.getValue()));

		int chartStep = 2000;
		int chartMax = chartStep;

		if (maxCategorySpending > 0) {
			chartMax = (int) Math.ceil(maxCategorySpending / chartStep) * chartStep;
		}

		String spendingChartHeading = "Spending by category";
		if (latestExpenseMonth != null) {
			String monthName = latestExpenseMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
			spendingChartHeading = "Spending by category for " + monthName + " " + latestExpenseMonth.getYear();
		}

		
		request.setAttribute("viewLoaded", Boolean.TRUE);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("currentAccount", currentAccount);
		request.setAttribute("transactions", transactions);
		request.setAttribute("categories", categories);
		request.setAttribute("spendingByCategory", sortedSpendingByCategory);
		request.setAttribute("status", request.getParameter("status"));
		request.setAttribute("chartMax", chartMax);
		request.setAttribute("spendingChartHeading", spendingChartHeading);
		request.setAttribute("totalIncome", totalIncome);
		request.setAttribute("totalExpenses", totalExpenses);


		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {

	    request.setCharacterEncoding("UTF-8");
	    String action = request.getParameter("action");

	    if (action == null) {
	        response.sendRedirect(request.getContextPath() + "/MainViewServlet");
	        return;
	    }

	    try {
	        switch (action) {
	            case "addTransaction":
	            case "updateTransaction":
	                String dateString = request.getParameter("transactionDate");
	                LocalDate localDate = LocalDate.parse(dateString);
	                
	                if (localDate.isAfter(LocalDate.now())) {
	                    request.setAttribute("status", "dateError");
	                    request.setAttribute("errorMessage", "You cannot set a future date for a transaction.");
	                    request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	                    return;
	                }

	                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
	                BigDecimal amount = new BigDecimal(request.getParameter("amount"));
	                String note = request.getParameter("note");
	                boolean repeating = request.getParameter("repeatingTransaction") != null;
	                
	                Timestamp transactionDate = Timestamp.valueOf(localDate.atStartOfDay());
	                
	                if (note != null && note.matches(".*[åäöÅÄÖ].*")) {
	                    request.setAttribute("status", "invalidCharError");
	                    request.setAttribute("errorMessage", "Please use standard English characters. å, ä, and ö are not allowed.");
	                    request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
	                    return; 
	                }

	                if ("addTransaction".equals(action)) {
	                    Transaction created = systemFacade.createTransactionForCurrentUser(
	                            categoryId, transactionDate, amount, note, repeating);
	                    
	                    response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=" + 
	                            (created != null ? "added" : "addError"));
	                } else {
	                    int transactionId = Integer.parseInt(request.getParameter("transactionId"));
	                    Transaction updated = systemFacade.updateTransactionForCurrentUser(
	                            transactionId, categoryId, transactionDate, amount, note, repeating);
	                    
	                    response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=" + 
	                            (updated != null ? "updated" : "updateError"));
	                }
	                break;

	            case "deleteTransaction":
	                int deleteId = Integer.parseInt(request.getParameter("transactionId"));
	                systemFacade.deleteTransactionForCurrentUser(deleteId);
	                response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=deleted");
	                break;

	            default:
	                response.sendRedirect(request.getContextPath() + "/MainViewServlet");
	                break;
	        }

	    } catch (DateTimeParseException e) {
	        response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=" + 
	                (action.equals("addTransaction") ? "addError" : "updateError"));
	    } catch (IllegalArgumentException e) {
	        response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=" + 
	                action.replace("Transaction", "Error")); 
	    }
	}}
