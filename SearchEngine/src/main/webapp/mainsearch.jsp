<%-- 
    Document   : mainsearch
    Created on : Oct 1, 2017, 12:41:10 PM
    Author     : tutiya
--%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>Tutiya Search Engine</title>
        
        <script src="js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript">
            
            function index(){
                $("#indexresult").html("indexing");
                $.ajax({
                    
                    type: "POST",
                    url: "indexing.jsp",
                    success: function (msg) 
                              { 
                                  $("#indexresult").html("success");
                              }
                    });
            }
            
            function search(){
                $.ajax({
                    
                    type: "POST",
                    
                   url: "searching.jsp",
                    
                    
                    data: {
                        searchtext: $("#text1").val()
                    },
                    
                    success: function (msg) 
                              { 
                                  $("#result").html(msg); 
                              }
                    });
            }
            
            function loadDoc() {
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() {
                  if (this.readyState == 4 && this.status == 200) {
                   document.getElementById("result").innerHTML = this.responseText;
                  }
                };
                xhttp.open("POST", "searching.jsp?searchtext="+document.getElementById("text1").value, true);
                xhttp.send();
              }
              


                        
        </script> 
    </head>
    <body>
        <h1>Tutiya Search Engine</h1>
        
        <form name="form1" id="form1" method="post" >
        <input type="button" value="Indexing" onclick="javascript:index()"/>
        <span id="indexresult"></span>
        <br/><br/>
        
        <input type="text" id="text1"  name="text1" />
        <input type="button" value="Search" onclick="javascript:search()"/>
        </form>
        
        <br/><br/>
        
        <div id="result">
        </div>
        
        
        
    </body>
</html>
