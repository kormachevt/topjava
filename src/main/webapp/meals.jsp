<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>

<html lang="ru">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="styles.css">
    <title>Моя еда</title>
</head>
<body>
<h3><a href="index.html">Главная</a></h3>
<hr>
<h2>Моя еда</h2>

<table class="minimalistBlack">
    <thead>
    <tr>
        <th>Дата/Время</th>
        <th>Описание</th>
        <th>Калории</th>
        <th></th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${mealsList}" var="mealTo">
        <jsp:useBean id="mealTo" type="ru.javawebinar.topjava.model.MealTo"/>
        <c:set var="color" value="${mealTo.excess ? 'red' :'green'}"/>
        <tr style="color:${color}">
            <td><javatime:format value="${mealTo.dateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td><a href="meals?id=${mealTo.id}&action=edit" class="button">Редактировать</a></td>
            <td><a href="meals?id=${mealTo.id}&action=delete" class="button">Удалить</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<a href="meals?id=&action=edit" class="button">Добавить</a>
</body>
</html>