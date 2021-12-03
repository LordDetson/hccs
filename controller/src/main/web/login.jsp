<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <link href="${pageContext.request.contextPath}/css/login.css" rel="stylesheet" type="text/css">
    <title>Вход</title>
</head>
<body>
<div id="login-form">
    <form class="d-grid form-sigin" action="j_security_check" method="post">
        <h1 class="h3 mb-3 font-weight-normal" style="text-align: center">Форма входа</h1>
        <div class="form-floating">
            <input type="text" id="userName" name="j_username" class="form-control" placeholder="Имя пользователя" required autofocus>
            <label for="userName">Имя пользователя</label>
        </div>
        <div class="form-floating">
            <input type="password" id="password" name="j_password" class="form-control" placeholder="Пароль" required>
            <label for="password">Пароль</label>
        </div>
        <button class="btn btn-success btn-block" type="submit" value="Login">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                 class="bi bi-box-arrow-in-right" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                      d="M6 3.5a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-2a.5.5 0 0 0-1 0v2A1.5 1.5 0 0 0 6.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-8A1.5 1.5 0 0 0 5 3.5v2a.5.5 0 0 0 1 0v-2z"/>
                <path fill-rule="evenodd"
                      d="M11.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H1.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z"/>
            </svg>
            Войти
        </button>
    </form>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>
