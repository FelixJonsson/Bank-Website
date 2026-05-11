package isp.controller;

import java.io.IOException;
import java.util.List;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import isp.entity.Category;

import isp.facade.SystemFacadeLocal;
import isp.entity.User;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    @EJB
    private SystemFacadeLocal systemFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    	List<User> allUsers = new ArrayList<User>();
    	User currentUser = systemFacade.findCurrentUser();

    	if (currentUser != null) {
    	    allUsers.add(currentUser);
    	}

    	List<Category> allCategories = systemFacade.findAllCategories();


        request.setAttribute("userList", allUsers);
        request.setAttribute("categoryList", allCategories);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
