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

		String action = request.getParameter("action");

		if ("addTransaction".equals(action)) {
		    String dateString = request.getParameter("transactionDate");
		    LocalDate transactionDate;

		    try {
		        transactionDate = LocalDate.parse(dateString);
		        LocalDate today = LocalDate.now();

		        if (transactionDate.isAfter(today)) {
		            request.setAttribute("status", "dateError"); 
		            request.setAttribute("errorMessage", "Du kan inte lägga till transaktioner i framtiden.");
		            request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
		            return; 
		        }

		    } catch (DateTimeParseException e) {
		    }
		}
		
		if (action == null) {
			response.sendRedirect(request.getContextPath() + "/MainViewServlet");
			return;
		}

		try {
			if ("addTransaction".equals(action)) {
				int categoryId = Integer.parseInt(request.getParameter("categoryId"));
				String transactionDateValue = request.getParameter("transactionDate");
				BigDecimal amount = new BigDecimal(request.getParameter("amount"));
				String note = request.getParameter("note");
				boolean repeatingTransaction = request.getParameter("repeatingTransaction") != null;
				Timestamp transactionDate = Timestamp.valueOf(transactionDateValue + " 00:00:00");

				Transaction createdTransaction = systemFacade.createTransactionForCurrentUser(categoryId,
						transactionDate, amount, note, repeatingTransaction);

				if (createdTransaction == null) {
					response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=addError");
					return;
				}

				response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=added");
				return;
			}

			if ("updateTransaction".equals(action)) {
				int transactionId = Integer.parseInt(request.getParameter("transactionId"));
				int categoryId = Integer.parseInt(request.getParameter("categoryId"));
				String transactionDateValue = request.getParameter("transactionDate");
				BigDecimal amount = new BigDecimal(request.getParameter("amount"));
				String note = request.getParameter("note");
				boolean repeatingTransaction = request.getParameter("repeatingTransaction") != null;
				Timestamp transactionDate = Timestamp.valueOf(transactionDateValue + " 00:00:00");

				Transaction updatedTransaction = systemFacade.updateTransactionForCurrentUser(transactionId,
						categoryId, transactionDate, amount, note, repeatingTransaction);

				if (updatedTransaction == null) {
					response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=updateError");
					return;
				}

				response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=updated");
				return;
			}

			if ("deleteTransaction".equals(action)) {
				int transactionId = Integer.parseInt(request.getParameter("transactionId"));
				Transaction transaction = systemFacade.findTransactionForCurrentUser(transactionId);

				if (transaction == null) {
					response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=deleteError");
					return;
				}

				systemFacade.deleteTransactionForCurrentUser(transactionId);
				response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=deleted");
				return;
			}

			response.sendRedirect(request.getContextPath() + "/MainViewServlet");
		} catch (IllegalArgumentException e) {
			String status = "addError";

			if ("updateTransaction".equals(action)) {
				status = "updateError";
			} else if ("deleteTransaction".equals(action)) {
				status = "deleteError";
			}

			response.sendRedirect(request.getContextPath() + "/MainViewServlet?status=" + status);
		}
	}
}
