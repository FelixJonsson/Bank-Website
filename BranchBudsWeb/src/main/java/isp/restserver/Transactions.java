package isp.restserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import isp.entity.Category;
import isp.entity.Transaction;
import isp.facade.SystemFacadeLocal;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Transactions/*")
public class Transactions extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @EJB
    private SystemFacadeLocal facade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            if (splits.length == 3 && "account".equals(splits[1])) {
                try {
                    List<Transaction> transactions = facade.findTransactionsForAccountId(
                        Integer.parseInt(splits[2]));
                    sendAsJson(response, transactions);
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                return;
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("categories".equals(splits[1])) {
            List<Category> categories = facade.findAllCategories();
            sendCategoriesAsJson(response, categories);
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            String id = splits[1];
            Transaction transaction = facade.findTransactionById(Integer.parseInt(id));
            if (transaction != null) {
                facade.deleteTransactionById(Integer.parseInt(id));
            }
            sendAsJson(response, transaction);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            BufferedReader reader = request.getReader();
            TransactionPayload payload = parseJsonTransaction(reader);
            Transaction transaction = facade.createTransactionForAccount(
                payload.accountId,
                payload.categoryId,
                payload.transactionDate,
                payload.amount,
                payload.note,
                payload.repeatingTransaction
            );
            sendAsJson(response, transaction);
            return;
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] splits = pathInfo.split("/");
        if (splits.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            String id = splits[1];
            BufferedReader reader = request.getReader();
            TransactionPayload payload = parseJsonTransaction(reader);
            Transaction transaction = facade.updateTransactionById(
                Integer.parseInt(id),
                payload.categoryId,
                payload.transactionDate,
                payload.amount,
                payload.note,
                payload.repeatingTransaction
            );
            sendAsJson(response, transaction);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void sendAsJson(HttpServletResponse response, Transaction transaction)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (transaction != null) {
            JsonObjectBuilder object = Json.createObjectBuilder();
            object.add("transactionId", String.valueOf(transaction.getTransactionId()));
            object.add("categoryId", String.valueOf(transaction.getCategory().getCategoryId()));
            object.add("categoryName", transaction.getCategory().getCategoryName());
            object.add("transactionDate", transaction.getTransactionDate().toLocalDateTime().toString());
            object.add("amount", String.valueOf(transaction.getAmount()));
            object.add("repeatingTransaction", transaction.isRepeatingTransaction());
            object.add("note", transaction.getNote() == null ? "" : transaction.getNote());
            out.print(object.build());
        } else {
            out.print("{ }");
        }

        out.flush();
    }

    private void sendAsJson(HttpServletResponse response, List<Transaction> transactions)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (transactions != null) {
            JsonArrayBuilder array = Json.createArrayBuilder();
            for (Transaction transaction : transactions) {
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("transactionId", String.valueOf(transaction.getTransactionId()));
                object.add("categoryId", String.valueOf(transaction.getCategory().getCategoryId()));
                object.add("categoryName", transaction.getCategory().getCategoryName());
                object.add("transactionDate", transaction.getTransactionDate().toLocalDateTime().toString());
                object.add("amount", String.valueOf(transaction.getAmount()));
                object.add("repeatingTransaction", transaction.isRepeatingTransaction());
                object.add("note", transaction.getNote() == null ? "" : transaction.getNote());
                array.add(object);
            }
            JsonArray jsonArray = array.build();
            out.print(jsonArray);
        } else {
            out.print("[]");
        }

        out.flush();
    }

    private void sendCategoriesAsJson(HttpServletResponse response, List<Category> categories)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (categories != null) {
            JsonArrayBuilder array = Json.createArrayBuilder();
            for (Category category : categories) {
                JsonObjectBuilder object = Json.createObjectBuilder();
                object.add("categoryId", category.getCategoryId());
                object.add("categoryName", category.getCategoryName());
                object.add("categoryType", category.getCategoryType() == null ? "" : category.getCategoryType());
                array.add(object);
            }
            JsonArray jsonArray = array.build();
            out.print(jsonArray);
        } else {
            out.print("[]");
        }

        out.flush();
    }

    private TransactionPayload parseJsonTransaction(BufferedReader reader) {
        JsonReader jsonReader = Json.createReader(reader);
        JsonObject jsonRoot = jsonReader.readObject();

        TransactionPayload payload = new TransactionPayload();
        payload.accountId = Integer.parseInt(jsonRoot.getString("accountId"));
        payload.categoryId = Integer.parseInt(jsonRoot.getString("categoryId"));
        payload.transactionDate = parseTimestamp(jsonRoot.getString("transactionDate"));
        payload.amount = new BigDecimal(jsonRoot.getString("amount"));
        payload.note = jsonRoot.containsKey("note") ? jsonRoot.getString("note") : "";
        payload.repeatingTransaction = jsonRoot.containsKey("repeatingTransaction")
                && jsonRoot.getBoolean("repeatingTransaction");
        return payload;
    }

    private Timestamp parseTimestamp(String value) {
        return Timestamp.valueOf(LocalDateTime.parse(value));
    }

    private static class TransactionPayload {
        private int accountId;
        private int categoryId;
        private Timestamp transactionDate;
        private BigDecimal amount;
        private String note;
        private boolean repeatingTransaction;
    }
}
