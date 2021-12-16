<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>Forbidden</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col">
            <h1 class="text-center">Code 403: Forbidden!</h1>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <p class="text-center"><i>Access denied. You don't have the permission to access this page on this server.</i></p>
        </div>
    </div>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>
