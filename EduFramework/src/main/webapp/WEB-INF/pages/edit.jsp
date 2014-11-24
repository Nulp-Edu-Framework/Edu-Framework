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
						<nav id="nav"></nav>
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
							<c:when test="${isSuccess == true}">
								<p id="actionSucsess" class="actionSucsess">Зміни успішно
									збережені</p>
							</c:when>
							<c:when test="${isSuccess == false}">
								<p id="actionError" class="actionError">Відбулась помилка
									збереження даних</p>
							</c:when>
						</c:choose>

						<c:choose>
							<c:when test="${edit == 'user'}">

								<c:choose>
									<c:when test="${isNew == true}">
										<h4 class="titleSection">Створення користувача</h4>

										<div id="createForm">
											<form class="editForm" method="POST" action="createUser">
												<label for="login" class="editLable">Логін
													користувача : </label> <input id="login" class="editInput"
													type="text" name="login" value="" /> <label for="Password"
													class="editLable">Пароль користувача : </label> <input
													id="Password" class="editInput" type="password"
													name="password" value="" /> <label for="firstName"
													class="editLable">Ім'я користувача : </label> <input
													id="firstName" class="editInput" type="text"
													name="firstname" value="" /> <label for="lastName"
													class="editLable">Прізвище користувача : </label> <input
													id="lastName" class="editInput" type="text" name="lastname"
													value="" /> <label for="userRole" class="editLable">Тип
													користувача : </label> <select id="userRole" class="editInput"
													name="userRole">
													<option value="ROLE_USER">Студент</option>
													<option value="ROLE_LECTOR">Лектор
													<option value="ROLE_ADMIN">Адміністратор</option>
												</select> <input class="button editBtn" type="submit"
													value="Зберегти" />
											</form>
										</div>
									</c:when>
									<c:otherwise>
										<h4 class="titleSection">
											Редагування користувача <a class="newElement"
												href='deleteUser?username=${user.username}'> Видалити
												користувача </a>
										</h4>

										<div id="editForm">
											<form class="editForm" method="POST" action="editUser">
												<label for="login" class="editLable">Логін
													користувача : </label> <input id="login" class="editInput"
													type="text" name="login" value="${user.username}" readonly />
												<label for="firstName" class="editLable">Ім'я
													користувача : </label> <input id="firstName" class="editInput"
													type="text" name="firstname"
													value="${user.userDetails.firstname}" /> <label
													for="lastName" class="editLable">Прізвище
													користувача : </label> <input id="lastName" class="editInput"
													type="text" name="lastname"
													value="${user.userDetails.lastname}" /> <label
													for="secureToken" class="editLable">Код безпеки : </label>
												<input id="secureToken" class="editInput" type="text"
													name="secureToken" value="${user.secureToken}" /> <label
													for="userRole" class="editLable">Тип користувача :
												</label> <select id="userRole" class="editInput" name="userRole">
													<option
														<c:if test="${user.userRole.role == 'ROLE_USER'}">selected</c:if>
														value="ROLE_USER">Студент</option>
													<option
														<c:if test="${user.userRole.role == 'ROLE_LECTOR'}">selected</c:if>
														value="ROLE_LECTOR">Лектор
													<option
														<c:if test="${user.userRole.role == 'ROLE_ADMIN'}">selected</c:if>
														value="ROLE_ADMIN">Адміністратор</option>
												</select> <input class="button editBtn" type="submit"
													value="Зберегти" />
											</form>
										</div>

									</c:otherwise>
								</c:choose>

							</c:when>
							<c:when test="${edit == 'lection'}">

								<c:choose>
									<c:when test="${isNew == true}">
										<h4 class="titleSection">Створення лекції :</h4>

										<div id="createNewLection">
											<form class="editPresentForm" method="POST"
												action="api/v1/chat/uploadFile"
												enctype="multipart/form-data">
												<input style="display: none" type="text" name="source"
													value="admin" /> <label for="lectureName"
													class="editPresentLable">Назва лекції : </label> <input
													id="lectureName" class="editPresentInput" type="text"
													name="chatName" value="" /> <label
													for="createLectionFakeBtn" id="loadedFileName"
													class="editPresentLable"> Вибрати презентації :</label> <input
													id="createLectionLoadBtn" class="button editPresentInput"
													type="submit" value="Зберегти" /> <input
													id="createLectionFakeBtn" class="button editPresentInput"
													type="button" value="Вибрати" /> <input id="chooseFileBtn"
													class="create-presentation-file-input editPresentInput"
													type="file" name="file" />
											</form>
										</div>
									</c:when>
									<c:otherwise>
										<h4 class="titleSection">
											Редагування лекції : <a class="newElement"
												href='deleteLecture?id=${lection.id}'> Видалити лекцію </a>
										</h4>

										<div id="createNewLection">
											<form class="editPresentForm" method="POST"
												action="api/v1/chat/uploadFile"
												enctype="multipart/form-data">
												<input style="display: none" type="text" name="edit"
													value="true" /> <input style="display: none" type="text"
													name="id" value="${lection.id}" /> <input
													style="display: none" type="text" name="source"
													value="admin" /> <label for="lectureName"
													class="editPresentLable">Назва лекції : </label> <input
													id="lectureName" class="editPresentInput" type="text"
													name="chatName" value="${lection.name}" /> <label
													for="createLectionFakeBtn" id="loadedFileName"
													class="editPresentLable"> Змінити презентації :</label> <input
													id="createLectionLoadBtn" class="button editPresentInput"
													type="submit" value="Зберегти" /> <input
													id="createLectionFakeBtn" class="button editPresentInput"
													type="button" value="Вибрати" /> <input id="chooseFileBtn"
													class="create-presentation-file-input editPresentInput"
													type="file" name="file" />
											</form>
										</div>
									</c:otherwise>
								</c:choose>

							</c:when>
						</c:choose>

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

	<script type="text/javascript">
		
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
			
			$("#chooseFileBtn").change(function() {
				$("#loadedFileName").text("Файл вибрано :");
				$("#loadedFileName").addClass("sucsess");
			});
			
			$("#nav").append('<a href="' + document.referrer + '"> <-- Повернутись на попередню</a>');
		</script>



</body>
</html>
