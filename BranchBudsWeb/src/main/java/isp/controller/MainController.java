package isp.controller;

import java.io.IOException;
import java.util.List;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import isp.facade.SystemFacadeLocal;
import isp.entity.User;

@WebServlet("/MainController")
public class MainController extends HttpServlet {
    @EJB
    private SystemFacadeLocal systemFacade;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        List<User> allUsers = systemFacade.getAllUsers();
        List<Category> allCategories = systemFacade.getAllCategories();

        request.setAttribute("userList", allUsers);
        request.setAttribute("categoryList", allCategories);

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}