<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
<meta charset="utf-8">
<title>Spring - Atmosphere chat example</title>
<style>
* {
	font-family: tahoma;
	font-size: 12px;
	padding: 0px;
	margin: 0px;
}

p {
	line-height: 18px;
}

div {
	width: 500px;
	margin-left: auto;
	margin-right: auto;
}

#presentation {
	text-align: center;
}

#content {
	padding: 5px;
	background: #ddd;
	border-radius: 5px;
	border: 1px solid #CCC;
	margin-top: 10px;
}

#input {
	border-radius: 2px;
	border: 1px solid #ccc;
	margin-top: 10px;
	padding: 5px;
	width: 400px;
}

#status {
	width: 88px;
	display: block;
	float: left;
	margin-top: 15px;
}
</style>
</head>
<body>
	<div id="presentation">
		<button id="restart" type="button">RESTART</button>
		<button id="prev" type="button">PREV</button>
		<button id="next" type="button">NEXT</button>
		<div>
			Current slide : <span id="currentStep"></span>
		</div>
		<div>
			Slides count : <span id="stepCount"></span>
		</div>
	</div>

	<div id="content"></div>
	<div>
		<span id="status">Connecting...</span> <input type="text" id="input"
			disabled="disabled" />
	</div>
	<script type="text/javascript">
    	chatId = ${chatId};
    	senderName = '${senderName}';
    	secureToken = '${secureToken}';
    </script>
	<script type="text/javascript"
		src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
	<script type="text/javascript" src="resources/jquery.atmosphere.js"></script>
	<script type="text/javascript" src="resources/atmosphere.js"></script>
	<script type="text/javascript" src="resources/presentation.js"></script>
</body>
</html>
