<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>Создать тип комнаты</title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/header-includer.jsp"/>
<div class="container">
    <form name="newRoomType" action="${pageContext.request.contextPath}/room/type/add" method="post">
        <c:forEach items="${requestScope.fields}" var="field" varStatus="status">
            <div class="mb-3 row">
                <label for="${field.name}" class="col-sm-3 col-form-label">${field.caption}:</label>
                <div class="col-sm-9">
                    <input type="${field.type}" id="${field.name}" name="${field.name}" class="form-control">
                </div>
            </div>
        </c:forEach>
        <button type="submit" class="btn btn-success">Создать</button>
    </form>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>
