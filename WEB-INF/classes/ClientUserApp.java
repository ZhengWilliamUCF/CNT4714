/* Name: William Zheng
 Course: CNT 4714 – Fall 2022 – Project Four
 Assignment title: A Three-Tier Distributed Web-Based Application
 Date: December 4, 2022
*/
import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ClientUserApp extends HttpServlet {
    private Statement test;
    public void init(ServletConfig config) throws ServletException{
    }

    public static Connection getConnection(){
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
                request.getRequestDispatcher("clientHome.jsp").forward(request, response);
            } catch (SQLException e) {
                error = errorMessage(e.getMessage(), "query");
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                session.setAttribute("queryTable", "");
                session.setAttribute("errormessage", error);
                request.getRequestDispatcher("clientHome.jsp").forward(request, response);
            }
        } else{
            try {
                test.executeUpdate(clientcommandinputarea);    
            } catch (SQLException e) {
                error = errorMessage(e.getMessage(), "statement");
                session.setAttribute("textarea", savetextarea(clientcommandinputarea));
                session.setAttribute("queryTable", "");
                session.setAttribute("errormessage", error);
                request.getRequestDispatcher("clientHome.jsp").forward(request, response);
            }
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
            request.getRequestDispatcher("clientHome.jsp").forward(request, response);
        }
        else if (buttonvalue.equals("ClearResults")){
            session.setAttribute("queryTable", "");
            session.setAttribute("errormessage", "");
            request.getRequestDispatcher("clientHome.jsp").forward(request, response);
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
}