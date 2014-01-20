<!DOCTYPE html>
<%@ page import="org.grails.plugins.google.visualization.data.Cell; org.grails.plugins.google.visualization.util.DateUtil" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Group statistics</title>
    
    <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    <gvisualization:pieCoreChart elementId="piechartAssays" title="Assays per group" width="${900}" height="${500}" columns="${session.groupColumnRuns}" data="${session.groupStatAssays}" />
    <gvisualization:pieCoreChart elementId="piechartRuns" title="Runs per group" width="${900}" height="${500}" columns="${session.groupColumnAssays}" data="${session.groupStatRuns}" />
</head>
<body>

 <div class="container">
	
	 	<h4>Group statistics</h4>
	
	  	 <!-- Show errors -->
    <g:if test="${flash.error}">
  		<div class="alert alert-block alert-danger">
  			<button type="button" class="close" data-dismiss="alert">&times;</button>
  			<strong>Error: </strong>${flash.error}
  		</div>
	</g:if>
	
		<!-- Show message -->
    <g:if test="${flash.message}">
  		<div class="alert alert-block alert-success">
  			<button type="button" class="close" data-dismiss="alert">&times;</button>
  			<strong>OK: </strong>${flash.message}
  		</div>
	</g:if>

	 <div id="piechartAssays"></div>
	 <div id="piechartRuns"></div>
 
	
</div> <!-- /container -->


  

</body>
</html>