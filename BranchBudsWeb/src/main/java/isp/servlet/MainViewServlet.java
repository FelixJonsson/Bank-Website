package isp.servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
	

		request.setAttribute("viewLoaded", Boolean.TRUE);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("currentAccount", currentAccount);
		request.setAttribute("transactions", transactions);
		request.setAttribute("categories", categories);
		request.setAttribute("status", request.getParameter("status"));


		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

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

