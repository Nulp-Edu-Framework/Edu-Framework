<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML>
<!--
	Minimaxing by HTML5 UP
	html5up.net | @n33co
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->
<html>
	<head>
		<title>Є - СИСТЕМ</title>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<meta name="description" content="" />
		<meta name="keywords" content="" />
		<link href="http://fonts.googleapis.com/css?family=Ubuntu+Condensed" rel="stylesheet">
		
		<script type="text/javascript" src="resources/jquery.min.js"></script>
		<script type="text/javascript" src="resources/skel.min.js"></script>
		<script type="text/javascript" src="resources/skel-layers.min.js"></script>
		<script type="text/javascript" src="resources/init.js"></script>

<noscript>
			<link rel="stylesheet" href="resources/skel.css" />
			<link rel="stylesheet" href="resources/style.css" />
			<link rel="stylesheet" href="resources/style-desktop.css" />
		</noscript>

		<!--[if lte IE 9]><link rel="stylesheet" href="resources/ie9.css" /><![endif]-->
		<!--[if lte IE 8]><script src="resources/html5shiv.js"></script><![endif]-->
	</head>
	<body>
		<div id="header-wrapper">
			<div class="container">
				<div class="row">
					<div class="12u">
						
						<header id="header">
							<h1><a href="#" id="logo">Є - СИСТЕМ</a></h1>
							<nav id="nav">
								<a href="/EduFramework/" class="current-page-item">Головна</a>
								<a href="lecture"">Лекції</a>
								<a href="twocolumn2.html">Веб Клієнт</a>
							</nav>
						</header>
					
					</div>
				</div>
			</div>
		</div>
		<div id="main">
			<div class="container">
			
			
			<div class="row main-row">
					<div class="12u">
					
						<section>
							<h2>список лекцій : </h2>
							<div>

	<ul class="link-list">
		<c:forEach items="${lecturesList}" var="item">
		    <li>
		    	<a href='chat?chatId=${item.getId()}'>${item.getName()}</a>
		    </li>
		</c:forEach>
	</ul>
	
	<h4>Створити лекцію : </h4>
	
	<div id="loadPresentationContent">
		<form class="createLecture" method="POST" action="api/v1/chat/uploadFile" enctype="multipart/form-data">
				<p class="load-content-lecture-lable inline">Назва лекції : </p>
				<input class="inline load-content-lecture-input" type="text" name="chatName">
				<br class="clearBoth" />
			 	<p class="load-content-file-lable inline">Вибрати ресурс презентації : </p>
			 	<input class="button inline loadBtn" type="button" value="Вибрати"/>
			 	<input id="chooseFileBtn" class="inline load-content-file-input" type="file" name="file"/>
			 	<input class="button inline loadedBtn" type="submit" value="Завантажити"/>
			<br class="clearBoth" />
    	</form>
	</div>


	<div id="result"></div>
	
	<script>
	
	$("#chooseFileBtn").change(function(){
		 //alert("choose : " + this.value);
	});
	
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
	
							</div>
						</section>

					</div>
				</div>
			</div>
		</div>
		<div id="footer-wrapper">
			<div class="container">
				
						<div id="copyright">
							&copy; Untitled. All rights reserved. | Design: <a href="http://html5up.net">HTML5 UP</a>
						</div>
			</div>
		</div>
	</body>
</html>
