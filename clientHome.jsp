<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<!DOCTYPE html>
<%
String queryTable = (String)session.getAttribute("queryTable");
String input = (String)session.getAttribute("input");
String errormessage = (String)session.getAttribute("errormessage");
String textarea = (String)session.getAttribute("textarea");
if (queryTable == null) 
   	queryTable = "";
if (input == null)
    input = "";
if (errormessage == null)
    errormessage = "";
if (textarea == null)
    textarea = "<textarea cols='75' name='clientcommandinputarea' id='clientcommandinputarea' rows='5'></textarea>";
%>
<html>
<body>
<div>
<h2 style="text-align: center;">Welcome To Fall 2022 Project 4 Enterprise Data System</h2>
</div>
<div style="text-align: center;">
<h3><strong>A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat Container</strong></h3>
<hr />
<p><strong><img src="https://preview.redd.it/k2b67radi5r81.png?auto=webp&amp;s=11c6dcf59e1386c0c20fe403a5c1b54d23a169f6" alt="" width="131" height="131" /></strong></p>
</div>
<div style="text-align: center;"><hr /></div>
<div style="text-align: center;">
<p>You are connected to the Project 4 Enterprise System database as a <strong>client-level</strong> user.</p>
</div>
<div style="text-align: center;">
<p>Please enter any valid SQL query or update command in the box below.</p>
</div>
<div style="text-align: center;"><form action="/Project4/ClientUserApp" method="post">
<p><%= textarea %></p>
<div><button>Execute Command</button></div>
</form></div>
<form action="/Project4/ClientUserApp" method="get">
    <p style="text-align: center;"><button name = "button" value="ResetForm">Reset Form</button></p>
</form>
<form action="/Project4/ClientUserApp" method="get">
    <p style="text-align: center;"><button name = "button" value="ClearResults">Clear Results</button></p>
</form>
<div style="text-align: center;">
<p>All execution results will appear below this line:</p>
</div>
<div style="text-align: center;"><hr /></div>
<div>
<p style="text-align: center;"><strong>Database Results:</strong></p>
<%= queryTable %>
<%= errormessage %>
</div>
</body>
</html>