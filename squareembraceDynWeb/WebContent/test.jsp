<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <title>Insert title here</title>
  <script type="text/javascript" src="js/jquery-1.9.1.min.js"></script> 
  <script type="text/javascript" src="js/jquery.history.js"></script>
  <script type="text/javascript" src="js/sqrm.js"></script>
</head>
<body>
  <script type="text/javascript">
	
    $(document).ready(function(){
       
  		$("#wikiCreate").click(function(){
  		   try
  		   {
  		    sqrm.create({
  		       title: $("#wikiTitle").val(),
  		       body: $("#wikiBody").val(),
  		       tags: [],
  		       note: $("#wikiNote").val(),
  		       success: function(data){

			       $('#wikiId').val(data.uuid);
			       $('#wikiRev').val(data.revision);
			       $('#wikiTitle').val(data.title);
		    	   $('#wikiBody').val(data.body);
		       	   $('#wikiNote').val(data.note);
		      	   $('#wikiRender').html(data.html);

		       }
  		    });
  		   } catch (e)
  		   {
  		      console.log(e);
  		   }
		   return false;
		});

  		$("#wikiUpdate").click(function(){
  		 sqrm.update({
  		       id: $("#wikiId").val(),
  		       title: $('#wikiTitle').val(),
  		       body : $("#wikiBody").val(),
  		       tags: [],
  		       note:$("#wikiNote").val(),
  		       success:function(data)
  		       {
		       		$('#wikiId').val(data.uuid);
		       		$('#wikiRev').val(data.revision);
		       		$('#wikiTitle').val(data.title);
		       		$('#wikiBody').val(data.body);
		       		$('#wikiNote').val(data.note);
			      	$('#wikiRender').html(data.html);

		   		}
  		 });
		   return false;
		});



  		$("#wikiGetId").click(function(){
  		   try
  		   {
  		 sqrm.get({
  		    id:$("#wikiId").val(),
  		    success: function(data) {
  		 
		       $('#wikiId').val(data.uuid);
		       $('#wikiRev').val(data.revision);
	           $('#wikiTitle').val(data.title);
	           $('#wikiBody').val(data.body);
	           $('#wikiNote').val(data.note);
         	   $('#wikiRender').html(data.html);
	        }});
  		   }
  		   catch (e)
  		   {
  		      console.log(e);
  		   }
		   return false;
		});
  		
  		$("#wikiDelete").click(function(){
  		 sqrm.del({
  		    id : $("#wikiId").val(),
  		    sucess: function(data){
  		 
		       $('#wikiId').val(data.uuid);
		       $('#wikiRev').val(data.revision);
	           $('#wikiTitle').val(data.title);
	           $('#wikiBody').val(data.body);
	           $('#wikiNote').val(data.note);
		       $('#wikiRender').html(data.html);
  		    }
		   });
		   return false;
		});

  		$("#wikiSearch").click(function(){
  		 sqrm.search({
  		    q : $("#wikiQ").val(),
  		    success: function(data){
  		 
		      console.log(data);
		   }
  		 });
		   return false;
		});

  		$("#login").click(function(){
  		   sqrm.login({
  		      username : $("#user").val(),
  		      password : $("#pass").val()
  		   });
  		   return false;
  		});
  		
  		$("#logout").click(function(){
  		   sqrm.logout();
  		   return false;
  		});
     });

    
  </script>
  
  <form>
  	<p><input id="user" type="text" /> <input id="pass" type="password" /> <input id="login" type="submit" value="login" /></p>
  	<p><input type="submit" id="logout" value="logout" /></p>
  	<p>q<input id="wikiQ" type="text" /> <input id="wikiSearch" type="submit" value="search" /></p>

  	<p>uuid<input id="wikiId" type="text" /> <input id="wikiGetId" type="submit" value="get by id"/></p>
  	<p>revision<input id="wikiRev" type="text" /></p>
  	<p>title<input id="wikiTitle" type="text" /> </p>
  	<p>body<textarea id="wikiBody" style="width:300px;height:200px;"></textarea></p>
  	<p>note<input id="wikiNote" type="text" /></p>
  	<p>
  		<input id="wikiReset" type="reset" value="reset" />
  		-
  		<input id="wikiCreate" type="submit" value="create" />
  		-
	  	<input id="wikiUpdate" type="submit" value="update" />
	  	-
  		<input id="wikiDelete" type="submit" value="delete"/>
  	</p>
  </form>
  
  <div id="wikiRender" style="width: 300px; height: 200px">
  </div>

</body>
</html>