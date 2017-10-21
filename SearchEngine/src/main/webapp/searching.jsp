<%-- 
    Document   : searching
    Created on : Oct 1, 2017, 12:41:40 PM
    Author     : tutiya
--%>


<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<%@page import="demo.*" %>

<%
        //request.setCharacterEncoding("UTF-8");
        String myquerystring = request.getParameter("searchtext");
        
        System.out.println("Search for query string="+myquerystring);
        List myresult = SearchFiles.search(myquerystring,request);
        
        for (int i=0;i<myresult.size();i++)
        {
            out.print((String) myresult.get(i)+"<br/>");
        }   

        
%>


