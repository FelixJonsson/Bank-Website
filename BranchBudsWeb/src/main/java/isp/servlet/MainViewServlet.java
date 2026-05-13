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
		String selectedUserIdParam = request.getParameter("selectedUserId");// Fånga upp klicket på "Redigera"
		String editIdStr = request.getParameter("editTransactionId");



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
		
		if (editIdStr != null && !editIdStr.isEmpty()) {
		    try {
		        int txId = Integer.parseInt(editIdStr);
		        Transaction tx = systemFacade.findTransaction(txId); 
		        request.setAttribute("transactionToEdit", tx);
		    } catch (Exception e) {
		        System.out.println("Fel vid hämtning av transaktion: " + e.getMessage());
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    
	    String selectedUserId = request.getParameter("selectedUserId");
	    String transactionIdStr = request.getParameter("transactionId");
	    String note = request.getParameter("note"); 
	    String amountStr = request.getParameter("amount");
	    String categoryIdStr = request.getParameter("categoryId");

	    try {
	        int transactionId = Integer.parseInt(transactionIdStr);
	        double amount = Double.parseDouble(amountStr);
	        int categoryId = Integer.parseInt(categoryIdStr);

	        systemFacade.updateTransaction(transactionId, categoryId, amount);

	    } catch (Exception e) {
	        System.out.println("Fel vid uppdatering: " + e.getMessage());
	    }

	    String redirectUrl = request.getContextPath() + "/MainViewServlet";
	    if (selectedUserId != null && !selectedUserId.isBlank()) {
	        redirectUrl += "?selectedUserId=" + selectedUserId;
	    }
	    response.sendRedirect(redirectUrl);
	}
}
