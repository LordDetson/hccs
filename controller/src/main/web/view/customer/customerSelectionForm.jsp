<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://berezkina.mitso.by/custom-taglib" prefix="custom" %>
<html>
<head>
    <jsp:include page="/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>${requestScope.selectionTableModel.title}</title>
</head>
<body>
<jsp:include page="/header-includer.jsp"/>
<div class="container">
    <form name="customer-search" action="${pageContext.request.contextPath}/customer/search" method="post">
        <div class="input-group mb-3">
            <input type="text" name="identifierNumber" value="${requestScope.identifierNumber}" class="form-control" placeholder="Поиск клиента по идентификационному номеру паспорта"
                   aria-label="Поиск клиента по идентификационному номеру паспорта" aria-describedby="customer-search" required autofocus>
            <button type="submit" id="customer-search" class="btn btn-outline-primary">Искать</button>
        </div>
    </form>
    <c:if test="${requestScope.selectionTableModel.isEmpty()}">
        <form name="customer-search" action="${pageContext.request.contextPath}/customer/add" method="get">
            <div class="mb-3">
                <input type="hidden" name="identifierNumber" value="${requestScope.identifierNumber}">
                <button type="submit" formaction="${pageContext.request.contextPath}/customer/add" class="btn btn-success">Создать</button>
            </div>
        </form>
    </c:if>
    <custom:selection-table selectionTableModel="${requestScope.selectionTableModel}"/>
</div>
<jsp:include page="/bootstrap-5.1.3-dist/bootstrap-js-includer.jsp"/>
</body>
</html>
