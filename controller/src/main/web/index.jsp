<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
    <title>Стартовая страница</title>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/header-includer.jsp"/>
<div class="container">
    <div class="row mb-3">
        <div class="col">
            <h3 class="text-center">Добро пожаловать в систему учета посетителей отеля!</h3>
        </div>
    </div>
    <div class="row mb-3 justify-content-center">
        <div class="col-10">
            <div class="list-group">
                <a href="${pageContext.request.contextPath}/rooms/types" class="list-group-item list-group-item-action">
                    <div class="fw-bold">Типы комнат</div>
                    Здесь вы можете посмотреть список типов комнат в гостинице.
                </a>
                <a href="${pageContext.request.contextPath}/rooms" class="list-group-item list-group-item-action">
                    <div class="fw-bold">Комнаты</div>
                    Здесь вы можете посмотреть список комнат в гостинице.
                </a>
                <a href="${pageContext.request.contextPath}/customers" class="list-group-item list-group-item-action">
                    <div class="fw-bold">Клиенты</div>
                    Здесь вы можете посмотреть список клиентов когда либо зарегестрированных в гостинице.
                </a>
                <a href="${pageContext.request.contextPath}/room/assignments" class="list-group-item list-group-item-action">
                    <div class="fw-bold">Назначения</div>
                    Здесь вы можете посмотреть занятые и зарезервированные комнаты гостиницы, а также историю проживания.
                </a>
                <a href="${pageContext.request.contextPath}/customer/select" class="list-group-item list-group-item-action">
                    <div class="fw-bold">Поселить</div>
                    Здесь вы можнете быстро найти клиента и поселить в комнату, по его требованияю.
                </a>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${pageContext.request.contextPath}/bootstrap-5.1.3-dist/bootstrap-css-includer.jsp"/>
</body>
</html>
