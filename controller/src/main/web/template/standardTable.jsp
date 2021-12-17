<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://berezkina.mitso.by/custom-taglib" prefix="custom"%>
<html>
<head>
    <jsp:include page="/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>${requestScope.tableModel.title}</title>
</head>
<body>
<jsp:include page="/header-includer.jsp"/>
<div class="container">
    <custom:table tableModel="${requestScope.tableModel}"/>
</div>
<jsp:include page="/bootstrap-5.1.3-dist/bootstrap-js-includer.jsp"/>
</body>
</html>