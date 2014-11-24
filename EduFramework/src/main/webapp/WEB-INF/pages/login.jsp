<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
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


						<div id="login-box">

							<form class="loginForm" name='loginForm'
								action="<c:url value='j_spring_security_check' />" method='POST'>
								<h1 class="loginTitle">Увійдіть у системи з логіном та
									паролем користувача</h1>

								<c:if test="${not empty error}">
									<div class="loginError">${error}</div>
								</c:if>

								<label for="login" class="editLable">Логін користувача :
								</label> <input id="login" class="loginInput" type="text"
									name="j_username" value="" /> <label for="Password"
									class="editLable">Пароль користувача : </label> <input
									id="Password" class="loginInput" type="password"
									name="j_password" value="" /> <input class="button loginBtn"
									type="submit" value="Увійти" />
							</form>
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

</body>
</html>