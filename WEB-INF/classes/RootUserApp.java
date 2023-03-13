/* Name: William Zheng
 Course: CNT 4714 – Fall 2022 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 4, 2022
*/
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RootUserApp extends HttpServlet {
    private Statement test;
    public void init(ServletConfig config) throws ServletException{
    }

    public static Connection getConnection(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        try{
            filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Project4/WEB-INF/lib/root.properties");
            properties.load(filein);

            Class.forName(properties.getProperty("driver"));
            Connection conn = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("username"), properties.getProperty("password"));
            return conn;
        }
        catch(Exception e){
            e.printStackTrace();
        }   
            return null;
        }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("text/html");
        try {
            test = getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String clientcommandinputarea = request.getParameter("clientcommandinputarea");
        String[] commandSplit = clientcommandinputarea.split(" ");
        String html = "";
        String error = "";
        HttpSession session = request.getSession();
        if (Objects.equals(commandSplit[0], "select")){
            try {
                html = selectQuery(clientcommandinputarea);
                session.setAttribute("queryTable", html);
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                session.setAttribute("input", clientcommandinputarea);
                session.setAttribute("errormessage", "");
                request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            } catch (SQLException e) {
                error = errorMessage(e.getMessage(), "query");
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                session.setAttribute("queryTable", "");
                session.setAttribute("errormessage", error);
                request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            }
        } else
            try {
                int rowsaffected = test.executeUpdate(clientcommandinputarea);
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                String message = successMessage(rowsaffected, clientcommandinputarea);
                session.setAttribute("errormessage", message);
                request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            } catch (SQLException e) {
                error = errorMessage(e.getMessage(), "statement");
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                session.setAttribute("queryTable", "");
                session.setAttribute("errormessage", error);
                request.getRequestDispatcher("rootHome.jsp").forward(request, response);
            }
    }

    public String selectQuery(String text) throws SQLException{
        ResultSet resultset = test.executeQuery(text);
        String output = "<table style=\"margin-left: auto; margin-right: auto;\"><tr>";
        for (int i =1; i<= resultset.getMetaData().getColumnCount(); i++){
            output += "<th>" + resultset.getMetaData().getColumnName(i) + "</th>";
        }
        output += "</tr>";
        while (resultset.next()){
            output += "<tr>";
            for (int i = 1; i <= resultset.getMetaData().getColumnCount(); i++){
                output += "<td>" + resultset.getString(i) + "</td>";
            }
            output += "</tr>";
        }
        output += "</table>";
        return output;
    }

    public String cleantextarea(){
        String output = "<textarea cols=\"75\" name=\"clientcommandinputarea\" id=\"clientcommandinputarea\" rows=\"5\"></textarea>";
        return output;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        HttpSession session = request.getSession();
        String buttonvalue = request.getParameter("button");
        if (buttonvalue.equals("ResetForm")){
            session.setAttribute("textarea", cleantextarea());
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
        }
        else if (buttonvalue.equals("ClearResults")){
            session.setAttribute("queryTable", "");
            session.setAttribute("errormessage", "");
            request.getRequestDispatcher("rootHome.jsp").forward(request, response);
        }
    }

    public String savetextarea(String text){
        String output = "<textarea cols=\"75\" name=\"clientcommandinputarea\" id=\"clientcommandinputarea\" rows=\"5\">" + text + "</textarea>";
        return output;
    }

    public String errorMessage(String errortext, String type){
        String output = "<p style=\"text-align: center;\">Error executing the SQL " + type + " :</p>";
        output += "<p style=\"text-align: center;\">" + errortext + "</p>";
        return output;
    }

    public String successMessage(int rowsaffected, String command) throws SQLException{
        String output = "<p style=\"text-align: center;\">The statement executed successfully.</p>";
        output += "<p style=\"text-align: center;\">" + rowsaffected + " row(s) affected.</p>";
        if (command.contains("shipments")){
            output += "<p style=\"text-align: center;\">Business Logic Detected! - Updating Supplier Status</p>";
            int updates = businessLogic();
            output += "<p style=\"text-align: center;\">Business Logic updated " + updates + " supplier status marks.</p>";
        }
        else output += "<p style=\"text-align: center;\">Business Logic Not Triggered!</p>";
        return output;
    }
    //  This logic will increment by 5, the status of a supplier anytime that supplier
    //  is involved in the insertion/update of a shipment record in which the quantity is greater than or equal to
    //  100. Note that any update of quantity >= 100 will affect any supplier involved in a shipment with a
    //  quantity >= 100. The example screen shots illustrate this case. An insert of a shipment tuple (S5, P6,
    //  J7, 400) will cause the status of every supplier who has a shipment with a quantity of 100 or greater to
    //  be increased by 5. In other words, even if a supplier’s shipment is not directly affected by the update,
    //  their status will be affected if they have any shipment with quantity >= 100. (See page xxs for a bonus
    //  problem that implements a modified version of this business rule.)
    //  insert into shipments values ("S5", "P6", "J4", 400)
    //  update jobs set jname = "Tough Job" where jnum ="J1"
    //  update shipments set quantity = 10

    // First it will insert/update stuff in shipments
    // Check if command includes the word shipment
    // if does tehn trigger business logic
    // select * from shipments where quantity >=100 group by snum
    // get the values of snum for each row
    // select * from suppliers where snum = "S1"
    // update suppliers set status = 6 where snum = "S1"
    // get the S# of those that have a shipment with quantity over 100
    // select * from suppliers
    //
    public int businessLogic() throws SQLException{
        ArrayList<String> snums = new ArrayList<String>();
        // gets all unique shipments where their quantity is >= 100
        ResultSet resultset = test.executeQuery("select * from shipments where quantity >=100 group by snum");
        while (resultset.next()){
            // gets the snum and adds to arraylist
            snums.add(resultset.getString(1));
            //test.executeUpdate("\"update suppliers set status = 6 where snum = \"" +  snum + "\"");
        }
        int count = 0;
        while (snums.size() > count){
            // gets the supplier with sname 
            ResultSet resultset2 = test.executeQuery("select * from suppliers where snum = \"" + snums.get(count) + "\"");
            resultset2.next();
            // gets the current status
            int status = resultset2.getInt(3);
            status = status + 5;
            test.executeUpdate("update suppliers set status = " + status + " where snum = '" +  snums.get(count) + "'");
            count++;
        }
        return snums.size();
    }
}