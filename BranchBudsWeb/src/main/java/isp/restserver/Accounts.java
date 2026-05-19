package isp.restserver;

import java.io.IOException;

import java.io.PrintWriter;
import java.util.List;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

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
        
        if (splits.length == 2) {
            try {
                Account account = facade.findAccount(Integer.parseInt(splits[1]));
                sendAsJson(response, account);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } 
        else if (splits.length == 3) {
            try {
                int accountId = Integer.parseInt(splits[1]);
                String action = splits[2]; 
                
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                if (action.equals("totalIncome")) {
                    double income = facade.calculateTotalIncome(accountId);
                    out.print("{\"totalIncome\": " + income + "}");
                } 
                else if (action.equals("totalExpenses")) {
                    double expenses = facade.calculateTotalExpenses(accountId);
                    out.print("{\"totalExpenses\": " + expenses + "}");
                } 
                else if (action.equals("recurringExpenses")) {
                    double recurring = facade.calculateRecurringExpenses(accountId);
                    out.print("{\"recurringExpenses\": " + recurring + "}");
                } 
                else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                out.flush();
                
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
        } 
        
        else {
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
    

 	@GET
 	@Path("/{id}/totalIncome")
 	@Produces(MediaType.APPLICATION_JSON)
 	public Response getTotalIncome(@PathParam("id") int accountId) {
 		double income = facade.calculateTotalIncome(accountId); 
 		return Response.ok("{\"totalIncome\": " + income + "}").build();
 	}

 	@GET
 	@Path("/{id}/totalExpenses")
 	@Produces(MediaType.APPLICATION_JSON)
 	public Response getTotalExpenses(@PathParam("id") int accountId) {
 		double expenses = facade.calculateTotalExpenses(accountId);
 		return Response.ok("{\"totalExpenses\": " + expenses + "}").build();
 	}

 	@GET
 	@Path("/{id}/recurringExpenses")
 	@Produces(MediaType.APPLICATION_JSON)
 	public Response getRecurringExpenses(@PathParam("id") int accountId) {
 		double recurring = facade.calculateRecurringExpenses(accountId);
 		return Response.ok("{\"recurringExpenses\": " + recurring + "}").build();
 	}
}
