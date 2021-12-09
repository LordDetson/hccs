<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://berezkina.mitso.by/custom-taglib" prefix="custom"%>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>${requestScope.crudTableModel.title}</title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/header-includer.jsp"/>
<div class="container">
    <custom:crud-table crudTableModel="${requestScope.crudTableModel}"/>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>