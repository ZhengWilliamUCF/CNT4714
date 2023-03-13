/* Name: William Zheng
 Course: CNT 4714 – Fall 2022 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 4, 2022
*/
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class addSupplierRecord extends HttpServlet {
    private Statement test;
    private Statement test2;
    public void init(ServletConfig config) throws ServletException{
    }

    public static Connection getConnection(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        try{
            filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Project4/WEB-INF/lib/dataentry.properties");
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

    public static Connection getConnection2(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        try{
            filein = new FileInputStream("C:/Program Files/Apache Software Foundation/Tomcat 10.0/webapps/Project4/WEB-INF/lib/client.properties");
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
            test2 = getConnection2().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //https://stackoverflow.com/questions/19380089/how-to-redirect-to-servlet-when-we-have-multiple-buttons
        String buttonvalue = request.getParameter("button");

        if (buttonvalue.equals("SupplierSubmit")){
            insertSupplier(request, response);
        }
        else if (buttonvalue.equals("PartSubmit")){
            insertPart(request, response);
        }
        else if (buttonvalue.equals("JobSubmit")){
            insertJob(request, response);
        }
        else if (buttonvalue.equals("ShipmentSubmit")){
            try {
                insertShipment(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String successMessage(String type, String value1, String value2, String value3, String value4, String value5){
        String output = "<p style=\"text-align: center;\">New " + type +" record: ";
        if (type == "parts"){
            output += "(" + value1 + ", "+ value2 + ", " + value3 + ", "+ value4 + ", " + value5 +") - successfully entered into database.</p>";
        }
        else if (type == "shipments"){
            if (Integer.parseInt(value4) >= 100){
                try {
                    businessLogic();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                output += "(" + value1 + ", "+ value2 + ", " + value3 + ", "+ value4 + ") - successfully entered into database. Business logic is triggered.</p>";
            }
            else
            output += "(" + value1 + ", "+ value2 + ", " + value3 + ", "+ value4 + ") - successfully entered into database. Business logic not triggered.</p>";
        }
        else {
            output += "(" + value1 + ", "+ value2 + ", " + value3 + ", "+ value4 + ") - successfully entered into database.</p>";
        }
        return output;
    }

    public void insertSupplier(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        String type = "suppliers";
        String snum = request.getParameter("snum");
        String sname = request.getParameter("sname");
        String sstatus = request.getParameter("sstatus");
        String scity = request.getParameter("scity");
        //out.println("<h1>" + snum + "</h1>");

        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        String updateString = "insert into suppliers values (?,?,?,?)";
        try {
            PreparedStatement updateSuppliers = getConnection().prepareStatement(updateString);
            updateSuppliers.setString(1, snum);
            updateSuppliers.setString(2, sname);
            updateSuppliers.setString(3, sstatus);
            updateSuppliers.setString(4, scity);
            updateSuppliers.executeUpdate();
            //getConnection().commit();
            session.setAttribute("errormessage", successMessage(type, snum, sname, sstatus, scity, ""));
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errormessage",e.getMessage());
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        }
    }

    public void insertPart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        String type = "parts";
        String pnum = request.getParameter("pnum");
        String pname = request.getParameter("pname");
        String pcolor = request.getParameter("pcolor");
        String pweight = request.getParameter("pweight");
        String pcity = request.getParameter("pcity");

        String updateString = "insert into parts values (?,?,?,?,?)";
        try {
            PreparedStatement updateSuppliers = getConnection().prepareStatement(updateString);
            updateSuppliers.setString(1, pnum);
            updateSuppliers.setString(2, pname);
            updateSuppliers.setString(3, pcolor);
            updateSuppliers.setString(4, pweight);
            updateSuppliers.setString(5, pcity);
            updateSuppliers.executeUpdate();
            //getConnection().commit();
            session.setAttribute("errormessage", successMessage(type, pnum, pname, pcolor, pweight, pcity));
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errormessage",e.getMessage());
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        }
    }

    public void insertJob(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        HttpSession session = request.getSession();
        String type = "jobs";
        String jnum = request.getParameter("jnum");
        String jname = request.getParameter("jname");
        String jnumworkers = request.getParameter("jnumworkers");
        String jcity = request.getParameter("jcity");
        //out.println("<h1>" + snum + "</h1>");

        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        String updateString = "insert into jobs values (?,?,?,?)";
        try {
            PreparedStatement updateSuppliers = getConnection().prepareStatement(updateString);
            updateSuppliers.setString(1, jnum);
            updateSuppliers.setString(2, jname);
            updateSuppliers.setString(3, jnumworkers);
            updateSuppliers.setString(4, jcity);
            updateSuppliers.executeUpdate();
            //getConnection().commit();
            session.setAttribute("errormessage", successMessage(type, jnum, jname, jnumworkers, jcity, ""));
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            session.setAttribute("errormessage",e.getMessage());
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        }
    }

    public void insertShipment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException{
        HttpSession session = request.getSession();
        String type = "shipments";
        String srsnum = request.getParameter("srsnum");
        String srpnum = request.getParameter("srpnum");
        String srjnum = request.getParameter("srjnum");
        String srquantity = request.getParameter("srquantity");
        //out.println("<h1>" + snum + "</h1>");

        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        String updateString = "insert into shipments values (?,?,?,?)";
        if (checkForeign(srsnum, srpnum, srjnum) == true){
                try {
                    PreparedStatement updateSuppliers = getConnection().prepareStatement(updateString);
                    updateSuppliers.setString(1, srsnum);
                    updateSuppliers.setString(2, srpnum);
                    updateSuppliers.setString(3, srjnum);
                    updateSuppliers.setString(4, srquantity);
                    updateSuppliers.executeUpdate();
                    //getConnection().commit();
                    session.setAttribute("errormessage", successMessage(type, srsnum, srpnum, srjnum, srquantity, ""));
                    request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);

                } catch (SQLException e) {
                    e.printStackTrace();
                    session.setAttribute("errormessage",e.getMessage());
                    request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
                }
        }
        else {
            session.setAttribute("errormessage", "<p>Error executing the SQL statement:</p><p>Cannot add or update a child row: a foreign key constraint fails.</p>");
            request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
        }
    }

    // check if the values already exists in a table
    public boolean checkForeign(String srsnum, String srpnum, String srjnum) throws SQLException{
        ResultSet resultset1 = test2.executeQuery("select * from suppliers where snum = \"" + srsnum + "\"");
        if (resultset1.next() ==  false){
            return false;
        }
        ResultSet resultset2 = test2.executeQuery("select * from parts where pnum = \"" + srpnum + "\"");
        if (resultset2.next() ==  false){
            return false;
        }
        ResultSet resultset3 = test2.executeQuery("select * from jobs where jnum = \"" + srjnum + "\"");
        if (resultset3.next() ==  false){
            return false;
        }
        return true;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        HttpSession session = request.getSession();
        session.setAttribute("errormessage", "");
        request.getRequestDispatcher("dataentryHome.jsp").forward(request, response);
    }

    public void businessLogic() throws SQLException{
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
    }
}