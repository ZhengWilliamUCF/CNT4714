<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%
String errormessage = (String)session.getAttribute("errormessage");
if (errormessage == null)
    errormessage = "";
%>
<h2 style="text-align: center;">Welcome To Fall 2022 Project 4 Enterprise Data System</h2>
<h3 style="text-align: center;"><strong>Data Entry Application</strong></h3>
<hr />
<p style="text-align: center;"><strong><img src="https://preview.redd.it/k2b67radi5r81.png?auto=webp&amp;s=11c6dcf59e1386c0c20fe403a5c1b54d23a169f6" alt="" width="131" height="131" /></strong></p>
<hr />
<p style="text-align: center;">You are connected to the Project 4 Enterprise System database as a <strong>data-entry-level</strong> user.</p>
<p style="text-align: center;">Enter the data values in the form below to add a new record to the corresponding database table.</p>
<hr />
<p style="text-align: center;"><strong>Suppliers Record Insert</strong></p>
<table style="width: 275px; margin-left: auto; margin-right: auto;">
<tbody>
<tr>
<td style="width: 50px; text-align: center;">snum</td>
<td style="width: 50px; text-align: center;">sname</td>
<td style="width: 50px; text-align: center;">status</td>
<td style="width: 50px; text-align: center;">city</td>
</tr>
<tr>
    <form action="/Project4/addSupplierRecord" method="post">
<td><input id="snum" name="snum" type="text" /></td>
<td><input id="sname" name="sname" type="text" /></td>
<td><input id="sstatus" name="sstatus" type="text" /></td>
<td><input id="scity" name="scity" type="text" /></td>  
</tr>
</tbody>
</table>
<div style="text-align: center;">&nbsp;</div>
<div style="text-align: center;"><button name = "button" value="SupplierSubmit">Enter Supplier Record Into Database</button></form><form action="/Project4/addSupplierRecord" method="get"><p style="text-align: center;"><button>Clear Data and Results</button></p></form>
<hr />
<p style="text-align: center;"><strong>Parts Record Insert</strong></p>
<table style="margin-left: auto; margin-right: auto;">
<tbody>
<tr>
<td style="text-align: center;">pnum</td>
<td style="text-align: center;">pname</td>
<td style="text-align: center;">color</td>
<td style="text-align: center;">weight</td>
<td style="text-align: center;">city</td>
</tr>
<tr>
    <form action="/Project4/addSupplierRecord" method="post">
<td><input id="pnum" name="pnum" type="text" /></td>
<td><input id="pname" name="pname" type="text" /></td>
<td><input id="pcolor" name="pcolor" type="text" /></td>
<td><input id="pweight" name="pweight" type="text" /></td>
<td><input id="pcity" name="pcity" type="text" /></td>
</tr>
</tbody>
</table>
<div style="text-align: center;">&nbsp;</div>
<div style="text-align: center;"><button name = "button" value="PartSubmit">Enter Part Record Into Database</button></form><form action="/Project4/addSupplierRecord" method="get"><p style="text-align: center;"><button>Clear Data and Results</button></p></form>
<div style="text-align: center;"><hr /></div>
<p style="text-align: center;"><strong>Jobs Record Insert</strong></p>
<table style="margin-left: auto; margin-right: auto;">
<tbody>
<tr>
<td style="text-align: center;">jnum</td>
<td style="text-align: center;">jname</td>
<td style="text-align: center;">numworkers</td>
<td style="text-align: center;">city</td>
</tr>
<tr>
    <form action="/Project4/addSupplierRecord" method="post">
<td><input id="jnum" name="jnum" type="text" /></td>
<td><input id="jname" name="jname" type="text" /></td>
<td><input id="jnumworkers" name="jnumworkers" type="text" /></td>
<td><input id="jcity" name="jcity" type="text" /></td>
</tr>
</tbody>
</table>
<div style="text-align: center;">&nbsp;</div>
<div style="text-align: center;"><button name = "button" value="JobSubmit">Enter Job Record Into Database</button></form><form action="/Project4/addSupplierRecord" method="get"><p style="text-align: center;"><button>Clear Data and Results</button></p></form>
<hr />
<p style="text-align: center;"><strong>Shipment Record Insert</strong></p>
<table style="margin-left: auto; margin-right: auto;">
<tbody>
<tr>
<td style="text-align: center;">snum</td>
<td style="text-align: center;">pnum</td>
<td style="text-align: center;">jnum</td>
<td style="text-align: center;">quantity</td>
</tr>
<tr>
    <form action="/Project4/addSupplierRecord" method="post">
<td><input id="srsnum" name="srsnum" type="text" /></td>
<td><input id="srpname" name="srpnum" type="text" /></td>
<td><input id="srjnum" name="srjnum" type="text" /></td>
<td><input id="srquantity" name="srquantity" type="text" /></td>
</tr>
</tbody>
</table>
<div style="text-align: center;">&nbsp;</div>
<div style="text-align: center;"><button name = "button" value="ShipmentSubmit">Enter Shipment Record Into Database</button></form><form action="/Project4/addSupplierRecord" method="get"><p style="text-align: center;"><button>Clear Data and Results</button></p></form>
<hr />
<p style="text-align: center;"><strong>Database Results:</strong></p>
<%= errormessage %>