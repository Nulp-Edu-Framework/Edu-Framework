<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Edu Framework</title>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
</head>
<body>
	<h4>Lectures list :</h4>

	<ul>
		<c:forEach items="${lecturesList}" var="item">
		    <li>
		    	<a href='chat?chatId=${item.getId()}'>${item.getName()}</a>
		    </li>
		</c:forEach>
	</ul>
	
	<form action="api/v1/chat/create" id="postForm">
		Lecture name: <input type="text" name="chatName">
		<input type="submit" value="Search">
	</form>


	<div id="result"></div>
	
	<script>
		$( "#postForm" ).submit(function( event ) {
			
			event.preventDefault();
			
			var $form = $( this ),
			
			term = $form.find( "input[name='chatName']" ).val(),
			url = $form.attr( "action" );
			
			var posting = $.post( url, { chatName: term } );
			
			posting.done(function( data ) {
				$( "#result" ).empty().append( data );
			});
		});
	</script>

</body>
</html>
