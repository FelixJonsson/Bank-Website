package isp.servlet;

import java.io.IOException;
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

		List<User> allUsers = systemFacade.getAllUsers();
		List<Category> categories = systemFacade.findAllCategories();
		String selectedUserIdParam = request.getParameter("selectedUserId");

		User currentUser;
		Account currentAccount;
		List<Transaction> transactions;
		Integer selectedUserId = null;

		if (selectedUserIdParam != null && !selectedUserIdParam.isBlank()) {
			try {
				selectedUserId = Integer.valueOf(selectedUserIdParam);
				currentUser = systemFacade.findUser(selectedUserId);
				currentAccount = systemFacade.findAccountForUser(selectedUserId);
				transactions = systemFacade.findTransactionsForUser(selectedUserId);
			} catch (NumberFormatException e) {
				currentUser = systemFacade.findCurrentUser();
				currentAccount = systemFacade.findCurrentUserAccount();
				transactions = systemFacade.findTransactionsForCurrentUser();
				selectedUserId = currentUser != null ? currentUser.getUserId() : null;
			}
		} else {
			currentUser = systemFacade.findCurrentUser();
			currentAccount = systemFacade.findCurrentUserAccount();
			transactions = systemFacade.findTransactionsForCurrentUser();
			if (currentUser != null) {
				selectedUserId = currentUser.getUserId();
			}
		}

		request.setAttribute("viewLoaded", Boolean.TRUE);
		request.setAttribute("selectedUserId", selectedUserId);
		request.setAttribute("currentUser", currentUser);
		request.setAttribute("currentAccount", currentAccount);
		request.setAttribute("transactions", transactions);
		request.setAttribute("categories", categories);
		request.setAttribute("userList", allUsers);

		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
}
