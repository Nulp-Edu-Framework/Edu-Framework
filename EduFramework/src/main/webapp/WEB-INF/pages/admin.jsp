<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<!DOCTYPE HTML>
<html>
<head>
<title>Є - СИСТЕМ</title>

<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="description" content="" />
<meta name="keywords" content="" />

<link href="http://fonts.googleapis.com/css?family=Ubuntu+Condensed"
	rel="stylesheet">

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
						<h1>
							<a href="#" id="logo">Є - СИСТЕМ</a>
						</h1>
						<nav id="nav">
							<a href="/EduFramework/">Головна</a> <a href="lecture">Лекції</a>
							<security:authorize ifAnyGranted="ROLE_ADMIN">
								<a href="admin">Адміністратор</a>
							</security:authorize>
							<a href="j_spring_security_logout">Вийти</a>
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

						<c:choose>
							<c:when test="${isCreated == true}">
								<p id="actionSucsess" class="actionSucsess">Зміни успішно
									збережені</p>
							</c:when>
							<c:when test="${isCreated == false}">
								<p id="actionError" class="actionError">Відбулась помилка
									збереження даних</p>
							</c:when>
						</c:choose>

						<h4 class="titleSection">
							Список користувачів : <a class="newElement" href='createUser'>
								Створити нового користувача </a>
						</h4>

						<div class="eduList">
							<ul id="usersList">
								<c:forEach items="${usersList}" var="item">
									<li><a href='editUser?username=${item.username}'>
											${item.getUserDetails().getFullName()} </a></li>
								</c:forEach>
							</ul>
						</div>

						<h4 class="titleSection">
							Список лекцій : <a class="newElement" href='createLection'>
								Створити нову лекцію </a>
						</h4>

						<div class="eduList">
							<ul id="lectionList">
								<c:forEach items="${lecturesList}" var="item">
									<li><a href='editLection?lectionId=${item.id}'>${item.getName()}</a>
									</li>
								</c:forEach>
							</ul>
						</div>

					</section>

				</div>
			</div>
		</div>
	</div>

	<div id="footer-wrapper">
		<div class="container">

			<div id="copyright">&copy; Автор проекту : Юрій Бугрин НУ ЛП</div>
		</div>
	</div>

	<script>
		setTimeout(function() {
			var createSucsess =  $('#actionSucsess');
			var createError =  $('#actionError');
			
			if(typeof createSucsess !== 'undefined'){
				createSucsess.fadeOut('slow');
			} 
			
			if(typeof createError !== 'undefined'){
				createError.fadeOut('slow');
			}
		}, 3000);
		</script>

</body>
</html>
