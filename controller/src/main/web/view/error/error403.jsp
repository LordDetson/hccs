<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>Доступ закрыт</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col">
            <h1 class="text-center">Code 403: Запрещенный!</h1>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <p class="text-center"><i>Доступ закрыт. У вас нет разрешения на доступ к этой странице на этом сервере.</i></p>
        </div>
    </div>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>
