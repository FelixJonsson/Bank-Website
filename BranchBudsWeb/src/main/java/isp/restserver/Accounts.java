package isp.restserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import isp.entity.Account;
import isp.facade.SystemFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Accounts/*")
public class Accounts extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private SystemFacadeLocal facade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<Account> allAccounts = facade.findAllAccounts();
            sendAsJson(response, allAccounts);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Account account = facade.findAccount(Integer.parseInt(splits[1]));
            sendAsJson(response, account);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendAsJson(HttpServletResponse response, Account account)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (account != null) {
            JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("accountName", account.getAccountName());
            object.add("currentBalance", String.valueOf(account.getCurrentBalance()));
            out.print(object.build());
        } else {
            out.print("{ }");
        }

        out.flush();
    }

    private void sendAsJson(HttpServletResponse response, List<Account> accounts)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (accounts != null) {
            JsonArrayBuilder array = Json.createArrayBuilder();
            for (Account account : accounts) {
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("accountName", account.getAccountName());
                object.add("currentBalance", String.valueOf(account.getCurrentBalance()));
                array.add(object);
            }
            JsonArray jsonArray = array.build();
            out.print(jsonArray);
        } else {
            out.print("[]");
        }

        out.flush();
    }
}
